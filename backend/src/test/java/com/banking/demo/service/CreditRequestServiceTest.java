package com.banking.demo.service;

import com.banking.demo.dto.CreditRequestCreateRequest;
import com.banking.demo.exception.BadRequestException;
import com.banking.demo.exception.ResourceNotFoundException;
import com.banking.demo.model.CreditRequest;
import com.banking.demo.model.CreditRequestStatus;
import com.banking.demo.repository.CreditRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CreditRequestServiceTest {

    @Mock
    private CreditRequestRepository creditRequestRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CreditRequestService creditRequestService;

    private void mockLoggedInUser(String username) {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
    }

    @Test
    void shouldRejectPendingRequestSuccessfully() {

        mockLoggedInUser("analyst_user");

        CreditRequest cr = new CreditRequest();
        cr.setStatus(CreditRequestStatus.PENDING);

        when(creditRequestRepository.findById("req999"))
                .thenReturn(Optional.of(cr));

        when(creditRequestRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreditRequest reviewed = creditRequestService.reviewRequest(
                "req999",
                CreditRequestStatus.REJECTED,
                "Insufficient documents"
        );

        assertEquals(CreditRequestStatus.REJECTED, reviewed.getStatus());
        assertEquals("analyst_user", reviewed.getAnalystUsername());
        assertNotNull(reviewed.getReviewedAt());
    }

    @Test
    void shouldFailWhenRequestAlreadyApproved() {

        mockLoggedInUser("analyst_user");

        CreditRequest cr = new CreditRequest();
        cr.setStatus(CreditRequestStatus.APPROVED);

        when(creditRequestRepository.findById("req123"))
                .thenReturn(Optional.of(cr));

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> creditRequestService.reviewRequest(
                        "req123",
                        CreditRequestStatus.REJECTED,
                        "Not allowed"
                )
        );

        assertEquals(
                "Only pending credit requests can be reviewed",
                ex.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenReviewRequestNotFound() {

        // ✅ REQUIRED
        mockLoggedInUser("analyst_user");

        when(creditRequestRepository.findById("req123"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                creditRequestService.reviewRequest(
                        "req123",
                        CreditRequestStatus.APPROVED,
                        "ok"
                )
        );
    }


    @Test
    void shouldFailWhenReviewStatusIsPending() {

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> creditRequestService.reviewRequest(
                        "req777",
                        CreditRequestStatus.PENDING,
                        "Invalid status"
                )
        );

        assertEquals("Invalid review status", ex.getMessage());
    }



    @Test
    void shouldFailWhenAnalystRemarkIsBlank() {

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> creditRequestService.reviewRequest(
                        "req888",
                        CreditRequestStatus.APPROVED,
                        "   "
                )
        );

        assertEquals("Analyst remark is required", ex.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenNoAuthenticationPresent() {

        // clear security context
        SecurityContextHolder.clearContext();

        assertThrows(NullPointerException.class, () -> {
            creditRequestService.getMyRequests();
        });

    }





}





