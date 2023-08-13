package server.nanum.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버 상태 확인용 컨트롤러
 * 서버 상태를 확인합니다.
 * @author Jinyeong Seol
 * @version 1.0.0
 * @since 2023-08-13
 */

@Slf4j
@RestController
@Hidden
@RequestMapping("/api/health-check")
public class HealthCheckController {
    @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Healthy");
    }
}

