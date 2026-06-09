package betcore.dto;

import betcore.entity.BetEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BetResponse {
    private Long id;
    private Long playerId;
    private String playerUsername;
    private Long selectionId;
    private String selectionName;
    private BigDecimal stake;
    private BigDecimal odds;
    private BigDecimal potentialWin;
    private String status;
    private LocalDateTime placedAt;
    private LocalDateTime settledAt;

    public static BetResponse form(BetEntity entity) {
        return BetResponse.builder()
                .id(entity.getId())
                .playerId(entity.getPlayer().getId())
                .playerUsername(entity.getPlayer().getUsername())
                .selectionId(entity.getSelection().getId())
                .selectionName(entity.getSelection().getName())
                .stake(entity.getStake())
                .odds(entity.getOdds())
                .potentialWin(entity.getPotentialWin())
                .status(entity.getStatus().name())
                .placedAt(entity.getPlacedAt())
                .settledAt(entity.getSettledAt())
                .build();
    }
}
