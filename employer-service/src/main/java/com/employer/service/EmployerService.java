package com.employer.service;

import com.employer.dto.JobSeekerDTO;
import com.employer.dto.LoginRequest;
import com.employer.entity.Employer;
import com.employer.feign.JobSeekerClient;
import com.employer.repository.EmployerRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class EmployerService {

    private final EmployerRepository employerRepository;
    private final JobSeekerClient jobSeekerClient;

    private static final Logger log = LoggerFactory.getLogger(EmployerService.class);

    @Autowired
    public EmployerService(EmployerRepository employerRepository, JobSeekerClient jobSeekerClient)
    {
        this.employerRepository = employerRepository;
        this.jobSeekerClient = jobSeekerClient;
    }

    public Employer register(Employer employer) {
        return employerRepository.save(employer);
    }

    public String login(LoginRequest loginRequest) {
        Optional<Employer> employer = employerRepository
                .findByUsername(loginRequest.getUsername());
        if (employer.isPresent() &&
                employer.get().getPassword().equals(loginRequest.getPassword())) {
            return "Login successful";
        }
        return "Invalid credentials";
    }

    public void viewApplicant(Long jobSeekerId) {
    JobSeekerDTO jobSeeker = jobSeekerClient.getJobSeekerById(jobSeekerId);
    log.info("Applicant {}", jobSeeker.getName());
    }
}

