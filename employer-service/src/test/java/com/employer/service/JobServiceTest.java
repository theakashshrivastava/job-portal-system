package com.employer.service;

import com.employer.entity.Job;
import com.employer.repository.JobRepository;
import com.employer.kafka.KafkaProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private JobService jobService;

    private Job job;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this.job = new Job());
        job.setId(1L);
        job.setTitle("Software Engineer");
        job.setLocation("Pune");
        job.setDescription("Develop applications");
        job.setExperienceRequired(2);
        job.setSalary(new BigDecimal("1000000"));
        job.setNoticePeriod(30);
        job.setContactEmail("hr@example.com");
        job.setStatus("OPEN");
        job.setEmployerId(100L);
    }

    @Test
    void testPostJob() {
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        Job result = jobService.postJob(job);

        assertNotNull(result);
        assertEquals("Software Engineer", result.getTitle());
        verify(kafkaProducerService).sendJobCreatedEvent(anyString());
    }

    @Test
    void testUpdateJob() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        Job updatedDetails = new Job();
        updatedDetails.setTitle("Senior Engineer");
        updatedDetails.setLocation("Mumbai");
        updatedDetails.setDescription("Lead development");
        updatedDetails.setExperienceRequired(5);
        updatedDetails.setSalary(new BigDecimal("1000000"));
        updatedDetails.setNoticePeriod(60);
        updatedDetails.setContactEmail("lead@example.com");
        updatedDetails.setStatus("OPEN");

        Job result = jobService.updateJob(1L, updatedDetails);

        assertEquals("Senior Engineer", result.getTitle());
        verify(kafkaProducerService).sendJobUpdatedEvent(anyString());
    }

    @Test
    void testDeleteJob() {
        doNothing().when(jobRepository).deleteById(1L);

        jobService.deleteJob(1L);

        verify(jobRepository).deleteById(1L);
        verify(kafkaProducerService).sendJobDeletedEvent("Job with ID 1 deleted");
    }

    @Test
    void testGetJobsByEmployer() {
        List<Job> jobs = List.of(job);
        when(jobRepository.findByEmployerId(100L)).thenReturn(jobs);

        List<Job> result = jobService.getJobsByEmployer(100L);

        assertEquals(1, result.size());
        assertEquals("Software Engineer", result.getFirst().getTitle());
    }
}

