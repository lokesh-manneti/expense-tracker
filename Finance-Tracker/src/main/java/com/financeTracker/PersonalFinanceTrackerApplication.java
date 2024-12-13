package com.financeTracker;

// Personal Finance Tracker Backend - Java Implementation

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.*;

// Spring Boot Application
@SpringBootApplication
public class PersonalFinanceTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PersonalFinanceTrackerApplication.class, args);
    }
}

// Entity: User
@Entity
@Table(name = "users")
@Data
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	
    
    
    // Getters and Setters
    
    // (Omitted for brevity)
}

// Entity: Transaction
@Entity
@Table(name = "transactions")
@Data
class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable=false)
    private long userId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Date transactionDate;

    @Column
    private String description;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    // Getters and Setters
    
    // (Omitted for brevity)
}

// Repository Interfaces
interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameOrEmail(String username, String email);

	Optional<User> findByEmail(String email);
}

//interface TransactionRepository extends JpaRepository<Transaction, Long> {
//    List<Transaction> findByUserUserId(Long userId);
//}


 interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByUserId(Long userId);
	
//    List<Transaction> findByUserAndCategory(User user, String category);
//    List<Transaction> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
//    List<Transaction> findByUserAndCategoryAndDateBetween(User user, String category, LocalDate startDate, LocalDate endDate);
}


////test
//public interface TransactionRepository extends `JpaRepository<Transaction, Long> {
//    List<Transaction> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
//}

//end

// Controller
@RestController
@RequestMapping("/api")
class FinanceController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Register User
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        if (userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        user.setPasswordHash(user.getPasswordHash()); // Hash password (not implemented here)
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
    
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody Map<String, String> loginRequest) {
//        String email = loginRequest.get("email");
//        String password = loginRequest.get("password");
//
//        // Fetch user from the database by email
//        Optional<User> userOptional = userRepository.findByEmail(email);
//
//        if (userOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not found");
//        }
//
//        User user = userOptional.get();
//
//        // Verify password (You should hash the password before checking in production)
//        if (!user.getPasswordHash().equals(password)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
//        }
//
//        return ResponseEntity.ok("Login successful");
//    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        // Fetch user from the database by email
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("message", "Email not found"));
        }

        User user = userOptional.get();

        // Verify password (You should hash the password before checking in production)
        if (!user.getPasswordHash().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("message", "Invalid password"));
        }

        // Return user ID along with the success message
        return ResponseEntity.ok(Map.of(
            "message", "Login successful",
            "userId", user.getUserId()
        ));
    }

    

    // Add Transaction
    @PostMapping("/add-transaction")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction, @RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        transaction.setUserId(userId);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    // Get Transactions
    @GetMapping("/get-transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        if (transactions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactions);
    }

    // Analytics
    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics(@RequestParam Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        if (transactions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        double totalSpent = transactions.stream().mapToDouble(Transaction::getAmount).sum();
        Map<String, Double> categorySummary = new HashMap<>();
        for (Transaction t : transactions) {
            categorySummary.put(t.getCategory(), categorySummary.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
        }

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalSpent", totalSpent);
        analytics.put("categorySummary", categorySummary);
        return ResponseEntity.ok(analytics);
    }
    
    
//    @GetMapping("/total-monthly")
//    public Double getTotalMonthlyExpenditure() {
//        return new E  // Return total expenditure for the current month
//    }
}
