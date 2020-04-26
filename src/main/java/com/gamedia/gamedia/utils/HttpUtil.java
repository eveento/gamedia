package com.gamedia.gamedia.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class HttpUtil {

    @Value("${crypto.api.key}")
    private static String apiKey;

    private static HttpHeaders headers() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Apikey", apiKey);
        return httpHeaders;
    }

    public static HttpEntity httpEntity() {
        return new HttpEntity(headers());
    }

}
