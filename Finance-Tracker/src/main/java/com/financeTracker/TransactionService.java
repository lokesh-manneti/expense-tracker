//package com.financeTracker;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class TransactionService {
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//    
//    @Autowired
//    private UserRepository userRepository;
//
//    public List<Transaction> getTransactions(Long userId, String category, Integer month) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        LocalDate startDate = null;
//        LocalDate endDate = null;
//
//        // If a month is provided, filter by month
//        if (month != null) {
//            startDate = LocalDate.of(LocalDate.now().getYear(), month, 1);
//            endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
//        }
//
//        // If category is provided, filter by category
//        if (category != null && !category.isEmpty()) {
//            if (month != null) {
//                return transactionRepository.findByUserAndCategoryAndDateBetween(user, category, startDate, endDate);
//            } else {
//                return transactionRepository.findByUserAndCategory(user, category);
//            }
//        } else if (month != null) {
//            return transactionRepository.findByUserAndDateBetween(user, startDate, endDate);
//        } else {
//            return transactionRepository.findByUser(user);  // Fetch all transactions for the user
//        }
//    }
//}
