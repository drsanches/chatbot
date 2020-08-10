package ru.chatbot.utils

import org.slf4j.Logger
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class MessageSenderTest extends Specification {

    def "check that sendMessage sends correct request to VK API"() {
        given: "MessageSender object, user id, message and event id"
        MessageSender messageSender = new MessageSender()
        messageSender.accessToken = UUID.randomUUID().toString()
        messageSender.url = "http://" + UUID.randomUUID().toString()
        messageSender.version = UUID.randomUUID().toString()
        messageSender.restTemplate = Mock(RestTemplate)
        messageSender.log = Mock(Logger)
        Integer userId = new Random().nextInt()
        String message = UUID.randomUUID().toString()
        String eventId = UUID.randomUUID().toString()

        and: "correct request"
        String responseBody = UUID.randomUUID().toString()
        MultiValueMap<String, String> parameters= new LinkedMultiValueMap<>()
        parameters.add("access_token", messageSender.accessToken)
        parameters.add("user_id", userId.toString())
        parameters.add("v", messageSender.version)
        parameters.add("message", message)
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, new HttpHeaders())

        when: "sendMessage is called"
        messageSender.sendMessage(userId, message, eventId)

        then: "it sends correct request to url"
        1 * messageSender.restTemplate.postForEntity(messageSender.url, request, String.class) >> Mock(ResponseEntity) {
                getBody() >> responseBody
            }

        and: "writes log"
        1 * messageSender.log.info("Message to user {} has been sent with response {}. Event id = {}", userId, responseBody, eventId)
    }
}