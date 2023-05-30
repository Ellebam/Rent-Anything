package io.bootify.backend.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;


public class MessageDTO {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long offerId;

    @NotNull
    private String content;

    @NotNull
    private LocalDateTime timestamp;

    private Long sender;

    private Long recipient;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(final Long offerId) {
        this.offerId = offerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(final Long sender) {
        this.sender = sender;
    }

    public Long getRecipient() {
        return recipient;
    }

    public void setRecipient(final Long recipient) {
        this.recipient = recipient;
    }

}
