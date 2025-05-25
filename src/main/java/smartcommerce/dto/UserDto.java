package smartcommerce.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String status;
//	public void setStatus(String status) {
//		// TODO Auto-generated method stub
//		
//	}
}
