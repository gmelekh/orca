package com.orca.attack.orca.service;

import com.orca.attack.orca.config.EnvironmentConfiguration;
import com.orca.attack.orca.exceptions.OrcaException;
import com.orca.attack.orca.model.StatsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yaml"})
class OrcaSecurityServiceTest {

    @Autowired
    private EnvironmentConfiguration configuration;
    private OrcaSecurityService orcaSecurityService;


    @BeforeEach
    void setup(){
        orcaSecurityService = new OrcaSecurityService(configuration);
    }

    @Test
    public void itShouldSuccessfullyGetListOfPotentialAttackers(){
        final var actual = orcaSecurityService.getPotentialAttackersByVmId("vm-a211de");
        assertThat(actual).isEqualTo(List.of("vm-c7bac01a07"));
    }

    @Test
    public void itShouldThrowOrcaExceptionOnWrongInput(){
        assertThatExceptionOfType(OrcaException.class)
                .isThrownBy(()->orcaSecurityService.getPotentialAttackersByVmId("not-valid"))
                .withMessage("Undefined vm_id: not-valid provided as input");
    }

    @Test
    public void itShouldSuccessfullyCountStats(){
        orcaSecurityService.getPotentialAttackersByVmId("vm-a211de");
        final var actual = orcaSecurityService.getStats();
        final var expected = StatsResponse.builder().vmCount(2).requestCount(2).averageRequestTime("0.00001").build();
        assertThat(actual.getVmCount()).isEqualTo(2);
        assertThat(actual.getRequestCount()).isEqualTo(2);
        assertThat(Double.parseDouble(actual.getAverageRequestTime())).isGreaterThan(Double.valueOf(expected.getAverageRequestTime()));
    }

    @Test
    public void itShouldCountStats_WhenStatsIsFirstRequest(){
        final var actual = orcaSecurityService.getStats();
        assertThat(actual).extracting("requestCount").isEqualTo(1);
    }

}