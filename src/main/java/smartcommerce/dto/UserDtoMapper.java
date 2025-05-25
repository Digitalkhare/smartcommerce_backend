package smartcommerce.dto;

import smartcommerce.model.User;


// will be used in the Usecontroller to map from user to UserDto objects
// don't want to expose sensitive data from user like user password
public class UserDtoMapper {
    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setStatus(user.isEnabled() ? "Active" : "Inactive");
        //System.err.println("Enabled "+user.isEnabled());

        return dto;
    }
}
