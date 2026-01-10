package com.banking.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "credit_requests")
public class CreditRequest {

    @Id
    private String id;

    private String clientId;
    private String rmUsername;

    private double requestedAmount;
    private int tenureMonths;
    private String purpose;

    private CreditRequestStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
    private String analystUsername;
    private String analystRemark;

    // getters & setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getRmUsername() { return rmUsername; }
    public void setRmUsername(String rmUsername) { this.rmUsername = rmUsername; }

    public double getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(double requestedAmount) { this.requestedAmount = requestedAmount; }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public CreditRequestStatus getStatus() { return status; }
    public void setStatus(CreditRequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getAnalystUsername() { return analystUsername; }
    public void setAnalystUsername(String analystUsername) { this.analystUsername = analystUsername; }

    public String getAnalystRemark() { return analystRemark; }
    public void setAnalystRemark(String analystRemark) { this.analystRemark = analystRemark; }
}
