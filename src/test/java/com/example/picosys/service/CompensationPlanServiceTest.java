package com.example.picosys.service;

import com.example.picosys.entity.CalculationMethodology;
import com.example.picosys.entity.CompensationPlan;
import com.example.picosys.entity.Role;
import com.example.picosys.entity.User;
import com.example.picosys.exceptions.PartnerCompServiceException;
import com.example.picosys.repository.CompensationPlanRepository;
import com.example.picosys.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
class CompensationPlanServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CompensationPlanRepository planRepository;

    @InjectMocks
    CompensationPlanService planService;

    private static Stream<Arguments> validRolesToCreateCompensationPlan() {
        return Stream.of(Arguments.of(
                Role.COMPENSATION_PLAN_USER,
                Role.ADMIN
        ));
    }

    private static User generateUser(Role role) {
        return User.builder()
                .employeeId(1L)
                .password("qwerty")
                .emailId("test@gmail.com")
                .firstName("Tester One")
                .lastName("Tester Two")
                .role(role)
                .build();
    }

    @BeforeEach
    public final void setup() {
        planService = new CompensationPlanService(userRepository, planRepository);
    }

    @ParameterizedTest
    @MethodSource("validRolesToCreateCompensationPlan")
    void createPlan_ValidTest(Role role) {
        //Given
        CompensationPlan compensationPlan = CompensationPlan.builder().planId(1L).partnerName("Amul Co").compensationPlanName("Marketing#121299").percentageCompensation(Byte.valueOf("50")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(12L).maxQuantity(14L).validFrom(LocalDate.now().plusDays(2)).validTo(LocalDate.now().plusDays(43)).build();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(generateUser(role)));
        Mockito.when(planRepository.save(any())).thenReturn(compensationPlan);

        //When
        Long compensationPlan1 = planService.createCompensationPlan(1L, compensationPlan);

        //then
        assertThat(compensationPlan1)
                .isNotNull()
                .isEqualTo(1L);
    }

    @Test
    void createPlan_InvalidTest_Role() {
        //Given
        Role role = Role.USER;
        CompensationPlan compensationPlan = CompensationPlan.builder().planId(1L).partnerName("Amul Co").compensationPlanName("Marketing#121299").percentageCompensation(Byte.valueOf("50")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(12L).maxQuantity(14L).validFrom(LocalDate.now().plusDays(2)).validTo(LocalDate.now().plusDays(43)).build();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(generateUser(role)));
        Mockito.when(planRepository.save(any())).thenReturn(compensationPlan);

        //When
        String errorMsg = "Compensation Error: User Does Not Have Permission To Create Compensation Plans";
        assertThatThrownBy(() -> planService.createCompensationPlan(1L, compensationPlan))
                .isInstanceOf(PartnerCompServiceException.class)
                .hasMessageMatching(errorMsg);
    }

    @Test
    void createPlan_InvalidTest_Date() {
        //Given
        Role role = Role.ADMIN;
        CompensationPlan compensationPlan = CompensationPlan.builder().planId(1L).partnerName("Amul Co").compensationPlanName("Marketing#121299").percentageCompensation(Byte.valueOf("50")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(12L).maxQuantity(14L).validFrom(LocalDate.now().plusDays(2)).validTo(LocalDate.now().minusDays(43)).build();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(generateUser(role)));
        Mockito.when(planRepository.save(any())).thenReturn(compensationPlan);

        //When
        String errorMsg = "Compensation Error: Invalid Date Selected";
        assertThatThrownBy(() -> planService.createCompensationPlan(1L, compensationPlan))
                .isInstanceOf(PartnerCompServiceException.class)
                .hasMessageMatching(errorMsg);
    }

    @Test
    void createPlan_InvalidTest_UserId() {
        //Given
        CompensationPlan compensationPlan = CompensationPlan.builder().planId(1L).partnerName("Amul Co").compensationPlanName("Marketing#121299").percentageCompensation(Byte.valueOf("50")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(12L).maxQuantity(14L).validFrom(LocalDate.now().plusDays(2)).validTo(LocalDate.now().plusDays(43)).build();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Mockito.when(planRepository.save(any())).thenReturn(compensationPlan);

        //When
        String errorMsg = "Compensation Error: userId is Invalid/Doesn't Exist";
        assertThatThrownBy(() -> planService.createCompensationPlan(100L, compensationPlan))
                .isInstanceOf(PartnerCompServiceException.class)
                .hasMessageMatching(errorMsg);
    }

    @Test
    void createPlan_InvalidTest_Compensation_Already_Present() {
        //Given
        User user = User.builder()
                .employeeId(1L)
                .password("qwerty")
                .emailId("test@gmail.com")
                .firstName("Tester One")
                .lastName("Tester Two")
                .role(Role.COMPENSATION_PLAN_USER)
                .compensationPlanId(2L)
                .build();
        CompensationPlan compensationPlan = CompensationPlan.builder().planId(1L).partnerName("Amul Co").compensationPlanName("Marketing#121299").percentageCompensation(Byte.valueOf("50")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(12L).maxQuantity(14L).validFrom(LocalDate.now().plusDays(2)).validTo(LocalDate.now().plusDays(43)).build();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(planRepository.save(any())).thenReturn(compensationPlan);

        //When
        String errorMsg = "Compensation Error: User Already Has A Compensation Plan";
        assertThatThrownBy(() -> planService.createCompensationPlan(1L, compensationPlan))
                .isInstanceOf(PartnerCompServiceException.class)
                .hasMessageMatching(errorMsg);
    }

    @Test
    void retrieveCompensationPlanForUserId_ValidTest() {
        //Given
        User user = User.builder()
                .employeeId(1L)
                .password("qwerty")
                .emailId("test@gmail.com")
                .firstName("Tester One")
                .lastName("Tester Two")
                .role(Role.COMPENSATION_PLAN_USER)
                .compensationPlanId(1L)
                .build();
        CompensationPlan compensationPlan = CompensationPlan.builder().planId(1L).partnerName("Amul Co").compensationPlanName("Marketing#121299").percentageCompensation(Byte.valueOf("50")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(12L).maxQuantity(14L).validFrom(LocalDate.now().plusDays(2)).validTo(LocalDate.now().plusDays(43)).build();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(planRepository.findById(anyLong())).thenReturn(Optional.ofNullable(compensationPlan));

        //When
        CompensationPlan compensationPlan1 = planService.retrieveCompensationPlanForUserId(1L);
        //Then
        assertThat(compensationPlan1.getPlanId()).isNotNull().isEqualTo(1L);
    }

    @Test
    void retrieveCompensationPlanForUserId_InvalidTest_user() {
        //Given
        CompensationPlan compensationPlan = CompensationPlan.builder().planId(1L).partnerName("Amul Co").compensationPlanName("Marketing#121299").percentageCompensation(Byte.valueOf("50")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(12L).maxQuantity(14L).validFrom(LocalDate.now().plusDays(2)).validTo(LocalDate.now().plusDays(43)).build();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Mockito.when(planRepository.findById(anyLong())).thenReturn(Optional.ofNullable(compensationPlan));

        //When
        String errorMsg = "Compensation Retrieve Error: userId is Invalid/Doesn't Exist";
        assertThatThrownBy(() -> planService.retrieveCompensationPlanForUserId(1L))
                .isInstanceOf(PartnerCompServiceException.class)
                .hasMessageMatching(errorMsg);
    }

    @Test
    void retrieveCompensationPlanForUserId_InvalidTest_NoCompensationPlanPresent() {
        //Given
        User user = User.builder()
                .employeeId(1L)
                .password("qwerty")
                .emailId("test@gmail.com")
                .firstName("Tester One")
                .lastName("Tester Two")
                .role(Role.COMPENSATION_PLAN_USER)
                .compensationPlanId(1L)
                .build();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(planRepository.findById(anyLong())).thenReturn(Optional.empty());

        //When
        String errorMsg = "Compensation Retrieve Error: Compensation Plan Doesn't Exist for this UserId";
        assertThatThrownBy(() -> planService.retrieveCompensationPlanForUserId(1L))
                .isInstanceOf(PartnerCompServiceException.class)
                .hasMessageMatching(errorMsg);
    }

    @Test
    void deleteCompensationPlan_ValidTest() {
        //Given
        User user = User.builder()
                .employeeId(1L)
                .password("qwerty")
                .emailId("test@gmail.com")
                .firstName("Tester One")
                .lastName("Tester Two")
                .role(Role.COMPENSATION_PLAN_USER)
                .compensationPlanId(1L)
                .build();
        CompensationPlan compensationPlan = CompensationPlan.builder().planId(1L).partnerName("Amul Co").compensationPlanName("Marketing#121299").percentageCompensation(Byte.valueOf("50")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(12L).maxQuantity(14L).validFrom(LocalDate.now().plusDays(2)).validTo(LocalDate.now().plusDays(43)).build();
        Mockito.when(planRepository.findById(anyLong())).thenReturn(Optional.ofNullable(compensationPlan));
        Mockito.when(userRepository.findByCompensationPlanId(anyLong())).thenReturn(Optional.ofNullable(user));

        //When
        boolean b = planService.deleteCompensationPlan(1L);

        //Then
        assertThat(b).isTrue();
    }

    @Test
    void deleteCompensationPlan_InvalidTest_User() {
        //Given
        CompensationPlan compensationPlan = CompensationPlan.builder().planId(1L).partnerName("Amul Co").compensationPlanName("Marketing#121299").percentageCompensation(Byte.valueOf("50")).calculationMethodology(CalculationMethodology.VOLUME).minQuantity(12L).maxQuantity(14L).validFrom(LocalDate.now().plusDays(2)).validTo(LocalDate.now().plusDays(43)).build();
        Mockito.when(planRepository.findById(anyLong())).thenReturn(Optional.ofNullable(compensationPlan));
        Mockito.when(userRepository.findByCompensationPlanId(anyLong())).thenReturn(Optional.empty());

        //When
        String errorMsg = "\\*\\*CRITICAL ERROR\\*\\* Compensation Delete Error: No User is associated with Compensation Plan";
        assertThatThrownBy(() -> planService.deleteCompensationPlan(1L))
                .isInstanceOf(PartnerCompServiceException.class)
                .hasMessageMatching(errorMsg);
    }

    @Test
    void deleteCompensationPlan_InvalidTest_PlanNotPresent() {
        //Given
        User user = User.builder()
                .employeeId(1L)
                .password("qwerty")
                .emailId("test@gmail.com")
                .firstName("Tester One")
                .lastName("Tester Two")
                .role(Role.COMPENSATION_PLAN_USER)
                .compensationPlanId(1L)
                .build();
        Mockito.when(planRepository.findById(anyLong())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByCompensationPlanId(anyLong())).thenReturn(Optional.ofNullable(user));

        //When
        String errorMsg = "Compensation Delete Error: Plan ID is Invalid";
        assertThatThrownBy(() -> planService.deleteCompensationPlan(1L))
                .isInstanceOf(PartnerCompServiceException.class)
                .hasMessageMatching(errorMsg);
    }

    @Test
    void getAllCompensationPlans_validTest(){
        Mockito.when(planRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(CompensationPlan.builder().build(),CompensationPlan.builder().build())));
        Page<CompensationPlan> allCompensationPlans = planService.getAllCompensationPlans(0);
        assertThat(allCompensationPlans).isNotNull();
        assertThat(allCompensationPlans.getContent()).isNotNull();
    }
}