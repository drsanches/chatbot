package ru.chatbot.controllers

import org.json.JSONObject
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import ru.chatbot.Application
import ru.chatbot.utils.MessageSender
import spock.lang.Specification

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CallbackControllerAPITest extends Specification {

    @Value('${vk.group_id}')
    private Integer groupId

    @Value('${vk.auth.confirm_code}')
    private String confirmCode

    @LocalServerPort
    private Integer port

    @SpringBean
    private MessageSender messageSender = Mock()

    def "check that callback sends correct response on confirmation request"() {
        given: "request"
        HttpHeaders headers = new HttpHeaders()
        headers.add("Content-Type", "application/json")
        HttpEntity<String> request = new HttpEntity<>(createConfirmJson(groupId), headers)

        when: "request is sent"
        ResponseEntity<String> response = new RestTemplate()
                .postForEntity("http://localhost:" + port + "/callback", request, String.class)

        then: "sends correct response"
        response.getStatusCode() == HttpStatus.OK
        response.body == confirmCode
    }

    def "check that callback sends correct response on new message request"() {
        given: "request"
        HttpHeaders headers = new HttpHeaders()
        headers.add("Content-Type", "application/json")
        String json = createNewMessageJson(new Random().nextInt(), UUID.randomUUID().toString(), groupId)
        HttpEntity<String> request = new HttpEntity<>(json, headers)

        when: "request is sent"
        ResponseEntity<String> response = new RestTemplate()
                .postForEntity("http://localhost:" + port + "/callback", request, String.class)

        then: "sends correct response"
        response.getStatusCode() == HttpStatus.OK
        response.body == "ok"

        where:
        userId                  |  body
        null                    |  null
        null                    |  UUID.randomUUID().toString()
        new Random().nextInt()  |  null
        new Random().nextInt()  |  UUID.randomUUID().toString()
    }

    def "check that callback sends correct response on incorrect request"() {
        given: "request"
        HttpHeaders headers = new HttpHeaders()
        headers.add("Content-Type", "application/json")
        HttpEntity<String> request = new HttpEntity<>(json, headers)

        when: "request is sent"
        ResponseEntity<String> response = new RestTemplate()
                .postForEntity("http://localhost:" + port + "/callback", request, String.class)

        then: "sends correct response"
        response.getStatusCode() == HttpStatus.OK
        response.body == "ok"

        where:
        json << [
                "{}",
                createNewMessageJson(new Random().nextInt(), UUID.randomUUID().toString(), new Random().nextInt())
        ]
    }

    private String createConfirmJson(Integer groupId) {
        return new JSONObject()
                .put("type", "confirmation")
                .put("group_id", groupId)
                .toString()
    }

    private String createNewMessageJson(Integer userId, String body, Integer groupId) {
        return new JSONObject()
                .put("type", "message_new")
                .put("object", new JSONObject()
                        .put("user_id", userId)
                        .put("body", body))
                .put("group_id", groupId)
                .toString()
    }
}