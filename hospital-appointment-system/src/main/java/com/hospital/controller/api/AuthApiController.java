package com.hospital.controller.api;

import com.hospital.dto.LoginRequest;
import com.hospital.dto.RegisterUserRequest;
import com.hospital.dto.UserResponse;
import com.hospital.entity.User;
import com.hospital.exception.NotAuthenticatedException;
import com.hospital.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Patient registration, login, logout and session lookup.
 * Equivalent to the legacy UserRegisterServlet / UserLoginServlet.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final UserService userService;

    public AuthApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setMobile(request.getMobile());

        User saved = userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(saved));
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        User user = userService.authenticate(request.getEmail(), request.getPassword());
        servletRequest.getSession().setAttribute("user", user);
        return UserResponse.from(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public UserResponse me(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new NotAuthenticatedException("No active patient session.");
        }
        return UserResponse.from(user);
    }
}
