//package com.financeTracker;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class TransactionController {
//
//    @Autowired
//    private TransactionService transactionService;
//
//    @GetMapping("/transactions")
//    public List<Transaction> getTransactions(@RequestParam Long userId, 
//                                              @RequestParam(required = false) String category,
//                                              @RequestParam(required = false) Integer month) {
//        return transactionService.getTransactions(userId, category, month);
//    }
//}
