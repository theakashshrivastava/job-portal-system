package com.employer.controller;

import com.employer.entity.Job;
import com.employer.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService)
    {
        this.jobService = jobService;
    }

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<Job>> getJobsByEmployer(@PathVariable Long employerId) {
        return ResponseEntity.ok(jobService.getJobsByEmployer(employerId));
    }

    @PostMapping("/employer/jobs/{jobId}/apply")
    public ResponseEntity<String> receiveJobApplication(
            @PathVariable String jobId,
            @RequestParam Long jobSeekerId) {
        // Log or process the application
        return ResponseEntity.ok("Application received for job ID " + jobId + " from job seeker ID " + jobSeekerId);
    }

    /* Producing Events */

    @PostMapping
    public ResponseEntity<Job> postJob(@RequestBody Job job) {
        return ResponseEntity.ok(jobService.postJob(job));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job) {
        return ResponseEntity.ok(jobService.updateJob(id, job));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}

