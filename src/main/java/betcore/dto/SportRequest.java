package betcore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SportRequest {

    @NotBlank(message = "Sport name is required")
    @Size(min = 2, max = 50, message = "Sport name must be 2-50 characters long")
    private String name;

    @NotBlank(message = "Sport code is required")
    @Pattern(regexp = "^[A-Z_]+$", message = "Code must be uppercase letters and underscores only")
    @Size(max = 20, message = "Code can be at most 20 characters long")
    private String code;
}
