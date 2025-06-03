package smartcommerce.dto;

import java.util.List;
import java.util.stream.Collectors;

import smartcommerce.model.OrderItem;
import smartcommerce.model.User;


public class UserDtoMapper {

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setStatus(user.isEnabled() ? "Active" : "Inactive");

        dto.setOrders(user.getOrders().stream().map(order -> {
        	 List<OrderItem> items = order.getOrderItems(); // force loading
        	OrderDto o = new OrderDto();
            o.setId(order.getId());
            o.setStatus(order.getStatus());
            o.setOrderDateTime(order.getOrderDateTime());
            o.setTotalAmount(order.getTotalAmount());

            o.setOrderItems(items.stream().map(item -> {
                OrderItemDto i = new OrderItemDto();
                i.setId(item.getId());
                i.setProductName(item.getProduct().getName());
                i.setQuantity(item.getQuantity());
                i.setPrice(item.getPrice());
                return i;
            }).collect(Collectors.toList()));

            return o;
        }).collect(Collectors.toList()));

        return dto;
    }
}
