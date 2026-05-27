package betcore.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequest {
    private String name;
    private LocalDateTime startTime;
}
