package betcore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlayerRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters long")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{3,50}$",
            message = "Password must be 3–50 characters and contain at least one uppercase letter, one lowercase letter, and one special character"
    )
    private String password;
}
