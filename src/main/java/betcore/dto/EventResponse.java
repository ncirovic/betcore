package betcore.dto;

import betcore.entity.EventEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String name;
    private LocalDateTime startTime;
    private String status;
    private Long sportId;
    private String sportName;

    public static EventResponse form(EventEntity entity) {
        return EventResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .startTime(entity.getStartTime())
                .status(entity.getStatus().name())
                .sportId(entity.getSport().getId())
                .sportName(entity.getSport().getName())
                .build();
    }
}
