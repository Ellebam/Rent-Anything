package io.bootify.backend.model;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public class RentalDTO {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long offerId;

    @NotNull
    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

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

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(final OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(final OffsetDateTime endDate) {
        this.endDate = endDate;
    }

}
