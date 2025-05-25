package smartcommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import smartcommerce.dto.UserDto;
import smartcommerce.dto.UserDtoMapper;
import smartcommerce.model.User;
import smartcommerce.service.UserDetailsServiceImpl;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserController {

    private final UserDetailsServiceImpl userService;
    
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers() {
    	List<User> users = userService.findAllUsers();
        return users.stream()
                .map(UserDtoMapper::toDto)
                .toList();     // Java 17+; if older use .collect(Collectors.toList())
    }
}