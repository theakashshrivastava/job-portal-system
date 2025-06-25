package com.employer.controller;

import com.employer.dto.LoginRequest;
import com.employer.entity.Employer;
import com.employer.service.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employers")
public class EmployerController {

    private final EmployerService employerService;

    @Autowired
    public EmployerController(EmployerService employerService){
        this.employerService = employerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Employer> register(@RequestBody Employer employer) {
        return ResponseEntity.ok(employerService.register(employer));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(employerService.login(loginRequest));
    }
}

