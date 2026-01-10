package com.banking.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class CreditRequestReviewRequest {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NotBlank(message = "Status is required")
    private String status; // APPROVED / REJECTED

    private String remark;


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
// getters and setters
}
