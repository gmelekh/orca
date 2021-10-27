package com.orca.attack.orca.controller;


import com.orca.attack.orca.model.StatsResponse;
import com.orca.attack.orca.service.OrcaSecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@Validated
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/v1/api")
public class OrcaController {

    private final OrcaSecurityService securityService;

    @GetMapping(path = "/attack")
    @ResponseStatus(OK)
    public List<String> getPotentialAttackers(@RequestParam(value = "vm_id", required = false, defaultValue = "") String vmId) {
        log.info("get potential attackers for {} vm request is invoked", vmId);
        return securityService.getPotentialAttackersByVmId(vmId);
    }

    @GetMapping(path = "/self")
    @ResponseStatus(OK)
    public Map<String, List<String>> runSelfCheck() {
        return securityService.selfCheck();
    }

    @GetMapping(path = "/stats")
    @ResponseStatus(OK)
    public StatsResponse getStats() {
        log.info("get stats request is invoked");
        return securityService.getStats();
    }
}
