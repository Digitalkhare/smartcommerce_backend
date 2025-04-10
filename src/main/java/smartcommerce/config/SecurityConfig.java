package smartcommerce.config;

import java.util.List;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import smartcommerce.security.JwtAuthenticationFilter;
import smartcommerce.service.UserDetailsServiceImpl;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
 private final JwtAuthenticationFilter jwtAuthFilter;
 private final UserDetailsServiceImpl  userDetailsService;
 
 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     return http
    		 .cors(withDefaults()) 
             .csrf(csrf -> csrf.disable())
             .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .authorizeHttpRequests(auth -> auth
                     .requestMatchers("/api/categories/**", "/api/products/**","/api/reviews/**","/api/cart/add/**","/error", "/api/auth/**").permitAll()
                     .requestMatchers("/api/admin/**").hasRole("ADMIN")
                     .anyRequest().authenticated()
             )
             .userDetailsService(userDetailsService)
             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
             .build();
 }
 
 @Bean
 public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
         throws Exception {
     return config.getAuthenticationManager();
 }
 
 @Bean
 public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
 }
 @Bean
 public CorsConfigurationSource corsConfigurationSource() {
     CorsConfiguration configuration = new CorsConfiguration();
     configuration.setAllowedOrigins(List.of("http://localhost:3000"));
     configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
     configuration.setAllowedHeaders(List.of("*"));
     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
     source.registerCorsConfiguration("/**", configuration);
     return source;
 }

}
