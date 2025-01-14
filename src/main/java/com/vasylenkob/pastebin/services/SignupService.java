package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.dto.SignUpRequest;
import com.vasylenkob.pastebin.dto.VerifyRequest;
import com.vasylenkob.pastebin.entities.User;
import com.vasylenkob.pastebin.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    @Value("${verificationCode.lifetimeMinutes}")
    private Long codeLifeTimeMinutes;

    public void signUp(SignUpRequest signUpRequest){
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username is already in use");
        }
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .verificationCode(generateVerificationCode())
                .verificationCodeExpiration(LocalDateTime.now().plusMinutes(codeLifeTimeMinutes))
                .build();
        sendVerificationEmail(user);
        userService.save(user);
    }

    public void verify(VerifyRequest verifyRequest) {
        Optional<User> optionalUser = userService.findByEmail(verifyRequest.getEmail());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiration().isBefore(LocalDateTime.now())){
                throw new VerificationCodeExpiredException("Verification code is expired");
            }
            if (user.getVerificationCode().equals(verifyRequest.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiration(null);
                userService.save(user);
            }else{
                throw new InvalidVerificationCodeException("Invalid verification code");
            }
        }
        else {
            throw new UserNotFoundException("User not found");
        }
    }

    public void resendEmail(String email){
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            if (user.isEnabled()){
                throw new UserAlreadyVerifiedException("Account is already verified");
            }
            String verificationCode = generateVerificationCode();
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiration(LocalDateTime.now().plusMinutes(codeLifeTimeMinutes));
            userService.save(user);
            sendVerificationEmail(user);
        }else {
            throw new UserNotFoundException("User not found");
        }
    }

    private void sendVerificationEmail(User user){
        String verificationCode = "VERIFICATION CODE: " + user.getVerificationCode();
        String html = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Email Verification</title>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f9f9f9;
                                color: #333;
                                margin: 0;
                                padding: 20px;
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                height: 100vh;
                            }
                            .email-container {
                                max-width: 600px;
                                background-color: #ffffff;
                                padding: 30px;
                                border-radius: 8px;
                                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                                text-align: center;
                            }
                            .email-container h1 {
                                color: #4CAF50;
                                margin-bottom: 20px;
                            }
                            .email-container p {
                                color: #666;
                                font-size: 16px;
                                line-height: 1.6;
                                margin-bottom: 30px;
                            }
                            .verification-code {
                                display: inline-block;
                                background-color: #4CAF50;
                                color: #ffffff;
                                text-decoration: none;
                                padding: 12px 25px;
                                border-radius: 5px;
                                font-size: 16px;
                                font-weight: bold;
                            }
                            .footer {
                                margin-top: 20px;
                                font-size: 12px;
                                color: #aaa;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="email-container">
                            <h1>Email Verification</h1>
                            <p>Hi there!</p>
                            <p>Thank you for signing up. Please enter the code below to verify your email address.</p>
                            <div class="verification-code">""" +  verificationCode +  """
                            </div>
                            <p>If you didn't request this email, you can safely ignore it.</p>
                        </div>
                    </body>
                    </html>
                """;
        emailService.sendEmail(user.getEmail(), "Email Verification", html);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
