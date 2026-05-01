package com.internship.tool.service;

import com.internship.tool.entity.User;
import com.internship.tool.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReminderService reminderService;

    @Test
    void test_noUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        reminderService.sendDailyReminders();

        verify(emailService, never()).sendEmail(any(), any(), any(), any());
    }

    @Test
    void test_validUser() {
        User user = new User();
        user.setEmail("test@gmail.com");

        when(userRepository.findAll()).thenReturn(List.of(user));

        reminderService.sendDailyReminders();

        verify(emailService).sendEmail(any(), any(), any(), any());
    }

    @Test
    void test_invalidUser() {
        User user = new User();
        user.setEmail("");

        when(userRepository.findAll()).thenReturn(List.of(user));

        reminderService.sendDailyReminders();

        verify(emailService, never()).sendEmail(any(), any(), any(), any());
    }

    @Test
    void test_exceptionFlow() {
        User user = new User();
        user.setEmail("test@gmail.com");

        when(userRepository.findAll()).thenReturn(List.of(user));
        doThrow(new RuntimeException()).when(emailService)
                .sendEmail(any(), any(), any(), any());

        reminderService.sendDailyReminders();

        verify(emailService).sendEmail(any(), any(), any(), any());
    }
    @Test
    void test_emptyUserList() {
        when(userRepository.findAll()).thenReturn(List.of());

        reminderService.sendDailyReminders();

        verify(emailService, never()).sendEmail(any(), any(), any(), any());
    }
    @Test
    void test_skipUserWithoutEmail() {
        User user = new User();
        user.setId(1L);
        user.setEmail(""); // invalid

        when(userRepository.findAll()).thenReturn(List.of(user));

        reminderService.sendDailyReminders();

        verify(emailService, never()).sendEmail(any(), any(), any(), any());
    }
    @Test
    void test_nullEmailUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail(null);

        when(userRepository.findAll()).thenReturn(List.of(user));

        reminderService.sendDailyReminders();

        verify(emailService, never()).sendEmail(any(), any(), any(), any());
    }

    @Test
    void test_multipleUsers_mixedCases() {
        User valid = new User();
        valid.setEmail("valid@gmail.com");
        valid.setName("Valid");

        User invalid = new User();
        invalid.setEmail(""); // should skip

        when(userRepository.findAll()).thenReturn(List.of(valid, invalid));

        reminderService.sendDailyReminders();

        verify(emailService, times(1)).sendEmail(eq("valid@gmail.com"), any(), any(), any());
    }

}