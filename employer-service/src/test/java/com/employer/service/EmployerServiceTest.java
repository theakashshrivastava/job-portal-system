package com.employer.service;

import com.employer.dto.LoginRequest;
import com.employer.entity.Employer;
import com.employer.feign.JobSeekerClient;
import com.employer.repository.EmployerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployerServiceTest {

    @Mock
    private EmployerRepository employerRepository;

    @Mock
    private JobSeekerClient jobSeekerClient;

    @InjectMocks
    private EmployerService employerService;

    @Test
    void testRegister() {
        Employer employer = new Employer();
        when(employerRepository.save(employer)).thenReturn(employer);

        Employer result = employerService.register(employer);

        assertEquals(employer, result);
        verify(employerRepository).save(employer);
    }

    @Test
    void testLoginSuccess() {
        Employer employer = new Employer();
        when(employerRepository.findByUsername("testUser")).thenReturn(Optional.of(employer));

        String result = employerService.login(new LoginRequest());

        assertEquals("Login successful", result);
    }

    @Test
    void testLoginFailure() {
        when(employerRepository.findByUsername("wrongUser")).thenReturn(Optional.empty());

        String result = employerService.login(new LoginRequest());

        assertEquals("Invalid credentials", result);
    }
}

