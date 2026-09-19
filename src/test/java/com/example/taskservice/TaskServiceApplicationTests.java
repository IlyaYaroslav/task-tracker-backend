package com.example.taskservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
        "security.jwt.secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=",
        "minio.initialize-bucket=false"}
)
@ActiveProfiles("test")
class TaskServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
