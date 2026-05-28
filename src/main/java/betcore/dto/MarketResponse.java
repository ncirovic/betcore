package betcore.dto;

import betcore.entity.MarketEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MarketResponse {
    private Long id;
    private String name;
    private Long eventId;
    private String eventName;
    private List<SelectionResponse> selections;

    public static MarketResponse form(MarketEntity entity) {
        return MarketResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .eventId(entity.getEvent().getId())
                .eventName(entity.getEvent().getName())
                .selections(entity.getSelections().stream()
                        .map(SelectionResponse::form)
                        .toList()
                ).build();
    }
}
