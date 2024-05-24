package coursemaker.coursemaker.domain.destination.dto;

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
    private String location; // 위치
    private BigDecimal longitude; // 경도
    private BigDecimal latitude; // 위도
}
