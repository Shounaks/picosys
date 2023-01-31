package com.example.picosys.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
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
    @NotBlank(message = "compensation Plan Name must not be empty")
    private String compensationPlanName;
    @NotNull(message = "calculation Methodology must not be empty")
    private CalculationMethodology calculationMethodology;
    @NotNull(message = "Minimum Quantity must not be empty")
    @Positive
    private Long minQuantity;
    @NotNull(message = "Maximum Quantity must not be empty")
    @Positive
    private Long maxQuantity;
    @NotNull(message = "Volume must not be empty")
    @Min(value = 0, message = "Percentage Compensation cannot be below 0%")
    @Max(value = 100, message = "Percentage Compensation cannot be above 100%")
    private Byte percentageCompensation;
    @NotNull(message = "validFrom Id must not be empty")
    @Future(message = "validFrom Date Must Be a Future Date")
    private LocalDate validFrom;
    @NotNull(message = "validTo Id must not be empty")
    @Future(message = "validTo Date Must Be a Future Date")
    private LocalDate validTo;
}
