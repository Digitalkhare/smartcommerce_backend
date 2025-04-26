package smartcommerce.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;
    private String subCategory;

    
}

