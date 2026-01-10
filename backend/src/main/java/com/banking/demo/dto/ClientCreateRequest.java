package com.banking.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class ClientCreateRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @NotBlank(message = "Industry is required")
    private String industry;

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @NotBlank(message = "Address is required")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotBlank(message = "Primary contact name is required")
    private String primaryContactName;

    public String getPrimaryContactName() {
        return primaryContactName;
    }

    public void setPrimaryContactName(String primaryContactName) {
        this.primaryContactName = primaryContactName;
    }

    @Email(message = "Invalid email format")
    @NotBlank(message = "Primary contact email is required")
    private String primaryContactEmail;

    public String getPrimaryContactEmail() {
        return primaryContactEmail;
    }

    public void setPrimaryContactEmail(String primaryContactEmail) {
        this.primaryContactEmail = primaryContactEmail;
    }

    @NotBlank(message = "Primary contact phone is required")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Invalid phone number"
    )
    private String primaryContactPhone;

    public String getPrimaryContactPhone() {
        return primaryContactPhone;
    }

    public void setPrimaryContactPhone(String primaryContactPhone) {
        this.primaryContactPhone = primaryContactPhone;
    }

    @Positive(message = "Annual turnover must be positive")
    private double annualTurnover;

    public double getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(double annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    private boolean documentsSubmitted;

    public boolean isDocumentsSubmitted() {
        return documentsSubmitted;
    }

    public void setDocumentsSubmitted(boolean documentsSubmitted) {
        this.documentsSubmitted = documentsSubmitted;
    }
}
