package com.vynnyk.nurseapp.controller;

import com.vynnyk.nurseapp.config.JwtUtil;
import com.vynnyk.nurseapp.domain.User;
import com.vynnyk.nurseapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	private final UserRepository userRepo;
	private final PasswordEncoder encoder;
	private final JwtUtil jwtUtil;

	@PostMapping("/register")
	public String register(@RequestBody User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userRepo.save(user);
		return "Registered";
	}

	@PostMapping("/login")
	public String login(@RequestBody Auth auth) {
		User user = userRepo.findByUsername(auth.login()).orElseThrow();
		if (encoder.matches(auth.password(), user.getPassword())) {
			// Генерируем access token и refresh token
			String accessToken = jwtUtil.generateToken(user);
			String refreshToken = jwtUtil.generateRefreshToken(user);
			return String.format("{\"accessToken\":\"%s\", \"refreshToken\":\"%s\"}", accessToken, refreshToken);
		} else {
			throw new RuntimeException("Invalid credentials");
		}
	}

	@PostMapping("/refresh-token")
	public String refreshToken(@RequestBody Token token) {
		// Проверка и обновление access token
		User user = userRepo.findByUsername(token.token()).orElseThrow();
		return jwtUtil.refreshAccessToken(token.token(), user);
	}

	private record Auth(String login, String password) {
	}

	private record Token(String token) {
	}
}
