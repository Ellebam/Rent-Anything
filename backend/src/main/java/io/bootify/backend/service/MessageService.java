package io.bootify.backend.service;

import io.bootify.backend.domain.Message;
import io.bootify.backend.domain.User;
import io.bootify.backend.model.MessageDTO;
import io.bootify.backend.repos.MessageRepository;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(final MessageRepository messageRepository,
            final UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<MessageDTO> findAll() {
        final List<Message> messages = messageRepository.findAll(Sort.by("id"));
        return messages.stream()
                .map((message) -> mapToDTO(message, new MessageDTO()))
                .toList();
    }

    public MessageDTO get(final Long id) {
        return messageRepository.findById(id)
                .map((message) -> mapToDTO(message, new MessageDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MessageDTO messageDTO) {
        final Message message = new Message();
        mapToEntity(messageDTO, message);
        return messageRepository.save(message).getId();
    }

    public void update(final Long id, final MessageDTO messageDTO) {
        final Message message = messageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(messageDTO, message);
        messageRepository.save(message);
    }

    public void delete(final Long id) {
        messageRepository.deleteById(id);
    }

    private MessageDTO mapToDTO(final Message message, final MessageDTO messageDTO) {
        messageDTO.setId(message.getId());
        messageDTO.setUserId(message.getUserId());
        messageDTO.setOfferId(message.getOfferId());
        messageDTO.setContent(message.getContent());
        messageDTO.setTimestamp(message.getTimestamp());
        messageDTO.setSender(message.getSender() == null ? null : message.getSender().getId());
        messageDTO.setRecipient(message.getRecipient() == null ? null : message.getRecipient().getId());
        return messageDTO;
    }

    private Message mapToEntity(final MessageDTO messageDTO, final Message message) {
        message.setUserId(messageDTO.getUserId());
        message.setOfferId(messageDTO.getOfferId());
        message.setContent(messageDTO.getContent());
        message.setTimestamp(messageDTO.getTimestamp());
        final User sender = messageDTO.getSender() == null ? null : userRepository.findById(messageDTO.getSender())
                .orElseThrow(() -> new NotFoundException("sender not found"));
        message.setSender(sender);
        final User recipient = messageDTO.getRecipient() == null ? null : userRepository.findById(messageDTO.getRecipient())
                .orElseThrow(() -> new NotFoundException("recipient not found"));
        message.setRecipient(recipient);
        return message;
    }

}
