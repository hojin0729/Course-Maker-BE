package coursemaker.coursemaker.domain.destination.dto;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.math.BigDecimal;
import java.util.List;

@Data
public class DestinationDto {
    @Schema(description = "여행지 Id", example = "1")
    private Long id;
    @Schema(description = "유저 닉네임", example = "coursemaker")
    private String nickname; // 유저 이름
    @Schema(description = "여행지 이름", defaultValue = "역시 부산은 해운대!")
    private String name; // 여행지 이름
    @Schema(description = "태그 리스트")
    private List<TagResponseDto> tags; // 태그 리스트
    @Schema(description = "위치 이름", defaultValue = "해운대")
    private String location; // 위치 이름
    @Schema(description = "위도", defaultValue = "35.169")
    private BigDecimal latitude; // 위도
    @Schema(description = "경도", defaultValue = "128.934")
    private BigDecimal longitude; // 경도
    @Schema(description = "대표 사진", defaultValue = "http://example.com/coursemaker.jpg")
    private String pictureLink; // 대표 사진
    @Schema(description = "텍스트 에디터", defaultValue = "해운대 물이 깨끗하고, 친구들과 여행하기 너무 좋았어요!")
    private String content; // 텍스트 에디터


    // Destination 엔티티를 DestinationDto로 변환하는 메서드
    public static DestinationDto toDto(Destination destination, List<TagResponseDto> tagDtos) {
        DestinationDto dto = new DestinationDto();
        dto.setId(destination.getId());
        dto.setNickname(destination.getMember().getNickname()); // 누가 만들었는지 설정
        dto.setName(destination.getName());
        dto.setPictureLink(destination.getPictureLink());
        dto.setContent(destination.getContent());
        dto.setLocation(destination.getLocation());
        dto.setLongitude(destination.getLongitude());
        dto.setLatitude(destination.getLatitude());
        dto.setTags(tagDtos);
        return dto;
    }
}

