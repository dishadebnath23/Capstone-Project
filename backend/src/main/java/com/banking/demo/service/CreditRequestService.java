package com.banking.demo.service;

import com.banking.demo.dto.CreditRequestCreateRequest;
import com.banking.demo.model.CreditRequest;
import com.banking.demo.model.CreditRequestStatus;
import com.banking.demo.repository.CreditRequestRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.banking.demo.exception.ResourceNotFoundException;
import com.banking.demo.exception.BadRequestException;

@Service
public class CreditRequestService {

    private final CreditRequestRepository creditRequestRepo;

    public CreditRequestService(CreditRequestRepository creditRequestRepo) {
        this.creditRequestRepo = creditRequestRepo;
    }

    // Analyst: view all requests
    public List<CreditRequest> getAllRequestsForAnalyst() {
        return creditRequestRepo.findAll();
    }




    // RM creates request
    public CreditRequest create(CreditRequestCreateRequest request) {

        String rmUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        CreditRequest cr = new CreditRequest();
        cr.setClientId(request.getClientId());
        cr.setRmUsername(rmUsername);
        cr.setRequestedAmount(request.getRequestedAmount());
        cr.setTenureMonths(request.getTenureMonths());
        cr.setPurpose(request.getPurpose());

        cr.setStatus(CreditRequestStatus.PENDING);
        cr.setCreatedAt(LocalDateTime.now());

        return creditRequestRepo.save(cr);
    }

    public List<CreditRequest> getMyRequests() {
        String rmUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return creditRequestRepo.findByRmUsername(rmUsername);
    }

    public List<CreditRequest> getPendingRequests() {
        return creditRequestRepo.findByStatus(CreditRequestStatus.PENDING);
    }

    public CreditRequest reviewRequest(
            String requestId,
            CreditRequestStatus status,
            String remark
    ) {

        // ✅ Validate status
        if (status != CreditRequestStatus.APPROVED &&
                status != CreditRequestStatus.REJECTED) {
            throw new BadRequestException("Invalid review status");
        }

        // ✅ Validate analyst remark
        if (remark == null || remark.trim().isEmpty()) {
            throw new BadRequestException("Analyst remark is required");
        }

        String analystUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        CreditRequest cr = creditRequestRepo.findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Credit request not found")
                );

        //  IMPORTANT BUSINESS RULE (missing earlier)
        if (cr.getStatus() != CreditRequestStatus.PENDING) {
            throw new BadRequestException(
                    "Only pending credit requests can be reviewed"
            );
        }

        cr.setStatus(status);
        cr.setReviewedAt(LocalDateTime.now());
        cr.setAnalystUsername(analystUsername);
        cr.setAnalystRemark(remark.trim());

        return creditRequestRepo.save(cr);
    }

}
