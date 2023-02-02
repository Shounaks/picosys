package com.example.picosys.controller;

import com.example.picosys.entity.CompensationPlan;
import com.example.picosys.entity.User;
import com.example.picosys.service.CompensationPlanService;
import com.example.picosys.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/picosys/admin/")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt_token_security")
@CrossOrigin("*")
public class AdminController {
    private final UserService userService;
    private final CompensationPlanService planService;

    @GetMapping("user-list")
    public ResponseEntity<Page<User>> retrieveAllUsers(@RequestParam int page) {
        return ResponseEntity.ok(userService.retrieveAllUsers(page));
    }

    @GetMapping("delete-compensation-plan/{compensationPlanId}")
    public ResponseEntity<Boolean> deleteCompensationPlan(@PathVariable Long compensationPlanId){
        boolean isDeleted = planService.deleteCompensationPlan(compensationPlanId);
        return ResponseEntity.ok(isDeleted);
    }

    @GetMapping("compensation-report")
    public ResponseEntity<Page<CompensationPlan>> retrieveAllCompensation(@RequestParam int page) {
        return ResponseEntity.ok(planService.getAllCompensationPlans(page));
    }

    @GetMapping("generate-compensation-report")
    public void generateReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=compensationPlanReport.xls";
        response.setHeader(headerKey,headerValue);
        planService.generateCompensationPlanReport(response);
    }
}
