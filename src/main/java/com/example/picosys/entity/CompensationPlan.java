package com.example.picosys.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompensationPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;
    private String compensationPlanName;
    private Long employeeId;
    private String calculationMethodology;
    private LocalDate validFrom;
    private LocalDate validTo;
}
