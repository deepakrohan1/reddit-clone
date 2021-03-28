package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.TokenNotFoundException;
import com.example.demo.model.NotificationEmail;
import com.example.demo.model.User;
import com.example.demo.model.VerificationToken;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.ws.ServiceMode;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private MailService mailService;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendEmail(new NotificationEmail("Please activate your account",
                "http://localhost:8080/api/auth/accountVerification/" + token,
                      user.getEmail()
                ));
    }

    private String generateVerificationToken(User user) {
        String tokenTemp = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(tokenTemp);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return tokenTemp;
    }


    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        log.info(verificationToken.get().getToken());
        verificationToken.orElseThrow(() -> new TokenNotFoundException("Token not found"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UserNotFoundException("No valid user found:" +username));
        user.get().setEnabled(true);
        log.info("Is user enabled:" +user.get().isEnabled());
        userRepository.save(user.get());
    }
}
