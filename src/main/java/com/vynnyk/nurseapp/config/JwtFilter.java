package com.vynnyk.nurseapp.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtService;
	private final UserDetailsService userDetailsService;

	public JwtFilter(JwtUtil jwtService, UserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
									@NonNull HttpServletResponse response,
									@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String refreshToken = request.getHeader("Refresh-Token");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String jwt = authHeader.substring(7);

		try {
			processJwtAuthentication(jwt, request);
		} catch (ExpiredJwtException e) {
			if (refreshToken != null && jwtService.isRefreshTokenValid(refreshToken, userDetailsService.loadUserByUsername(e.getClaims().getSubject()))) {
				handleRefreshToken(response, refreshToken);
				return;
			} else {
				handleExpiredJwtException(response);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private void processJwtAuthentication(String jwt, HttpServletRequest request) {
		String username = jwtService.extractUsername(jwt);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			if (jwtService.isAccessTokenValid(jwt, userDetails)) {
				authenticateUser(userDetails, request);
			}
		}
	}

	private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}

	private void handleExpiredJwtException(HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write("JWT token has expired. Please log in again.");
	}

	private void handleRefreshToken(HttpServletResponse response, String refreshToken) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.extractUsername(refreshToken));
		String newAccessToken = jwtService.refreshAccessToken(refreshToken, userDetails);
		response.setHeader("Authorization", "Bearer " + newAccessToken);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.OK.value());
	}
}