package io.bootify.backend.model;

import jakarta.validation.constraints.NotNull;

public class RentalApplicationDTO {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long offerId;

    @NotNull
    private Boolean isApproved;

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

}
