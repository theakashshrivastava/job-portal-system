package com.employer.controller;

import com.employer.entity.Job;
import com.employer.service.JobService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(JobController.class)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @Autowired
    private ObjectMapper objectMapper;

    private Job createSampleJob() {
        Job job = new Job();
        job.setId(1L);
        job.setTitle("Backend Developer");
        job.setLocation("Pune");
        job.setDescription("Spring Boot Developer");
        job.setExperienceRequired(3);
        job.setSalary(new BigDecimal("1500000"));
        job.setNoticePeriod(30);
        job.setContactEmail("hr@example.com");
        job.setStatus("OPEN");
        job.setEmployerId(101L);
        return job;
    }

    @Test
    void testGetJobsByEmployer() throws Exception {
        Job job = createSampleJob();
        Mockito.when(jobService.getJobsByEmployer(101L)).thenReturn(List.of(job));

        mockMvc.perform(get("/api/jobs/employer/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Backend Developer"));
    }

    @Test
    void testReceiveJobApplication() throws Exception {
        mockMvc.perform(post("/api/jobs/employer/jobs/1/apply")
                        .param("jobSeekerId", "200"))
                .andExpect(status().isOk())
                .andExpect(content().string("Application received for job ID 1 from job seeker ID 200"));
    }

    @Test
    void testPostJob() throws Exception {
        Job job = createSampleJob();
        Mockito.when(jobService.postJob(any(Job.class))).thenReturn(job);

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(job)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Backend Developer"));
    }

    @Test
    void testUpdateJob() throws Exception {
        Job job = createSampleJob();
        job.setTitle("Updated Title");
        Mockito.when(jobService.updateJob(eq(1L), any(Job.class))).thenReturn(job);

        mockMvc.perform(put("/api/jobs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(job)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void testDeleteJob() throws Exception {
        mockMvc.perform(delete("/api/jobs/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(jobService).deleteJob(1L);
    }
}

