package org.example.user.service;

import lombok.RequiredArgsConstructor;
import org.example.common.exception.CommonException;
import org.example.user.domain.RegisterRequest;
import org.example.user.domain.User;
import org.example.user.service.port.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.common.exception.ErrorCode.EMAIL_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new CommonException(EMAIL_ALREADY_EXISTS);
        }

        User user = User.from(
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.password())
        );
        userRepository.save(user);
    }
}
