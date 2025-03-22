package com.example.service;

import com.example.entity.Message;
import com.example.exception.InvalidInputException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            throw new InvalidInputException("Message text cannot be blank");
        }
        if (message.getMessageText().length() > 255) {
            throw new InvalidInputException("Message text must be under 255 characters");
        }
        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new InvalidInputException("PostedBy does not refer to a valid account");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer id) {
        return messageRepository.findById(id).orElse(null);
    }

    public int deleteMessage(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public int updateMessage(Integer id, String newMessageText) {
        if (newMessageText == null || newMessageText.trim().isEmpty()) {
            throw new InvalidInputException("New message text cannot be blank");
        }
        if (newMessageText.length() > 255) {
            throw new InvalidInputException("New message text must be under 255 characters");
        }
        return messageRepository.findById(id)
                .map(message -> {
                    message.setMessageText(newMessageText);
                    messageRepository.save(message);
                    return 1;
                })
                .orElseThrow(() -> new InvalidInputException("Message not found"));
    }

    public List<Message> getMessagesByAccount(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}