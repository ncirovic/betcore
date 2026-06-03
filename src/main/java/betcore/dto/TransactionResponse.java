package betcore.dto;

import betcore.entity.TransactionEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private String type;
    private String status;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String description;
    private LocalDateTime createdAt;

    public static TransactionResponse form(TransactionEntity entity) {
        return TransactionResponse.builder()
                .id(entity.getId())
                .type(entity.getType().name())
                .status(entity.getStatus().name())
                .amount(entity.getAmount())
                .balanceBefore(entity.getBalanceBefore())
                .balanceAfter(entity.getBalanceAfter())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
