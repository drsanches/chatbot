package ru.chatbot.controllers

import org.slf4j.Logger
import ru.chatbot.pojo.ObjectDTO
import ru.chatbot.pojo.RequestBodyDTO
import ru.chatbot.utils.MessageSender
import spock.lang.Specification

class CallbackControllerTest extends Specification {

    def "check that callback returns correct confirmation code"() {
        given: "CallbackController object"
        CallbackController callbackController = new CallbackController()
        callbackController.groupId = new Random().nextInt()
        callbackController.confirmCode = UUID.randomUUID().toString()
        callbackController.log = Mock(Logger)
        callbackController.messageSender = Mock(MessageSender)

        and: "request"
        String type = "confirmation"
        RequestBodyDTO requestBodyDTO = new RequestBodyDTO(type, null, callbackController.groupId, null)

        when: "callback is called"
        String confirmCode = callbackController.callback(requestBodyDTO)

        then: "it logs request body"
        1 * callbackController.log.info("Request received: {}", requestBodyDTO)

        and: "doesn't send message to the user"
        0 * callbackController.messageSender.sendMessage(*_)

        and: "logs message about confirmation code response"
        1 * callbackController.log.info("Response with confirmation code was sent")

        and: "returns correct confirmation code"
        confirmCode == callbackController.confirmCode
    }

    def "check that callback sends correct message to user"() {
        given: "CallbackController object"
        CallbackController callbackController = new CallbackController()
        callbackController.groupId = new Random().nextInt()
        callbackController.confirmCode = UUID.randomUUID().toString()
        callbackController.log = Mock(Logger)
        callbackController.messageSender = Mock(MessageSender)

        and: "request"
        String type = "message_new"
        String eventId = UUID.randomUUID().toString()
        Integer userId = new Random().nextInt()
        String body = UUID.randomUUID().toString()
        ObjectDTO objectDTO = new ObjectDTO(null, null, null, userId, null, null, body, null)
        RequestBodyDTO requestBodyDTO = new RequestBodyDTO(type, objectDTO, callbackController.groupId, eventId)

        when: "callback is called"
        String response = callbackController.callback(requestBodyDTO)

        then: "it logs request body"
        1 * callbackController.log.info("Request received: {}", requestBodyDTO)

        and: "sends correct message to the user"
        1 * callbackController.messageSender.sendMessage(userId, "Вы сказали: " + body, eventId)

        and: "returns ok"
        response == "ok"
    }

    def "check that callback do nothing for incorrect request"() {
        given: "CallbackController object"
        CallbackController callbackController = new CallbackController()
        callbackController.groupId = 100
        callbackController.confirmCode = UUID.randomUUID().toString()
        callbackController.log = Mock(Logger)
        callbackController.messageSender = Mock(MessageSender)

        and: "request"
        ObjectDTO objectDTO = new ObjectDTO(null, null, null, userId, null, null, body, null)
        RequestBodyDTO requestBodyDTO = new RequestBodyDTO(type, objectDTO, groupId, null)

        when: "callback is called"
        String response = callbackController.callback(requestBodyDTO)

        then: "it logs request body"
        1 * callbackController.log.info("Request received: {}", requestBodyDTO)

        and: "don't send the message"
        0 * callbackController.messageSender.sendMessage(*_)

        and: "logs message about unhandled request"
        1 * callbackController.log.warn("The request '{}' was not processed", requestBodyDTO)

        and: "returns ok"
        response == "ok"

        where:
        type                          |  groupId  |  userId                  |  body
        null                          |  100      |  new Random().nextInt()  |  UUID.randomUUID().toString()
        UUID.randomUUID().toString()  |  null     |  new Random().nextInt()  |  UUID.randomUUID().toString()
        UUID.randomUUID().toString()  |  101      |  new Random().nextInt()  |  UUID.randomUUID().toString()
        UUID.randomUUID().toString()  |  100      |  new Random().nextInt()  |  UUID.randomUUID().toString()
        "message_new"                 |  100      |  null                    |  UUID.randomUUID().toString()
        "message_new"                 |  100      |  new Random().nextInt()  |  null
    }
}