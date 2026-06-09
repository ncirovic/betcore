package betcore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BetRequest {

    @NotNull(message = "Player ID is required")
    private Long playerId;

    @NotNull(message = "Selection ID is required")
    private Long selectionId;

    @NotNull(message = "Stake is required")
    @DecimalMin(value = "1.00", message = "Minimum stake is 1.00")
    private BigDecimal stake;
}