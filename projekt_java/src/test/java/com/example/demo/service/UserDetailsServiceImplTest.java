package com.example.demo.service;

import com.example.demo.data.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(new HashSet<>());

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistent"));
    }

    @Test
    public void testRegister_ValidRequest() {
        RegisterRequest request = new RegisterRequest("newuser", "password123", List.of("editor"));


        when(userRepository.findByUsername("newuser")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        List<String> result = userDetailsService.register(request);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegister_UsernameTooShort() {
        RegisterRequest request = new RegisterRequest("usr", "password123", List.of("editor"));

        List<String> result = userDetailsService.register(request);

        assertEquals(1, result.size());
        assertEquals("Username too short (min 4 characters)", result.get(0));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegister_UsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest("existinguser", "password123", List.of("editor"));

        when(userRepository.findByUsername("existinguser")).thenReturn(new User());

        List<String> result = userDetailsService.register(request);

        assertEquals(1, result.size());
        assertEquals("Username already exists", result.get(0));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegister_UnrecognizedAccess() {
        RegisterRequest request = new RegisterRequest("newuser", "password123", List.of("unknown"));

        List<String> result = userDetailsService.register(request);

        assertEquals(1, result.size());
        assertEquals("Unrecognized requested access: unknown", result.get(0));
        verify(userRepository, never()).save(any(User.class));
    }
}