package coursemaker.coursemaker.domain.destination.dto;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;

import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DestinationDto {
    private Long id; // 여행지 id
    private String name; // 여행지 이름
    private List<TagResponseDto> tags; // 태그 리스트
    private String location; // 위치 이름
    private BigDecimal latitude; // 위도
    private BigDecimal longitude; // 경도
    private String pictureLink; // 대표 사진
    private String content; // 텍스트 에디터

    // DestinationDto를 Destination 엔티티로 변환하는 메서드
    public static Destination toEntity(DestinationDto dto) {
        Destination destination = new Destination();
        destination.setId(dto.getId());
        destination.setName(dto.getName());
        destination.setPictureLink(dto.getPictureLink());
        destination.setContent(dto.getContent());
        destination.setLocation(dto.getLocation());
        destination.setLongitude(dto.getLongitude());
        destination.setLatitude(dto.getLatitude());
        return destination;
    }

    // Destination 엔티티를 DestinationDto로 변환하는 메서드
    public static DestinationDto toDto(Destination destination, List<TagResponseDto> tagDtos) {
        DestinationDto dto = new DestinationDto();
        dto.setId(destination.getId());
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

