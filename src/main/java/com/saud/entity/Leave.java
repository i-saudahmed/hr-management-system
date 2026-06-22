package com.saud.entity;

import com.saud.enums.LeaveStatus;
import com.saud.enums.LeaveType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "leave_request")
public class Leave {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id ;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee ; // who applied

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type")
    private LeaveType leaveType ;

    @Column(name =  "start_date", nullable = false)
    private LocalDate startDate ;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate ;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays ; // end date - start date + 1

    private String reason ; // emp reason

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeaveStatus  status = LeaveStatus.PENDING_MANAGER_APPROVAL ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_approved_by")
    private Employee managerApprovedBy ;

    @Column(name = "manager_approved_at")
    private LocalDateTime managerApprovedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_approved_by")
    private Employee hrApprovedBy;        // who gave final HR approval

    @Column(name = "hr_approved_at")
    private LocalDateTime hrApprovedAt;

    @Column(name = "rejection_reason")
    private String rejectionReason;       // filled only if rejected

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
