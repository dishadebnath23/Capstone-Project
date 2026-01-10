package com.banking.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clients")
public class Client {

    @Id
    private String id;

    private String companyName;
    private String industry;
    private String address;

    private PrimaryContact primaryContact;

    private double annualTurnover;
    private boolean documentsSubmitted;

    // 🔐 RM who owns this client (JWT subject)
    private String rmUsername;

    // ---------------- GETTERS & SETTERS ----------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PrimaryContact getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(PrimaryContact primaryContact) {
        this.primaryContact = primaryContact;
    }

    public double getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(double annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public boolean isDocumentsSubmitted() {
        return documentsSubmitted;
    }

    public void setDocumentsSubmitted(boolean documentsSubmitted) {
        this.documentsSubmitted = documentsSubmitted;
    }

    public String getRmUsername() {
        return rmUsername;
    }

    public void setRmUsername(String rmUsername) {
        this.rmUsername = rmUsername;
    }
}
