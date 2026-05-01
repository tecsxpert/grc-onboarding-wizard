package com.internship.tool.service;

import com.internship.tool.entity.User;
import com.internship.tool.entity.Role;
import com.internship.tool.repository.UserRepository;
import com.internship.tool.exception.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Karthik");
        user.setEmail("test@gmail.com");
        user.setPassword("123456");
        user.setRole(Role.USER);
    }

    // =========================
    // CREATE USER
    // =========================

    @Test
    void createUser_success() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_duplicateEmail() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> userService.createUser(user));
    }

    @Test
    void createUser_nullEmail() {
        user.setEmail(null);

        assertThrows(InvalidInputException.class,
                () -> userService.createUser(user));
    }

    @Test
    void createUser_nullPassword() {
        user.setPassword(null);

        assertThrows(InvalidInputException.class,
                () -> userService.createUser(user));
    }

    @Test
    void createUser_emptyEmail() {
        user.setEmail("");

        assertThrows(InvalidInputException.class,
                () -> userService.createUser(user));
    }

    @Test
    void createUser_emptyPassword() {
        user.setPassword("");

        assertThrows(InvalidInputException.class,
                () -> userService.createUser(user));
    }

    @Test
    void createUser_nullUser() {
        assertThrows(InvalidInputException.class,
                () -> userService.createUser(null));
    }

    // =========================
    // GET USER
    // =========================

    @Test
    void getUserById_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1L));
    }

    // =========================
    // GET ALL USERS
    // =========================

    @Test
    void getAllUsers_nonEmpty() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
    }

    @Test
    void getAllUsers_empty() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> users = userService.getAllUsers();

        assertTrue(users.isEmpty());
    }

    @Test
    void getAllUsers_nullList() {
        when(userRepository.findAll()).thenReturn(null);

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
    }

    // =========================
    // UPDATE USER
    // =========================

    @Test
    void updateUser_success() {
        User existing = new User();
        existing.setId(1L);
        existing.setEmail("old@gmail.com");

        User update = new User();
        update.setName("Updated");
        update.setPassword("newpass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1L, update);

        assertEquals("Updated", result.getName());
        verify(passwordEncoder).encode("newpass");
    }

    @Test
    void updateUser_changeEmail_success() {
        User existing = new User();
        existing.setId(1L);
        existing.setEmail("old@gmail.com");

        User update = new User();
        update.setEmail("new@gmail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmail("new@gmail.com")).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1L, update);

        assertEquals("new@gmail.com", result.getEmail());
    }

    @Test
    void updateUser_sameEmail_noDuplicateCheck() {
        User existing = new User();
        existing.setId(1L);
        existing.setEmail("same@gmail.com");

        User update = new User();
        update.setEmail("same@gmail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1L, update);

        assertEquals("same@gmail.com", result.getEmail());
    }

    @Test
    void updateUser_emptyFields_shouldIgnore() {
        User existing = new User();
        existing.setId(1L);
        existing.setName("Old");
        existing.setEmail("old@gmail.com");

        User update = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1L, update);

        assertEquals("Old", result.getName());
        assertEquals("old@gmail.com", result.getEmail());
    }

    @Test
    void updateUser_nullInput() {
        assertThrows(InvalidInputException.class,
                () -> userService.updateUser(1L, null));
    }

    @Test
    void updateUser_emailConflict() {
        User existing = new User();
        existing.setId(1L);
        existing.setEmail("current@gmail.com");

        User update = new User();
        update.setEmail("taken@gmail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmail("taken@gmail.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> userService.updateUser(1L, update));
    }

    @Test
    void updateUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(1L, user));
    }

    @Test
    void updateUser_withoutPasswordChange() {
        User existing = new User();
        existing.setId(1L);
        existing.setPassword("oldpass");

        User update = new User();
        update.setName("Updated");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1L, update);

        assertEquals("oldpass", result.getPassword());
    }

    // =========================
    // DELETE USER
    // =========================

    @Test
    void deleteUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(1L));
    }
    @Test
    void getUserById_nullId() {
        assertThrows(Exception.class,
                () -> userService.getUserById(null));
    }

}