package com.dnd.dotchi.domain.healthcheck.service;

import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    public String check() {
        return "health-check-new";
    }

}
