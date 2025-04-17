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

import smartcommerce.service.TextToSpeechService;

@RestController
@RequestMapping("/api/tts")
public class TextToSpeechController {

    @Autowired
    private TextToSpeechService ttsService;

    @PostMapping
    public ResponseEntity<byte[]> textToSpeech(@RequestBody Map<String, String> payload) {
        String text = payload.get("text");
        byte[] audio = ttsService.synthesizeSpeech(text);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(audio, headers, HttpStatus.OK);
    }
}

