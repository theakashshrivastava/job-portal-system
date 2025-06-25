package com.employer.service;

import com.employer.entity.Job;
import com.employer.kafka.KafkaProducerService;
import com.employer.repository.JobRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final KafkaProducerService kafkaProducerService;

    public JobService(JobRepository jobRepository, KafkaProducerService kafkaProducerService){
        this.jobRepository = jobRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    public Job postJob(Job job) {
        Job savedJob = jobRepository.save(job);
        sendJobCreatedEvent(savedJob);
        return savedJob;
    }

    public Job updateJob(Long id, Job jobDetails) {
        Job job = jobRepository.findById(id).orElseThrow();
        job.setTitle(jobDetails.getTitle());
        job.setLocation(jobDetails.getLocation());
        job.setDescription(jobDetails.getDescription());
        job.setExperienceRequired(jobDetails.getExperienceRequired());
        job.setSalary(jobDetails.getSalary());
        job.setNoticePeriod(jobDetails.getNoticePeriod());
        job.setContactEmail(jobDetails.getContactEmail());
        job.setStatus(jobDetails.getStatus());
        Job updatedJob = jobRepository.save(job);
        sendJobUpdatedEvent(updatedJob);
        return updatedJob;
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
        sendJobDeletedEvent(id);
    }

    public List<Job> getJobsByEmployer(Long employerId) {
        return jobRepository.findByEmployerId(employerId);
    }

    /** Kafka Event Methods **/
    private void sendJobCreatedEvent(Job job) {
        sendKafkaEvent(job, kafkaProducerService::sendJobCreatedEvent);
    }

    private void sendJobUpdatedEvent(Job job) {
        sendKafkaEvent(job, kafkaProducerService::sendJobUpdatedEvent);
    }

    private void sendJobDeletedEvent(Long jobId) {
        kafkaProducerService.sendJobDeletedEvent("Job with ID " + jobId + " deleted");
    }

    private void sendKafkaEvent(Job job, java.util.function.Consumer<String> kafkaSender) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String message = mapper.writeValueAsString(job);
            kafkaSender.accept(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
