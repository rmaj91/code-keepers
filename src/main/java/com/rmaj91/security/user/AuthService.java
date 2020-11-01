package com.rmaj91.security.user;

import com.rmaj91.logging.Monitoring;
import com.rmaj91.security.config.JwtUtil;
import com.rmaj91.security.domain.AuthRequest;
import com.rmaj91.security.domain.JwtResponse;
import com.rmaj91.security.domain.RegistrationRequest;
import com.rmaj91.security.exceptions.IncorrectUsernamePasswordException;
import com.rmaj91.security.exceptions.UsernameAlreadyTakenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Monitoring
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public void register(RegistrationRequest request) {
        Optional.of(userRepository.existsByUsername(request.getUsername()))
                .filter(exist -> !exist)
                .map(__ -> User.builder()
                        .username(request.getUsername())
                        .password(encoder.encode(request.getPassword()))
                        .role(Role.USER)
                        .build()
                )
                .map(userRepository::save)
                .orElseThrow(UsernameAlreadyTakenException::new);
    }

    public JwtResponse login(AuthRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> isPasswordMatching(request.getPassword(), user.getPassword()))
                .map(jwtUtil::generateJwtResponse)
                .orElseThrow(IncorrectUsernamePasswordException::new);
    }

    private boolean isPasswordMatching(String requestPassword, String passwordHash) {
        return encoder.matches(requestPassword, passwordHash);
    }
}
