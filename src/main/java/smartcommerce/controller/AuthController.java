package smartcommerce.controller;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import smartcommerce.dto.JwtResponse;
import smartcommerce.dto.LoginRequest;
import smartcommerce.dto.RegisterRequest;
import smartcommerce.model.Role;
import smartcommerce.model.User;
import smartcommerce.repository.UserRepository;
import smartcommerce.security.JwtService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	@PostMapping("/register")
	public String register(@RequestBody RegisterRequest request) {
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		
		//below is just for testing. revert to user user.setRole(Role.USER) as default and set ADMIN role by other secure means
		user.setRole(request.getRole() != null ? request.getRole() : Role.USER);
		userRepository.save(user);
		return "User registered with role: "+user.getRole();
	}

	@PostMapping("/login")
	public JwtResponse login(@RequestBody LoginRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
		String jwt = jwtService.generateToken(new org.springframework.security.core.userdetails.User(user.getEmail(),
				user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))));
		return new JwtResponse(jwt);
	}
}
