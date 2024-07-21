package com.picpaysimplificado.service;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dto.NotificationRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) {
        String email = user.getEmail();
        NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO(message, email);

        ResponseEntity<String> response =  restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", notificationRequestDTO, String.class);

        if (!(response.getStatusCode() == HttpStatus.OK)){
            System.out.println("Error sending notification");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, response.getBody());
        }
    }
}
