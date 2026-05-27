package betcore.dto;

import betcore.entity.SportEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SportResponse {
    private Long id;
    private String name;
    private String code;

    public static SportResponse form(SportEntity entity) {
        return SportResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .build();
    }
}
