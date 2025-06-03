package smartcommerce.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String status;
    private List<OrderDto> orders;
}
