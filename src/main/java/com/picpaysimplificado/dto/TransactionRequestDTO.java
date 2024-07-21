package com.picpaysimplificado.dto;

import java.math.BigDecimal;

public record TransactionRequestDTO(BigDecimal value, Long senderId, Long receiverId) {
}
