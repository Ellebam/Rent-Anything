package io.bootify.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;


public class RentalApplicationDTO {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long offerId;

    @NotNull
    @JsonProperty("isApproved")
    private Boolean isApproved;

    private Long user;

    private Long offer;

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

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(final Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(final Long user) {
        this.user = user;
    }

    public Long getOffer() {
        return offer;
    }

    public void setOffer(final Long offer) {
        this.offer = offer;
    }

}
