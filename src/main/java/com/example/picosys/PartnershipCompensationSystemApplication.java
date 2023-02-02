package com.example.picosys;

import com.example.picosys.entity.CalculationMethodology;
import com.example.picosys.entity.CompensationPlan;
import com.example.picosys.entity.Role;
import com.example.picosys.entity.User;
import com.example.picosys.repository.UserRepository;
import com.example.picosys.service.CompensationPlanService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@SecurityScheme(
        name = "jwt_token_security",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER)
public class PartnershipCompensationSystemApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompensationPlanService planService;

    public static void main(String[] args) {
        SpringApplication.run(PartnershipCompensationSystemApplication.class, args);
    }

    private List<User> populateUsers() {
        return List.of(
                //ADMIN
                User.builder().firstName("Shounak").lastName("Bhalerao").department("Management").jobTitle("CEO").location("Aurangabad, MH").emailId("shounakbhalerao777@gmail.com").password("qwerty").role(Role.ADMIN).build(),
                User.builder().firstName("Yash").lastName("Pawar").department("Finance").jobTitle("Project Master").location("Pune, MH").emailId("yash@gmail.com").password("qwerty").role(Role.ADMIN).build(),
                User.builder().firstName("Adwait").lastName("Gite").department("Training").jobTitle("Tech Evangalist").location("Nagpur, MH").emailId("adwait@gmail.com").password("qwerty").role(Role.ADMIN).build(),
                User.builder().firstName("Pavan").lastName("Raut").department("Sales").jobTitle("Sales Lead Manager").location("Nashik, MH").emailId("pavan@gmail.com").password("qwerty").role(Role.ADMIN).build(),
                User.builder().firstName("Ashish").lastName("Verma").department("Marketing").jobTitle("Marketing Communications Specialist").location("Mumbai, MH").emailId("ashish@gmail.com").password("qwerty").role(Role.ADMIN).build(),
                //COMPENSATION_PLAN_USER
                User.builder().firstName("Akshay").lastName("Goel").department("Development").jobTitle("Tech Lead").location("Chalisgaon, MH").emailId("akshay@gmail.com").password("qwerty").role(Role.COMPENSATION_PLAN_USER).build(),
                User.builder().firstName("Trishank").lastName("Mahur").department("Development").jobTitle("Tech Lead").location("Jelgaon, MH").emailId("trishank@gmail.com").password("qwerty").role(Role.COMPENSATION_PLAN_USER).build(),
                User.builder().firstName("Ayush").lastName("Kumar").department("Finance").jobTitle("IT Finance Lead").location("Satara, MH").emailId("ayush@gmail.com").password("qwerty").role(Role.COMPENSATION_PLAN_USER).build(),
                User.builder().firstName("Harsh").lastName("Kanzaria").department("Training").jobTitle("Lead Teacher").location("Latur, MH").emailId("harsh@gmail.com").password("qwerty").role(Role.COMPENSATION_PLAN_USER).build(),
                User.builder().firstName("Harshvardhan").lastName("Patil").department("Development").jobTitle("Tech Architect").location("Hingoli, MH").emailId("harshvardhan@gmail.com").password("qwerty").role(Role.COMPENSATION_PLAN_USER).build(),
                //USER
                User.builder().firstName("Sanket").lastName("Narkhede").department("Development").jobTitle("DevOps Master").location("Parbhani, MH").emailId("sanket@gmail.com").password("qwerty").role(Role.USER).build(),
                User.builder().firstName("Jobin").lastName("Vargheese").department("CICD").jobTitle("Cloud Specialist").location("Wardha, MH").emailId("jobin@gmail.com").password("qwerty").role(Role.USER).build(),
                User.builder().firstName("Dhiraj").lastName("Chavhan").department("Marketing").jobTitle("Marketing Lead").location("Dhule, MH").emailId("dhiraj@gmail.com").password("qwerty").role(Role.USER).build(),
                User.builder().firstName("Prateek").lastName("Kurvalkar").department("Gaming").jobTitle("Youtube Coordinator").location("Amrawati, MH").emailId("prateek@gmail.com").password("qwerty").role(Role.USER).build(),
                User.builder().firstName("Taukeer").lastName("Khan").department("SAP").jobTitle("SAP LEAD").location("Beed, MH").emailId("taukeer@gmail.com").password("qwerty").role(Role.USER).build()
        );
    }

    private void generateCompensationPlans(){
        planService.createCompensationPlan(1L, CompensationPlan.builder().partnerName("Amul Co").compensationPlanName("Marketing#121299").percentageCompensation(Byte.valueOf("50")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(12L).maxQuantity(14L).validFrom(LocalDate.now().plusDays(2)).validTo(LocalDate.now().plusDays(43)).build());
        planService.createCompensationPlan(2L, CompensationPlan.builder().partnerName("Tata Manifacturing").compensationPlanName("Manifacturing#211221").percentageCompensation(Byte.valueOf("24")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(45L).maxQuantity(92L).validFrom(LocalDate.now().plusDays(12)).validTo(LocalDate.now().plusDays(43)).build());
        planService.createCompensationPlan(3L, CompensationPlan.builder().partnerName("Boat Headphones Ltd").compensationPlanName("Microchip#12071").percentageCompensation(Byte.valueOf("12")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(10L).maxQuantity(20L).validFrom(LocalDate.now().plusDays(32)).validTo(LocalDate.now().plusDays(43)).build());
        planService.createCompensationPlan(4L, CompensationPlan.builder().partnerName("Valve Co.").compensationPlanName("Dota2#231227").percentageCompensation(Byte.valueOf("68")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(100L).maxQuantity(150L).validFrom(LocalDate.now().plusDays(7)).validTo(LocalDate.now().plusDays(43)).build());
        planService.createCompensationPlan(5L, CompensationPlan.builder().partnerName("Samsung Ltd.").compensationPlanName("Communication#21664").percentageCompensation(Byte.valueOf("6")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(120L).maxQuantity(240L).validFrom(LocalDate.now().plusDays(23)).validTo(LocalDate.now().plusDays(43)).build());
    }
    @Override
    public void run(String... args) throws Exception {
        userRepository.saveAll(populateUsers());
        generateCompensationPlans();
    }
}
