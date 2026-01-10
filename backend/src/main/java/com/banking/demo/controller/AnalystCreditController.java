package com.banking.demo.controller;
import com.banking.demo.model.CreditRequestStatus;

import com.banking.demo.dto.CreditRequestReviewRequest;
import com.banking.demo.model.CreditRequest;
import com.banking.demo.service.CreditRequestService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analyst/credit-requests")
@PreAuthorize("hasRole('ANALYST')")
public class AnalystCreditController {

    private final CreditRequestService creditService;

    public AnalystCreditController(CreditRequestService creditService) {
        this.creditService = creditService;
    }


    @PostMapping("/{id}/review")
    public CreditRequest review(
            @PathVariable String id,
            @Valid @RequestBody CreditRequestReviewRequest request
    ) {
        CreditRequestStatus status =
                CreditRequestStatus.valueOf(request.getStatus());

        return creditService.reviewRequest(
                id,
                status,
                request.getRemark()
        );
    }

}
