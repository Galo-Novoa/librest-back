package com.galonovoa.mercado.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;

@RestController
public class CloudinaryController {

    private final String cloudinaryApiKey = System.getenv("CLOUDINARY_API_KEY");
    private final String cloudinaryApiSecret = System.getenv("CLOUDINARY_API_SECRET");
    private final String cloudinaryCloudName = System.getenv("CLOUDINARY_CLOUD_NAME");

    @GetMapping("/cloudinary/signature")
    public Map<String, Object> getSignature() {
        long timestamp = System.currentTimeMillis() / 1000;

        String toSign = "timestamp=" + timestamp + cloudinaryApiSecret;
        String signature = DigestUtils.sha1Hex(toSign);

        return Map.of(
            "signature", signature,
            "timestamp", timestamp,
            "apiKey", cloudinaryApiKey,
            "cloudName", cloudinaryCloudName
        );
    }
}