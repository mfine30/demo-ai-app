package com.vmware.tanzu.app.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import reactor.core.Exceptions;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

record Prompt(String text, Optional<Resource> image) {

    Prompt(String text, Resource image) throws IOException {
        this(text, image.contentLength() == 0 ? Optional.empty() : Optional.of(image));
    }

    public String encodedImage() {
        return image
                .map(i -> String.format("data:%s;base64,%s", mediaType(i), encode(i)))
                .orElse("");
    }

    void user(ChatClient.PromptUserSpec spec) {
        spec.text(text);
        image.ifPresent(i -> spec.media(mediaType(i), i));
    }

    private static MediaType mediaType(Resource resource) {
        return MediaTypeFactory.getMediaType(resource).orElseThrow();
    }

    private static String encode(Resource resource) {
        try {
            return Base64.getEncoder().encodeToString(resource.getContentAsByteArray());
        } catch (IOException e) {
            throw Exceptions.propagate(e);
        }
    }
}
