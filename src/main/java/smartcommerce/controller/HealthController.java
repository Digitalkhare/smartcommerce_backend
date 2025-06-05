package smartcommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/health/db")
    public ResponseEntity<String> checkDatabase() {
        try (Connection conn = dataSource.getConnection()) {
            return ResponseEntity.ok("✅ Database connection successful");
        } catch (Exception e) {
            // Print full stack trace to Heroku logs
            e.printStackTrace();

            // Return message to client
            return ResponseEntity.status(500)
                    .body("❌ Database connection failed: " + e.getMessage());
        }
    }
}
