package com.orca.attack.orca.service;


import com.orca.attack.orca.config.EnvironmentConfiguration;
import com.orca.attack.orca.exceptions.OrcaException;
import com.orca.attack.orca.model.StatsResponse;
import com.orca.attack.orca.model.VirtualMachine;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@Slf4j
public class OrcaSecurityService {

    private final Map<String, List<String>> tagsByVm;
    private final List<Pair<String, String>> firewallRules;
    private final List<Pair<Integer, Long>> stats;
    private final EnvironmentConfiguration configuration;
    private static final int MILLIS_TO_SEC = 1000;
    private static final String MILLIS_FORMAT_PATTERN = "#0.0000000000";

    public OrcaSecurityService(EnvironmentConfiguration configuration) {
        this.configuration = configuration;
        this.tagsByVm = initializeVms();
        this.firewallRules = initializeRules();
        this.stats = new ArrayList<>();

        log.debug("OrcaSecurityService initialized. tagsByVm: {} \n firewallRules: {}", tagsByVm, firewallRules);
    }

    private Map<String, List<String>> initializeVms() {
        return configuration.getVms().stream().collect(Collectors.toMap(VirtualMachine::getVm_id, VirtualMachine::getTags));
    }

    private List<Pair<String, String>> initializeRules() {
        return configuration.getFw_rules().stream().map(rule -> new Pair<>(rule.getSourceTag(), rule.getDestTag())).collect(Collectors.toList());
    }

    public List<String> getPotentialAttackersByVmId(String vmId) {
        var startTime = System.currentTimeMillis();
        final List<String> requestedVmsTags = tagsByVm.getOrDefault(vmId, null);
        if (requestedVmsTags == null) throw new OrcaException("Undefined vm_id: " + vmId + " provided as input");

        log.debug("requestedVmsTags: {}", requestedVmsTags);
        final List<List<String>> rulesAllowedByRequestedVm = firewallRules.stream()
                .filter(rulesPair -> requestedVmsTags.stream().anyMatch(tag -> rulesPair.getValue().equals(tag)))
                .findAny()
                .stream()
                .map(pair -> List.of(pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());
        log.debug("rulesAllowedByRequestedVm: {}", rulesAllowedByRequestedVm);

        if (requestedVmsTags.isEmpty() || rulesAllowedByRequestedVm.isEmpty()) return List.of();

        var result = tagsByVm.entrySet().stream()
                .filter(entry -> rulesAllowedByRequestedVm.stream().allMatch(rules -> rules.containsAll(entry.getValue())))
                .findAny().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        updateStats(startTime, System.currentTimeMillis());
        log.debug("Response: {}", result);
        return new ArrayList<>(result.keySet());
    }

    public StatsResponse getStats() {
        var startTime = System.currentTimeMillis();
        var vmCount = tagsByVm.size();

        if (stats.isEmpty()) {
            updateStats(startTime, System.currentTimeMillis());
            return new StatsResponse(vmCount, 1, setRequiredPrecision(0.0));
        }

        updateStats(startTime, System.currentTimeMillis());
        return stats.stream()
                .reduce((x, y) -> new Pair<>(x.getKey() + y.getKey(), x.getValue() + y.getValue()))
                .map(pair -> StatsResponse.builder()
                        .vmCount(vmCount)
                        .requestCount(pair.getKey())
                        .averageRequestTime(setRequiredPrecision((double) pair.getValue() / pair.getKey())).build()).orElse(new StatsResponse());
    }

    private String setRequiredPrecision(Double source) {
        final var dec = new DecimalFormat(MILLIS_FORMAT_PATTERN);
        return dec.format(source / MILLIS_TO_SEC);
    }


    private void updateStats(Long startedAt, Long endedAt) {
        stats.add(new Pair<>(1, (endedAt - startedAt)));
    }

    public Map<String, List<String>> selfCheck() {
        return tagsByVm.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> getPotentialAttackersByVmId(e.getKey())
        ));
    }
}
