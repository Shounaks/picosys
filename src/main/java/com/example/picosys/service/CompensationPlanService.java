package com.example.picosys.service;

import com.example.picosys.entity.CompensationPlan;
import com.example.picosys.entity.Role;
import com.example.picosys.entity.User;
import com.example.picosys.exceptions.PartnerCompServiceException;
import com.example.picosys.repository.CompensationPlanRepository;
import com.example.picosys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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

    public boolean deleteCompensationPlan(Long compensationPlanId) {
        //Validation
        compensationPlanRepository.findById(compensationPlanId).orElseThrow(() -> new PartnerCompServiceException("Compensation Delete Error: Plan ID is Invalid"));
        User user = userRepository.findByCompensationPlanId(compensationPlanId).orElseThrow(() -> new PartnerCompServiceException("**CRITICAL ERROR** Compensation Delete Error: No User is associated with Compensation Plan"));
        //DELETION
        user.setCompensationPlanId(null);
        compensationPlanRepository.deleteById(compensationPlanId);
        return true;
    }

    public CompensationPlan retrieveCompensationPlanForUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PartnerCompServiceException("Compensation Retrieve Error: userId is Invalid/Doesn't Exist"));
        return compensationPlanRepository.findById(user.getCompensationPlanId())
                .orElseThrow(() -> new PartnerCompServiceException("Compensation Retrieve Error: Compensation Plan Doesn't Exist for this UserId"));
    }

    private void validateCompensationPlanData(Long userId, CompensationPlan compensationPlan) {
        //Check if userID is a compensation Plan USER:
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PartnerCompServiceException("Compensation Error: userId is Invalid/Doesn't Exist"));
        if (user.getCompensationPlanId() != null) {
            throw new PartnerCompServiceException("Compensation Error: User Already Has A Compensation Plan");
        }
        if (!user.getRole().equals(Role.COMPENSATION_PLAN_USER) && !user.getRole().equals(Role.ADMIN)) {
            throw new PartnerCompServiceException("Compensation Error: User Does Not Have Permission To Create Compensation Plans");
        }

        //Check if Dates Are valid
        LocalDate validTo = compensationPlan.getValidTo();
        LocalDate validFrom = compensationPlan.getValidFrom();
        if (validTo.isBefore(validFrom))
            throw new PartnerCompServiceException("Compensation Error: Invalid Date Selected");
    }

    public Page<CompensationPlan> getAllCompensationPlans(int page) {
        return compensationPlanRepository.findAll(PageRequest.of(page, 10));
    }

    public void generateCompensationPlanReport(HttpServletResponse response) throws IOException {
        List<CompensationPlan> plans = compensationPlanRepository.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet compensationPlanReportSheet = workbook.createSheet("Compensation Plan Report");
        HSSFRow row = compensationPlanReportSheet.createRow(0);
        row.createCell(0).setCellValue("planId");
        row.createCell(1).setCellValue("partnerName");
        row.createCell(2).setCellValue("compensationPlanName");
        row.createCell(3).setCellValue("calculationMethodology");
        row.createCell(4).setCellValue("minQuantity");
        row.createCell(5).setCellValue("maxQuantity");
        row.createCell(6).setCellValue("percentageCompensation");
        row.createCell(7).setCellValue("validFrom");
        row.createCell(8).setCellValue("validTo");

        int dataRowIndex = 1;
        for (CompensationPlan c : plans) {
            HSSFRow dataRow = compensationPlanReportSheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(c.getPlanId());
            dataRow.createCell(1).setCellValue(c.getPartnerName());
            dataRow.createCell(2).setCellValue(c.getCompensationPlanName());
            dataRow.createCell(3).setCellValue(c.getCalculationMethodology().name());
            dataRow.createCell(4).setCellValue(c.getMinQuantity());
            dataRow.createCell(5).setCellValue(c.getMaxQuantity());
            dataRow.createCell(6).setCellValue(c.getPercentageCompensation());
            dataRow.createCell(7).setCellValue(c.getValidFrom().toString());
            dataRow.createCell(8).setCellValue(c.getValidTo().toString());
            dataRowIndex++;
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
