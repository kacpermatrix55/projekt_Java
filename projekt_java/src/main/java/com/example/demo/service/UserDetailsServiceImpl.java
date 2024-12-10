package com.example.demo.service;

import com.example.demo.data.RegisterRequest;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.demo.entity.User found = userRepository.findByUsername(username);

        if (found == null) {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }

        String[] roles = found.roles.stream().map(Enum::name).toArray(String[]::new);

        return User.builder().username(username).password(found.password).roles(roles).build();
    }

    @Transactional
    public List<String> register(RegisterRequest request) {
        if (request.username.length() < 4) {
            return List.of("Username too short (min 4 characters)");
        }

        if (userRepository.findByUsername(request.username) != null) {
            return List.of("Username already exists");
        }

        com.example.demo.entity.User user = new com.example.demo.entity.User();
        user.username = request.username;
        user.password = passwordEncoder.encode(request.password);

//        if (request.access == null || request.access.isEmpty()) {
//            return List.of("No access requested, please choose at least one");
//        }

        if (request.access != null) {
            for (String requested : request.access) {
                if (requested.contentEquals("editor")) {
                    user.roles.add(Role.EDITOR);
                } else {
                    return List.of("Unrecognized requested access: " + requested);
                }
            }
        }

        userRepository.save(user);

        return List.of();
    }
}
