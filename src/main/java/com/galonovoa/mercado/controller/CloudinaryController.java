package com.galonovoa.mercado.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import io.github.cdimascio.dotenv.Dotenv;

@RestController
public class CloudinaryController {

    private final Dotenv dotenv = Dotenv.load();

    private final String cloudinaryApiKey = dotenv.get("CLOUDINARY_API_KEY");
    private final String cloudinaryApiSecret = dotenv.get("CLOUDINARY_API_SECRET");
    private final String cloudinaryCloudName = dotenv.get("CLOUDINARY_CLOUD_NAME");

    @GetMapping("/cloudinary/signature")
    public Map<String, Object> getSignature() {
        long timestamp = System.currentTimeMillis() / 1000;
        String toSign = "timestamp=" + timestamp + cloudinaryApiSecret;
        String signature = org.apache.commons.codec.digest.DigestUtils.sha1Hex(toSign);

        return Map.of(
            "signature", signature,
            "timestamp", timestamp,
            "apiKey", cloudinaryApiKey,
            "cloudName", cloudinaryCloudName
        );
    }
}
