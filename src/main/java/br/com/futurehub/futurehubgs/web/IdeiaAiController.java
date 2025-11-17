package br.com.futurehub.futurehubgs.web;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai/ideias")
public class IdeiaAiController {

    private final ChatClient chatClient;

    // 👇 ÚNICO construtor, o Spring injeta o Builder aqui
    @Autowired
    public IdeiaAiController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @PostMapping("/sugerir")
    public ResponseEntity<Map<String, Object>> sugerir(@RequestBody Map<String, String> body) {
        String tema = body.getOrDefault("tema", "energia sustentável em escolas");

        String conteudo = chatClient
                .prompt("Liste 3 ideias curtas e práticas para: " + tema)
                .call()
                .content();

        return ResponseEntity.ok(
                Map.of(
                        "tema", tema,
                        "sugestoes", conteudo
                )
        );
    }
}
