package betcore.dto;

import betcore.entity.SelectionEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MarketRequest {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotBlank(message = "Market name is required")
    private String name;

    @NotEmpty(message = "At least one selection is required")
    @Valid
    private List<SelectionEntity> selections;
}
