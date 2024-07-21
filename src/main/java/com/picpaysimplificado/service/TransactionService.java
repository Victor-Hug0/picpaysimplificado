package com.picpaysimplificado.service;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dto.TransactionRequestDTO;
import com.picpaysimplificado.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionRequestDTO data) throws Exception {
        User sender = userService.findUserById(data.senderId());
        User receiver = userService.findUserById(data.receiverId());

        userService.validateTransaction(sender, data.value());

        if (this.authorizeTransaction(sender, data.value())) {
            throw new Exception("Transação não autorizada!");
        }

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(data.value());
        transaction.setTransactionDate(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(data.value()));
        receiver.setBalance(receiver.getBalance().add(data.value()));

        userService.saveUser(sender);
        userService.saveUser(receiver);
//        notificationService.sendNotification(sender, "Transação realizada com sucesso!");
//        notificationService.sendNotification(receiver, "Transação recebida com sucesso!");

        return transactionRepository.save(transaction);
    }

    public boolean authorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
        return authorizationResponse.getStatusCode() == HttpStatus.OK && authorizationResponse.getBody().get("status") == "success";
    }
}
