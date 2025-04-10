package smartcommerce.dto;

import lombok.Data;
import smartcommerce.model.Role;

//RegisterRequest.java
@Data
public class RegisterRequest {
	private String email;
	private String password;
	private String firstName;
	private String LastName;
	private Role role;
}
