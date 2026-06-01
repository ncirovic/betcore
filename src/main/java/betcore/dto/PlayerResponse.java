package betcore.dto;

import betcore.entity.PlayerEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PlayerResponse {
    private Long id;
    private String username;
    private String email;
    private BigDecimal balance;
    private boolean active;
    private LocalDateTime createdAt;

    public static PlayerResponse form(PlayerEntity entity) {
        return PlayerResponse.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .balance(entity.getBalance())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
