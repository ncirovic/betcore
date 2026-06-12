package betcore.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LeaderboardEntry {
    private int rank;
    private Long playerId;
    private String username;
    private long totalBets;
    private BigDecimal totalStaked;
    private BigDecimal totalWon;
    private BigDecimal netProfit;
    private long wins;
    private long losses;
}
