package io.bootify.backend.model;

import jakarta.validation.constraints.NotNull;

public class RentalApplicationDTO {

    private Long id;

    @NotNull
    private Long applicantId;

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

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(final Long applicantId) {
        this.applicantId = applicantId;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(final Long offerId) {
        this.offerId = offerId;
    }

    public Boolean isApproved() {
        return isApproved;
    }

    public void setApproved(final Boolean isApproved) {
        this.isApproved = isApproved;
    }

}
