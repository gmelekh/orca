package com.orca.attack.orca.config;

import com.orca.attack.orca.model.FirewallRule;
import com.orca.attack.orca.model.VirtualMachine;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
public class EnvironmentConfiguration {
    private List<VirtualMachine> vms;
    private List<FirewallRule> fw_rules ;
}
