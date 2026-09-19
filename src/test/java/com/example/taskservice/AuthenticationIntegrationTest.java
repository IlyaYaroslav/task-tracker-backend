package com.example.taskservice;

import com.example.taskservice.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "security.jwt.secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=")
class AuthenticationIntegrationTest {
    private static final String SECRET = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper json = new ObjectMapper();
    @Value("${local.server.port}") int port;
    @Autowired UserRepository users;
    @Autowired PasswordEncoder passwords;

    @Test
    void registrationLoginAndProjectFlowUsesOneBackend() throws Exception {
        String email = UUID.randomUUID() + "@example.com";
        String registration = "{\"name\":\"Alice\",\"email\":\"" + email + "\",\"password\":\"password123\"}";
        var registered = request("POST", "/auth/register", registration, null);
        assertThat(registered.statusCode()).isEqualTo(200);
        JsonNode account = json.readTree(registered.body());
        String token = account.path("token").asText();
        String userId = account.path("id").asText();
        assertThat(token).isNotBlank();
        var stored = users.findByEmail(email).orElseThrow();
        assertThat(stored.getPassword()).isNotEqualTo("password123");
        assertThat(passwords.matches("password123", stored.getPassword())).isTrue();
        assertThat(stored.getRole()).isEqualTo("USER");
        assertThat(request("POST", "/auth/register", registration, null).statusCode()).isEqualTo(409);

        var login = request("POST", "/auth/login",
                "{\"email\":\"" + email + "\",\"password\":\"password123\"}", null);
        assertThat(login.statusCode()).isEqualTo(200);
        token = json.readTree(login.body()).path("accessToken").asText();
        assertThat(request("GET", "/users", null, token).statusCode()).isEqualTo(200);
        assertThat(request("GET", "/users/" + userId, null, token).statusCode()).isEqualTo(200);
        assertThat(request("PATCH", "/users/" + userId + "/name",
                "{\"newFirstName\":\"Updated\"}", token).statusCode()).isEqualTo(200);

        var other = request("POST", "/auth/register",
                registration.replace(email, UUID.randomUUID() + "@example.com"), null);
        String otherToken = json.readTree(other.body()).path("token").asText();
        assertThat(request("PATCH", "/users/" + userId + "/name",
                "{\"newFirstName\":\"Intruder\"}", otherToken).statusCode()).isEqualTo(403);
        assertThat(request("PATCH", "/users/" + userId + "/password",
                "{\"oldPassword\":\"password123\",\"newPassword\":\"changed123\"}", otherToken).statusCode()).isEqualTo(403);
        assertThat(request("PUT", "/users/" + userId + "/profile-picture", "image", otherToken).statusCode()).isEqualTo(403);
        assertThat(request("DELETE", "/users/" + userId + "/profile-picture", null, otherToken).statusCode()).isEqualTo(403);

        var project = request("POST", "/projects", "{\"name\":\"Test\",\"key\":\"T" + UUID.randomUUID().toString().substring(0, 8) + "\"}", token);
        assertThat(project.statusCode()).isEqualTo(201);
        String projectId = json.readTree(project.body()).path("id").asText();
        var projects = request("GET", "/projects", null, token);
        assertThat(projects.statusCode()).isEqualTo(200);
        assertThat(projects.body()).contains(projectId);
        assertThat(request("DELETE", "/projects/" + projectId, null, otherToken).statusCode()).isEqualTo(403);
        assertThat(request("DELETE", "/projects/" + projectId, null, token).statusCode()).isEqualTo(204);

        assertThat(request("PATCH", "/users/" + userId + "/password",
                "{\"oldPassword\":\"wrongpass\",\"newPassword\":\"changed123\"}", token).statusCode()).isEqualTo(401);
        assertThat(request("PATCH", "/users/" + userId + "/password",
                "{\"oldPassword\":\"password123\",\"newPassword\":\"changed123\"}", token).statusCode()).isEqualTo(200);
        assertThat(request("POST", "/auth/login",
                "{\"email\":\"" + email + "\",\"password\":\"password123\"}", null).statusCode()).isEqualTo(401);
        assertThat(request("POST", "/auth/login",
                "{\"email\":\"" + email + "\",\"password\":\"changed123\"}", null).statusCode()).isEqualTo(200);
    }

    @Test
    void rejectsAnonymousInvalidAndExpiredTokens() throws Exception {
        assertThat(request("GET", "/users", null, null).statusCode()).isEqualTo(401);
        assertThat(request("GET", "/projects", null, null).statusCode()).isEqualTo(401);
        assertThat(request("POST", "/projects", "{}", null).statusCode()).isEqualTo(401);
        assertThat(request("GET", "/users", null, "bad-token").statusCode()).isEqualTo(401);
        String expired = Jwts.builder().setSubject(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() - 60000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET)), SignatureAlgorithm.HS256).compact();
        assertThat(request("GET", "/users", null, expired).statusCode()).isEqualTo(401);
        assertThat(request("POST", "/auth/register", "{}", null).statusCode()).isEqualTo(400);
        assertThat(request("POST", "/auth/login",
                "{\"email\":\"missing@example.com\",\"password\":\"password123\"}", null).statusCode()).isEqualTo(401);
        assertThat(request("GET", "/v3/api-docs", null, null).statusCode()).isEqualTo(200);
    }

    private HttpResponse<String> request(String method, String path, String body, String token) throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/task-service" + path))
                .header("Content-Type", "application/json")
                .method(method, body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
        if (token != null) request.header("Authorization", "Bearer " + token);
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }
}
