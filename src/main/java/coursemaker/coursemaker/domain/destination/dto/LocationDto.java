package coursemaker.coursemaker.domain.destination.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @Schema(description = "도로명 주소", example = "부산광역시 해운대구 우동")
    private String address; // 위치
    @Schema(description = "경도", example = "128.934")
    private BigDecimal longitude; // 경도
    @Schema(description = "위도", example = "35.169")
    private BigDecimal latitude; // 위도
}
