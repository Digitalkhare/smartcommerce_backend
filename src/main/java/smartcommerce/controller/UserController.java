package smartcommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import smartcommerce.dto.StatusUpdateDto;
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
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
    @PutMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUserStatus(@PathVariable Long id, @RequestBody StatusUpdateDto dto) {
        userService.updateUserStatus(id, dto.getStatus());
    }


}