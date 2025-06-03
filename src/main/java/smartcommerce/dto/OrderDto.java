package smartcommerce.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String status;
    private LocalDateTime orderDateTime;
    private Double totalAmount;
    private List<OrderItemDto> orderItems;
}
