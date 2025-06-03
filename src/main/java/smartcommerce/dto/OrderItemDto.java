package smartcommerce.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private String productName;
    private int quantity;
    private double price;
}
