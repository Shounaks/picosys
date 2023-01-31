package com.example.picosys.controller;

import com.example.picosys.entity.CompensationPlan;
import com.example.picosys.service.CompensationPlanService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/picosys/planner/")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt_token_security")
@CrossOrigin("*")
public class CompensationPlanController {
    private final CompensationPlanService compensationPlanService;

    @PostMapping("{userId}/create")
    public ResponseEntity<Long> createCompensationPlan(@PathVariable Long userId, @Valid @RequestBody CompensationPlan compensationPlan) {
        return ResponseEntity.ok(compensationPlanService.createCompensationPlan(userId, compensationPlan));
    }
}
