package io.bootify.backend.service;

import io.bootify.backend.domain.Message;
import io.bootify.backend.domain.User;
import io.bootify.backend.domain.Offer;
import io.bootify.backend.repos.OfferRepository;
import io.bootify.backend.model.MessageDTO;
import io.bootify.backend.repos.MessageRepository;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.util.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository, OfferRepository offerRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
    }

    public List<MessageDTO> findAll() {
        List<Message> messages = messageRepository.findAll(Sort.by("id"));
        return messages.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MessageDTO get(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return mapToDTO(message);
    }

    public Long create(MessageDTO messageDTO) {
        Message message = mapToEntity(messageDTO);
        return messageRepository.save(message).getId();
    }

    public void update(Long id, MessageDTO messageDTO) {
        Message message = messageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(messageDTO, message);
        messageRepository.save(message);
    }

    public void delete(Long id) {
        messageRepository.deleteById(id);
    }

    private MessageDTO mapToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setSenderId(message.getSender() == null ? null : message.getSender().getId());
        messageDTO.setRecipientId(message.getRecipient() == null ? null : message.getRecipient().getId());
        messageDTO.setOfferId(message.getOffer() == null ? null : message.getOffer().getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setTimestamp(message.getTimestamp());
        return messageDTO;
    }

    private Message mapToEntity(MessageDTO messageDTO) {
        Message message = new Message();
        mapToEntity(messageDTO, message);
        return message;
    }

    private void mapToEntity(MessageDTO messageDTO, Message message) {
        message.setContent(messageDTO.getContent());
        message.setTimestamp(messageDTO.getTimestamp());
        User sender = messageDTO.getSenderId() == null ? null : userRepository.findById(messageDTO.getSenderId())
                .orElseThrow(() -> new NotFoundException("Sender not found"));
        message.setSender(sender);
        User recipient = messageDTO.getRecipientId() == null ? null : userRepository.findById(messageDTO.getRecipientId())
                .orElseThrow(() -> new NotFoundException("Recipient not found"));
        message.setRecipient(recipient);
        Offer offer = messageDTO.getOfferId() == null ? null : offerRepository.findById(messageDTO.getOfferId())
                .orElseThrow(() -> new NotFoundException("Offer not found"));
        message.setOffer(offer);
    }
}