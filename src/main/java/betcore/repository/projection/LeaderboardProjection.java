package betcore.repository.projection;

import java.math.BigDecimal;

public interface LeaderboardProjection {
    Long getPlayerId();
    String getUsername();
    Long getTotalBets();
    BigDecimal getTotalStaked();
    BigDecimal getTotalWon();
    BigDecimal getNetProfit();
    Long getWins();
    Long getLosses();
}
