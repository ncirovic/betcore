package betcore.dto;

import betcore.entity.SelectionEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class MarketRequest {

    @NotBlank(message = "Market name is required")
    private String name;

    @NotEmpty(message = "At least one selection is required")
    @Valid
    private List<SelectionEntity> selection;
}
