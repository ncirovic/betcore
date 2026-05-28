package betcore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SelectionRequest {

    @NotBlank(message = "Selection name is required")
    private String name;

    @NotNull(message = "Odds are required")
    @DecimalMin(value = "1.01", message = "Odds must be at least 1.01")
    private BigDecimal odds;
}
