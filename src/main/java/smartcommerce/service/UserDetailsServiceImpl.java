package smartcommerce.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import smartcommerce.model.User;
import smartcommerce.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
		 private final UserRepository userRepository;

		    @Override
		    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		        User user = userRepository.findByEmail(email)
		                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		        return new org.springframework.security.core.userdetails.User(
		                user.getEmail(),
		                user.getPassword(),
		                user.getRole().name().equals("ADMIN")
		                        ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
		                        : List.of(new SimpleGrantedAuthority("ROLE_USER"))
		        );
	}
		 
		    public User findByEmail(String email) {
		        return userRepository.findByEmail(email)
		            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
		    }


}
