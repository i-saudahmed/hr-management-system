package com.saud.entity;

import com.saud.enums.LeaveType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "leave_balance")
public class LeaveBalance {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType;

    @Column(name = "year", nullable = false)
    private Integer year;             // e.g. 2026

    @Column(name = "total_allotted", nullable = false)
    private Integer totalAllotted;    // e.g. 20 annual days for the year

    @Column(name = "used", nullable = false)
    private Integer used = 0;         // how many days already taken

    public Integer getRemaining() {
        return totalAllotted - used;  // calculated, not stored
    }
}
