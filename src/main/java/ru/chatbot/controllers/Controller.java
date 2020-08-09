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

@RestController
public class Controller {

    @Autowired
    private MessageSender messageSender;

    @Value("${vk.group_id}")
    private Integer groupId;

    @Value("${vk.auth.confirm_code}")
    private String confirmCode;

    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public String callback(@RequestBody RequestBodyDTO requestBodyDTO) {
        LOG.info("Request received: {}", requestBodyDTO);
        if (requestBodyDTO.getType() != null) {
            switch (requestBodyDTO.getType()) {
                case "confirmation":
                    if (requestBodyDTO.getGroupId() != null && requestBodyDTO.getGroupId().equals(groupId)) {
                        LOG.info("Response with confirmation code was sent");
                        return confirmCode;
                    }
                    break;
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
        LOG.warn("The request '{}' was not processed", requestBodyDTO);
        return "ok";
    }
}