package com.vasylenkob.pastebin.controllers;

import com.vasylenkob.pastebin.dto.SignUpUser;
import com.vasylenkob.pastebin.dto.VerifyUser;
import com.vasylenkob.pastebin.services.SignupService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class SignupController {
    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpUser signUpUser) {
        signupService.signUp(signUpUser);
        return ResponseEntity.ok("You signed up successfully! Please, verify your email");
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyUser verifyUser){
        signupService.verify(verifyUser);
        return ResponseEntity.ok("Verification completed");
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendEmail(@RequestParam String email){
        signupService.resendEmail(email);
        return ResponseEntity.ok("Verification code sent");
    }
}
