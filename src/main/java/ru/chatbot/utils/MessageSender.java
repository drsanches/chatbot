package ru.chatbot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Spring component for sending private messages to users in VK.
 * @see Component
 */
@Component
public class MessageSender {

    private RestTemplate restTemplate = new RestTemplate();
    private static final Logger LOG = LoggerFactory.getLogger(MessageSender.class);

    @Value("${vk.auth.access_token}")
    private String accessToken;

    @Value("${vk.api.send_message_url}")
    private String url;

    @Value("${vk.api.version}")
    private String version;

    /**
     * Sends a private message to the VK user
     * @param userId user id to send the message
     * @param message message text
     * @param eventId id of the event that triggered the message (used for logging)
     */
    public void sendMessage(Integer userId, String message, String eventId) {
        MultiValueMap<String, String> parameters= new LinkedMultiValueMap<>();
        parameters.add("access_token", accessToken);
        parameters.add("user_id", userId.toString());
        parameters.add("v", version);
        parameters.add("message", message);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, new HttpHeaders());
        HttpEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        LOG.info("Message to user {} has been sent with response {}. Event id = {}", userId, response.getBody(), eventId);
    }
}