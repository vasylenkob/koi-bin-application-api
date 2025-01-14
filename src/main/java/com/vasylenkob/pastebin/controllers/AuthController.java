package com.vasylenkob.pastebin.controllers;

import com.vasylenkob.pastebin.dto.LoginRequest;
import com.vasylenkob.pastebin.dto.SignUpRequest;
import com.vasylenkob.pastebin.dto.VerifyRequest;
import com.vasylenkob.pastebin.services.SignupService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final SignupService signupService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            return ResponseEntity.ok("You log in successfully!");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        signupService.signUp(signUpRequest);
        return ResponseEntity.ok("You signed up successfully! Please, verify your email");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyRequest verifyRequest){
        signupService.verify(verifyRequest);
        return ResponseEntity.ok("Verification completed");
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendEmail(@RequestParam String email){
        signupService.resendEmail(email);
        return ResponseEntity.ok("Verification code sent");
    }
}
