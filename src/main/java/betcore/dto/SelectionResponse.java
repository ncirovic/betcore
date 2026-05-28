package betcore.dto;

import betcore.entity.SelectionEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SelectionResponse {
    private Long id;
    private String name;
    private BigDecimal odds;

    public static SelectionResponse form(SelectionEntity entity) {
        return SelectionResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .odds(entity.getOdds())
                .build();
    }
}
