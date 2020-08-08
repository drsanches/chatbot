package ru.chatbot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class MessageSender {

    private RestTemplate restTemplate;
    private HttpEntity<String> entity;
    private static final Logger LOG = LoggerFactory.getLogger(MessageSender.class);

    @Value("${vk.auth.access_token}")
    private String accessToken;

    @Value("${vk.api.send_message_url}")
    private String url;

    @Value("${vk.api.version}")
    private String version;

    public MessageSender() {
        restTemplate = new RestTemplate();
        entity = new HttpEntity<>(new HttpHeaders());
    }

    public void sendMessage(Integer userId, String message, String eventId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("access_token", accessToken)
                .queryParam("user_id", userId)
                .queryParam("v", version)
                .encode();
        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString() + "&message=" + message,
                HttpMethod.POST,
                entity,
                String.class);
        LOG.info("Message to user {} has been sent with response {}. Event id = {}", userId, response.getBody(), eventId);
    }
}