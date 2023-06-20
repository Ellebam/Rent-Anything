package io.bootify.backend.model;

import jakarta.validation.constraints.NotNull;

public class RentalApplicationDTO {

    private Long id;


    @NotNull
    private Long offerId;

    @NotNull
    private Boolean isApproved = false;

    private Long applicantId;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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
    
    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(final Long applicantId) {
        this.applicantId = applicantId;
    }
}
