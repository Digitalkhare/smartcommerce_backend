package smartcommerce.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private Double price;
	private int stock;
	private String imageUrl;
	
	//@Column(name = "is_featured", nullable = false, columnDefinition = "NUMBER(1,0) DEFAULT 0")
	private boolean featured;
	
	public boolean isFeatured() {
		return featured;
	}
	public void setFeatured(boolean featured) {
	    this.featured = featured;
	}

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	 @ManyToOne
	    @JoinColumn(name = "sub_category_id")
	    private SubCategory subCategory;
	 
	 @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	 @JsonIgnore
	 private List<CartItem> cartItems;

	 @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	 @JsonIgnore
	 private List<OrderItem> orderItems;

	 @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	 @JsonIgnore
	 private List<Review> reviews;
}

