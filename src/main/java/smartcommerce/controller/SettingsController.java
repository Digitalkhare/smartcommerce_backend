package smartcommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import smartcommerce.service.SettingsService;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    @GetMapping("/tts-mode")
    public ResponseEntity<Map<String, String>> getTtsMode() {
        String mode = settingsService.getTtsMode();
        return ResponseEntity.ok(Map.of("ttsMode", mode));
    }

    @PutMapping("/tts-mode")
    public ResponseEntity<?> updateTtsMode(@RequestBody Map<String, String> body) {
        String mode = body.get("ttsMode");
        settingsService.updateTtsMode(mode);
        return ResponseEntity.ok().build();
    }
}
