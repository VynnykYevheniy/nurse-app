package com.vynnyk.nurseapp.controller;

import com.vynnyk.nurseapp.domain.User;
import com.vynnyk.nurseapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@AllArgsConstructor
public class ProfileController {

	private final UserRepository userRepository;

	@GetMapping
	public ResponseEntity<User> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
		return userRepository.findByUsername(userDetails.getUsername())
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping
	public ResponseEntity<User> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
											  @RequestBody User updatedUser) {
		return userRepository.findByUsername(userDetails.getUsername()).map(user -> {
			user.setFullName(updatedUser.getFullName());
			user.setPhoneNumber(updatedUser.getPhoneNumber());
			user.setAddress(updatedUser.getAddress());
			return ResponseEntity.ok(userRepository.save(user));
		}).orElse(ResponseEntity.notFound().build());
	}
}
