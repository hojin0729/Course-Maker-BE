package coursemaker.coursemaker.domain.destination.dto;

//import coursemaker.coursemaker.domain.tag.dto.TagDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DestinationDto {
    private String name; // 여행지 이름
   // private List<TagDto> tags; // 태그 리스트
    private String location; // 위치 이름
    private BigDecimal latitude; // 위도
    private BigDecimal longitude; // 경도
    private String pictureLink; // 대표 사진
    private String description; // 텍스트 에디터

}
