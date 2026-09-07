package com.drivinglicense.entity;


import com.drivinglicense.enums.RequestStatus;
import com.drivinglicense.enums.ServiceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Builder.Default
    @Column(nullable = false,updatable = false)
    private LocalDate creationDate = LocalDate.now();

    private LocalDate cancellationDate;

    @NotNull
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus = RequestStatus.NEW;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @NotNull(message = "request should be related to an existing person")
    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id", nullable = false,updatable = false)
    private  Person person;

    @ManyToOne
    @JoinColumn(name = "category_id", updatable = false)
    private LicenseCategory licenseCategory;

    @ManyToOne
    @JoinColumn(name = "cancelled_by_user_id")
    private User cancelledBy;

    @ManyToOne
    @JoinColumn(name = "original_request_id", updatable = false)
    private Request originalRequest;
}
