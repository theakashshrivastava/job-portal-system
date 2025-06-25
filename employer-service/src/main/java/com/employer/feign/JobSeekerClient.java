package com.employer.feign;

import com.employer.dto.JobSeekerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "jobseeker-service", url = "http://localhost:8081")
public interface JobSeekerClient {

    @GetMapping("/jobseekers/{id}")
    JobSeekerDTO getJobSeekerById(@PathVariable Long id);
}

