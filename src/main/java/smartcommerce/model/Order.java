package smartcommerce.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String status; //pending, shipped, delivered
	//private Date orderDate = new Date();
	private LocalDateTime orderDateTime;
	private double totalAmount;
	
	@PrePersist  //autosets time just before persisting
    protected void onCreate() {
	 orderDateTime = LocalDateTime.now();
    }
	
	@ManyToOne
	@JoinColumn(name = "order_user_id")
	//@JsonIgnore
	@JsonBackReference
	private User user;
	
	
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	//@JsonIgnore
	@JsonManagedReference
	private List<OrderItem> orderItems;
	
	public String getUserFirstName() {
	    return user != null ? user.getFirstName() : null;
	}

	
	 
	
}

