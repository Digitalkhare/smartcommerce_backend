package smartcommerce.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;
import smartcommerce.model.Cart;
import smartcommerce.model.CartItem;
import smartcommerce.model.Category;
import smartcommerce.model.Order;
import smartcommerce.model.OrderItem;
import smartcommerce.model.Product;
import smartcommerce.model.Review;
import smartcommerce.model.Role;
import smartcommerce.model.SubCategory;
import smartcommerce.model.User;
import smartcommerce.repository.CartItemRepository;
import smartcommerce.repository.CartRepository;
import smartcommerce.repository.CatergoryRepository;
import smartcommerce.repository.OrderItemRepository;
import smartcommerce.repository.OrderRepository;
import smartcommerce.repository.ProductRepository;
import smartcommerce.repository.ReviewRepository;
import smartcommerce.repository.SubCategoryRepository;
import smartcommerce.repository.UserRepository;


//Test subjects to populate database

@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final CatergoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final ReviewRepository reviewRepo;
    private final PasswordEncoder passwordEncoder;
    private final SubCategoryRepository subCategoryRepo;


    private final Random random = new Random();
    private final Faker faker = new Faker();
    private static final Map<String, List<String>> categoryProductNames = Map.of(
            "Electronics", List.of("Wireless Earbuds", "Smartphone", "Bluetooth Speaker", "Laptop", "Fitness Tracker"),
            "Fashion", List.of("Denim Jacket", "Running Shoes", "Silk Scarf", "Graphic Tee", "Leather Belt"),
            "Home & Living", List.of("Coffee Table", "Floor Lamp", "Cushion Set", "Wall Art", "Scented Candle"),
            "Books", List.of("Mystery Novel", "Cookbook", "Science Fiction", "Self Help Guide", "Fantasy Epic"),
            "Sports", List.of("Football", "Yoga Mat", "Tennis Racket", "Running Shorts", "Water Bottle")
        );

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) return; // Avoid reseeding

        // USERS
        List<User> users = new ArrayList<>();
        User admin =  new User();
        admin.setEmail("jane@email.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Jane");
        admin.setLastName("Doe");
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);
        admin.setOrders(new ArrayList<>());
        //users.add(userRepo.save(new User(null, "admin@smart.com", passwordEncoder.encode("admin123"), "Admin", Role.ADMIN, true, new ArrayList<>())));
        users.add(userRepo.save(admin));
        for (int i = 1; i <= 9; i++) {
        	User user = new User();
        	String firstName = faker.name().firstName();
        	String lastName = faker.name().lastName();
        	user.setEmail((firstName+"."+lastName+"@email.com").toLowerCase());
            user.setPassword(passwordEncoder.encode("pass123"));
            user.setFirstName(firstName);
           user.setLastName(lastName);
            user.setRole(Role.USER);
            user.setEnabled(true);
           user.setOrders(new ArrayList<>());
            
            //users.add(userRepo.save(new User(null, "user" + i + "@email.com", passwordEncoder.encode("pass123"), "User " + i, Role.USER, true, new ArrayList<>())));
            users.add(userRepo.save(user));
        }

        // CATEGORIES
        List<Category> categories = new ArrayList<>();
        String[] catNames = {"Electronics", "Fashion", "Home & Living", "Books", "Sports"};

        for (String name : catNames) {
            Category category = new Category();
            category.setName(name);
            category.setProducts(new ArrayList<>()); // Only if needed based on your entity
            categories.add(categoryRepo.save(category));
        }

        
     // Create subcategories for Fashion
        List<SubCategory> fashionSubs = new ArrayList<>();
        Category fashionCategory = categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase("Fashion"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Fashion category not found"));

        List<String> fashionSubNames = List.of("Men", "Women", "Boys", "Girls");

        for (String subName : fashionSubNames) {
            SubCategory sub = new SubCategory();
            sub.setName(subName);
            sub.setCategory(fashionCategory);
            fashionSubs.add(subCategoryRepo.save(sub));
        }

     // PRODUCTS
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Category category = categories.get(random.nextInt(categories.size()));
            Product product = new Product();

            List<String> namePool = categoryProductNames.get(category.getName());
            String name = namePool.get(random.nextInt(namePool.size()));
            product.setName(name);

            String description = switch (category.getName()) {
                case "Electronics" -> "Top-of-the-line gadget with cutting-edge features.";
                case "Fashion" -> "Stylish and trendy — perfect for any occasion.";
                case "Books" -> "Engaging and insightful read from a popular author.";
                case "Home & Living" -> "Adds comfort and style to your living space.";
                case "Sports" -> "Ideal gear for training and performance.";
                default -> faker.lorem().sentence();
            };

            product.setDescription(description);
            product.setPrice(Double.parseDouble(faker.commerce().price(10.0, 500.0)));
            product.setStock(10 + random.nextInt(100));
            product.setImageUrl("https://placehold.co/200x150?text=" + category.getName().replace(" ", "+") + "+" + i);
            product.setCategory(category);
            product.setFeatured(random.nextBoolean() && random.nextInt(3) == 0);

            if (category.getName().equalsIgnoreCase("Fashion")) {
                product.setSubCategory(fashionSubs.get(random.nextInt(fashionSubs.size())));
            }

            products.add(productRepo.save(product));
        }

