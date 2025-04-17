package smartcommerce.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import smartcommerce.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

//    @PostMapping
//    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> payload) {
//        String userMessage = payload.get("message");
//        String response = chatService.getChatResponse(userMessage);
//        return ResponseEntity.ok(Map.of("reply", response));
//    }
    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> body, Principal principal) {
        String userEmail = principal.getName();
        String message = body.get("message");
        String reply = chatService.getSmartResponse(userEmail, message);
        return ResponseEntity.ok(Map.of("reply", reply));
    }
}

