package com.vmware.tanzu.app.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
class MainController {

    private final ChatClient client;

    public MainController(ChatClient.Builder builder) {
        this.client = builder.build();
    }

    @GetMapping("/")
    Rendering get() {
        return Rendering
                .view("index")
                .build();
    }

    @PostMapping("/")
    Mono<Rendering> post(@RequestPart("prompt.text") String text, @RequestPart("prompt.image") Resource image) throws IOException {
        Prompt prompt = new Prompt(text, image);

        return client.prompt()
                .user(prompt::user)
                .stream()
                .content()
                .collect(Collectors.joining())
                .map(response -> Rendering
                        .view("index")
                        .modelAttribute("response", new Response(prompt, response))
                        .build());
    }
}
