package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.model.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TalkController {

    @Value("classpath:/static/corgi.jpg")
    Resource image;

    private final OllamaChatModel chatModel;

    @Autowired
    public TalkController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/talk")
    public String talk(@RequestParam(value = "message", defaultValue = "Explain what do you see on this picture?") String message, Model model) {
        var userMessage = new UserMessage(message, List.of(new Media(MimeTypeUtils.IMAGE_PNG, image)));
        
        model.addAttribute("prompt", message);
        model.addAttribute("answer", chatModel.call(userMessage));
        
        return "talk";
    }
}
