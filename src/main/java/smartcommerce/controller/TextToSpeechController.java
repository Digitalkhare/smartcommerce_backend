package smartcommerce.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import smartcommerce.service.SettingsService;
import smartcommerce.service.TextToSpeechService;

@RestController
@RequestMapping("/api/tts")
public class TextToSpeechController {

    @Autowired
    private TextToSpeechService ttsService;
    @Autowired
    private SettingsService settingsService;

    @PostMapping
    public ResponseEntity<byte[]> textToSpeech(@RequestBody Map<String, String> payload) {
        String text = payload.get("text");
        if ("native".equals(settingsService.getTtsMode())) {
            // Return empty or dummy response
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        byte[] audio = ttsService.synthesizeSpeech(text);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(audio, headers, HttpStatus.OK);
    }
}

