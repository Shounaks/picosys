package com.example.picosys.service;

import com.example.picosys.entity.CompensationPlan;
import com.example.picosys.entity.Role;
import com.example.picosys.entity.User;
import com.example.picosys.exceptions.PartnerCompServiceException;
import com.example.picosys.repository.CompensationPlanRepository;
import com.example.picosys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class CompensationPlanService {
    private final UserRepository userRepository;
    private final CompensationPlanRepository compensationPlanRepository;

    public Long createCompensationPlan(Long userId, CompensationPlan compensationPlan) {
        validateCompensationPlanData(userId, compensationPlan);
        User user = userRepository.findById(userId).get();
        CompensationPlan savedPlan = compensationPlanRepository.save(compensationPlan);
        user.setCompensationPlanId(savedPlan.getPlanId());
        return savedPlan.getPlanId();
    }

    public CompensationPlan retrieveCompensationPlanForUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PartnerCompServiceException("Compensation Retrieve Error: userId is Invalid/Doesn't Exist"));
        return compensationPlanRepository.findById(user.getCompensationPlanId())
                .orElseThrow(() -> new PartnerCompServiceException("Compensation Retrieve Error: Compensation Plan Doesn't Exist for this UserId"));
    }

    private void validateCompensationPlanData(Long userId, CompensationPlan compensationPlan) {
        //Check if userID is a compensation Plan USER:
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PartnerCompServiceException("Compensation Error: userId is Invalid/Doesn't Exist"));
        if (user.getCompensationPlanId() != null){
            throw new PartnerCompServiceException("Compensation Error: User Already Has A Compensation Plan");
        }
        if (!user.getRole().equals(Role.COMPENSATION_PLAN_USER) && !user.getRole().equals(Role.ADMIN)) {
            throw new PartnerCompServiceException("Compensation Error: User Does Not Have Permission To Create Compensation Plans");
        }

        //Check if Dates Are valid
        LocalDate validTo = compensationPlan.getValidTo();
        LocalDate validFrom = compensationPlan.getValidFrom();
        if (validTo.isBefore(validFrom)) throw new PartnerCompServiceException("Compensation Error: Invalid Date Selected");
    }

    public Page<CompensationPlan> getAllCompensationPlans(int page) {
        return compensationPlanRepository.findAll(PageRequest.of(page,10));
    }
}
