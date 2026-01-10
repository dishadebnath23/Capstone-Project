package com.banking.demo.controller;

import com.banking.demo.dto.CreditRequestCreateRequest;
import com.banking.demo.model.CreditRequest;
import com.banking.demo.model.CreditRequestStatus;
import com.banking.demo.service.CreditRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credit-requests")
public class CreditRequestController {

    private final CreditRequestService creditRequestService;

    public CreditRequestController(CreditRequestService creditRequestService) {
        this.creditRequestService = creditRequestService;
    }

    // ================= ANALYST =================

    @GetMapping("/all")
    @PreAuthorize("hasRole('ANALYST')")
    public List<CreditRequest> getAllRequests() {
        return creditRequestService.getAllRequestsForAnalyst();
    }


    // ================= RM =================

    @PostMapping
    @PreAuthorize("hasRole('RM')")
    public CreditRequest create(
            @RequestBody CreditRequestCreateRequest request
    ) {
        return creditRequestService.create(request);
    }

    @GetMapping("/rm")
    @PreAuthorize("hasRole('RM')")
    public List<CreditRequest> getMyRequests() {
        return creditRequestService.getMyRequests();
    }

    // ================= ANALYST =================

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ANALYST')")
    public List<CreditRequest> getPendingRequests() {
        return creditRequestService.getPendingRequests();
    }

    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('ANALYST')")
    public CreditRequest reviewRequest(
            @PathVariable String id,
            @RequestParam CreditRequestStatus status,
            @RequestParam(required = false) String remark
    ) {
        return creditRequestService.reviewRequest(id, status, remark);
    }

}