//        for (Product p : products) { // randomly select feature product
//        	boolean featured = random.nextBoolean() && random.nextInt(3) == 0;
//            p.setFeatured(featured);
//            System.out.printf("Product %d: featured = %b\n", p.getId(), featured);
//            productRepo.save(p);
//        }
       // productRepo.saveAll(products);
       // productRepo.flush();
        
//        Product p = productRepo.findById(1L).get();
//        p.setFeatured(true);
//        productRepo.save(p);
//        productRepo.flush();

        // CARTS & CART ITEMS (for 6 users)
        for (int i = 1; i <= 6; i++) {
            User cartUser = users.get(i);
            Cart cart = cartRepo.save(new Cart(null, cartUser, new ArrayList<>()));

            for (int j = 0; j < 2 + random.nextInt(3); j++) {
                Product product = products.get(random.nextInt(products.size()));
                CartItem item = new CartItem(null, random.nextInt(1, 4), product, cart);
                cartItemRepo.save(item);
            }
        }

        // ORDERS (for all users except admin)
        for (int i = 1; i <= 30; i++) {
            User orderUser = users.get(random.nextInt(1, users.size())); // exclude admin
            Order order = new Order();
            order.setUser(orderUser);
            order.setOrderDateTime(randomDate());
            order.setStatus(randomOrderStatus());
            order.setTotalAmount(0);
            order = orderRepo.save(order);

            int itemCount = 1 + random.nextInt(4);
            double orderTotal = 0;

            for (int j = 0; j < itemCount; j++) {
                Product product = products.get(random.nextInt(products.size()));
                int qty = 1 + random.nextInt(2);
                double price = product.getPrice();
                
            
                OrderItem item = new OrderItem();
                item.setQuantity(qty);
                item.setPrice(price);
                item.setProduct(product);
                item.setOrder(order);
               
               // OrderItem item = new OrderItem(null, qty, price, product, order);
                orderItemRepo.save(item);
                orderTotal += price * qty;
            }

            order.setTotalAmount(orderTotal);
            orderRepo.save(order);
        }

        // REVIEWS
        Set<String> userProductPairs = new HashSet<>();
        int reviewsCreated = 0;
        while (reviewsCreated < 100) {
            User user = users.get(random.nextInt(1, users.size()));
            Product product = products.get(random.nextInt(products.size()));
            String key = user.getId() + ":" + product.getId();

            if (userProductPairs.contains(key)) continue;

            int rating = 1 + random.nextInt(5);
            String comment = switch (rating) {
                case 5 -> "Absolutely love it! " + faker.lorem().sentence();
                case 4 -> "Pretty good! " + faker.lorem().sentence();
                case 3 -> "Average. " + faker.lorem().sentence();
                case 2 -> "Not great. " + faker.lorem().sentence();
                default -> "Would not recommend. " + faker.lorem().sentence();
            };

            Review review = new Review(null, rating, comment, randomDate(), product, user);
            reviewRepo.save(review);

            userProductPairs.add(key);
            reviewsCreated++;
        }

        System.out.printf("\n✅ Seeded: %d Users, %d Categories, %d Products, %d Carts, %d Orders, %d Reviews\n",
                users.size(), categories.size(), products.size(), cartRepo.count(), orderRepo.count(), reviewRepo.count());
    }

    //use localDateTime instead
//    private Date randomDate() {
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.DAY_OF_YEAR, -random.nextInt(180)); // Past 6 months
//        return c.getTime();
//    }

    private LocalDateTime randomDate() {
       // return LocalDateTime.now().minusDays(random.nextInt(180));
        return LocalDateTime.now()
                .minusDays(random.nextInt(180))
                .minusHours(random.nextInt(24))
                .minusMinutes(random.nextInt(60));
    }
    private String randomOrderStatus() {
        return List.of("PENDING", "SHIPPED", "DELIVERED", "CANCELLED").get(random.nextInt(4));
    }
}

