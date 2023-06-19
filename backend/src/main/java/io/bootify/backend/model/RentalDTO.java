package io.bootify.backend.model;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public class RentalDTO {

    private Long id;

    @NotNull
    private Long renterId;

    @NotNull
    private Long offerId;

    @NotNull
    private OffsetDateTime startDate;

    private OffsetDateTime endDate;
    
    private boolean isFinished = false;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getRenterId() {
        return renterId;
    }

    public void setRenterId(final Long renterId) {
        this.renterId = renterId;
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

    public boolean getIsFinished() {
    return this.isFinished;
    }

    public void setIsFinished(boolean isFinished) {
    this.isFinished = isFinished;
    }

}
