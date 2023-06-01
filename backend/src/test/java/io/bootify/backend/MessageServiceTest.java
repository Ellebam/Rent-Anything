package io.bootify.backend;

import io.bootify.backend.domain.Message;
import io.bootify.backend.domain.User;
import io.bootify.backend.domain.Offer;
import io.bootify.backend.model.MessageDTO;
import io.bootify.backend.repos.MessageRepository;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.repos.OfferRepository;
import io.bootify.backend.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OfferRepository offerRepository;
    private MessageService messageService;
    private Message message;
    private MessageDTO messageDTO;
    private User user;
    private Offer offer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messageService = new MessageService(messageRepository, userRepository, offerRepository);
        user = new User();
        user.setId(1L);
        offer = new Offer();
        offer.setId(1L);
        message = new Message();
        message.setId(1L);
        message.setSender(user);
        message.setRecipient(user);
        message.setOffer(offer);
        message.setContent("Test content");
        message.setTimestamp(OffsetDateTime.now());
        messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setSenderId(1L);
        messageDTO.setRecipientId(1L);
        messageDTO.setOfferId(1L);
        messageDTO.setContent("Test content");
        messageDTO.setTimestamp(OffsetDateTime.now());
    
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(offerRepository.findById(anyLong())).willReturn(Optional.of(offer));
        
        // Mock the save method to mimic the behavior of a real repository
        given(messageRepository.save(any(Message.class))).willAnswer(invocation -> {
            Message savedMessage = invocation.getArgument(0, Message.class);
            // Mimic JPA's generated ID behavior
            savedMessage.setId(1L);
            return savedMessage;
        });
    }
    

    @Test
    void shouldCreateMessage() {
        // Given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(offerRepository.findById(anyLong())).willReturn(Optional.of(offer));

        // When
        Long createdMessageId = messageService.create(messageDTO);

        // Then
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(messageArgumentCaptor.capture());
        Message capturedMessage = messageArgumentCaptor.getValue();
        assertThat(capturedMessage.getId()).isEqualTo(messageDTO.getId());
        assertThat(capturedMessage.getContent()).isEqualTo(messageDTO.getContent());
        assertThat(capturedMessage.getTimestamp()).isEqualTo(messageDTO.getTimestamp());
        assertThat(capturedMessage.getSender().getId()).isEqualTo(messageDTO.getSenderId());
        assertThat(capturedMessage.getRecipient().getId()).isEqualTo(messageDTO.getRecipientId());
        assertThat(capturedMessage.getOffer().getId()).isEqualTo(messageDTO.getOfferId());
        assertThat(createdMessageId).isEqualTo(1L);
    }

    @Test
    void shouldUpdateMessage() {
        // Given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(offerRepository.findById(anyLong())).willReturn(Optional.of(offer));
        given(messageRepository.findById(any(Long.class))).willReturn(Optional.of(message));
        given(messageRepository.save(any(Message.class))).willReturn(message);

        // When
        messageService.update(1L, messageDTO);

        // Then
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(messageArgumentCaptor.capture());
        Message capturedMessage = messageArgumentCaptor.getValue();
        assertThat(capturedMessage.getContent()).isEqualTo(messageDTO.getContent());
        assertThat(capturedMessage.getTimestamp()).isEqualTo(messageDTO.getTimestamp());
        assertThat(capturedMessage.getSender().getId()).isEqualTo(messageDTO.getSenderId());
        assertThat(capturedMessage.getRecipient().getId()).isEqualTo(messageDTO.getRecipientId());
        assertThat(capturedMessage.getOffer().getId()).isEqualTo(messageDTO.getOfferId());
    }

    @Test
    void shouldDeleteMessage() {
        // Given
        given(messageRepository.findById(any(Long.class))).willReturn(Optional.of(message));

        // When
        messageService.delete(1L);

        // Then
        verify(messageRepository).deleteById(anyLong());
    }

    @Test
    void shouldFindMessageById() {
        // Given
        given(messageRepository.findById(any(Long.class))).willReturn(Optional.of(message));

        // When
        MessageDTO foundMessage = messageService.get(1L);

        // Then
        assertThat(foundMessage.getContent()).isEqualTo(message.getContent());
        assertThat(foundMessage.getTimestamp()).isEqualTo(message.getTimestamp());
        assertThat(foundMessage.getSenderId()).isEqualTo(message.getSender().getId());
        assertThat(foundMessage.getRecipientId()).isEqualTo(message.getRecipient().getId());
        assertThat(foundMessage.getOfferId()).isEqualTo(message.getOffer().getId());
    }
}
