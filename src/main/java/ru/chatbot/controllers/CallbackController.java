package ru.chatbot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.chatbot.pojo.RequestBodyDTO;
import ru.chatbot.utils.MessageSender;

/**
 * Controller for VK Callback API
 */
@RestController
public class CallbackController {

    @Autowired
    private MessageSender messageSender;

    @Value("${vk.group_id}")
    private Integer groupId;

    @Value("${vk.auth.confirm_code}")
    private String confirmCode;

    private Logger log = LoggerFactory.getLogger(CallbackController.class);

    /**
     * Endpoint for VK Callback API
     * @param requestBodyDTO json body of request
     * @return status code 200 and string "ok"
     */
    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public String callback(@RequestBody RequestBodyDTO requestBodyDTO) {
        log.info("Request received: {}", requestBodyDTO);
        if (requestBodyDTO.getType() != null && requestBodyDTO.getGroupId() != null && requestBodyDTO.getGroupId().equals(groupId)) {
            switch (requestBodyDTO.getType()) {
                case "confirmation":
                    log.info("Response with confirmation code was sent");
                    return confirmCode;
                case "message_new":
                    Integer userId = requestBodyDTO.getObject().getUserId();
                    String message = requestBodyDTO.getObject().getBody();
                    String eventId = requestBodyDTO.getEventId();
                    if (userId != null && message != null) {
                        messageSender.sendMessage(userId, "Вы сказали: " + message, eventId);
                        return "ok";
                    }
                    break;
            }
        }
        log.warn("The request '{}' was not processed", requestBodyDTO);
        return "ok";
    }
}