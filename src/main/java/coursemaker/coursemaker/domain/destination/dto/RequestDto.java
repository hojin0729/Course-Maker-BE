package coursemaker.coursemaker.domain.destination.dto;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RequestDto {
    @Schema(description = "유저 닉네임", example = "coursemaker")
    @NotEmpty(message = "닉네임은 비어 있을 수 없습니다.")
    private String nickname; // 유저 이름

    @Schema(description = "여행지 이름", defaultValue = "역시 부산은 해운대!")
    @NotEmpty(message = "여행지 이름은 비어 있을 수 없습니다.")
    private String name; // 여행지 이름

    @Schema(description = "태그 리스트")
    @NotNull(message = "태그 리스트는 비어 있을 수 없습니다.")
    @Size(min = 1, message = "적어도 하나의 태그가 있어야 합니다.")
    private List<TagResponseDto> tags; // 태그 리스트

    @Schema(description = "위치 이름", defaultValue = "해운대")
    @NotEmpty(message = "위치 이름은 비어 있을 수 없습니다.")
    private String location; // 위치 이름

    @Schema(description = "위도", defaultValue = "35.169")
    @NotNull(message = "위도는 비어 있을 수 없습니다.")
    private BigDecimal latitude; // 위도

    @Schema(description = "경도", defaultValue = "128.934")
    @NotNull(message = "경도는 비어 있을 수 없습니다.")
    private BigDecimal longitude; // 경도

    @Schema(description = "대표 사진", defaultValue = "http://example.com/coursemaker.jpg")
    @NotEmpty(message = "대표 사진 링크는 비어 있을 수 없습니다.")
    private String pictureLink; // 대표 사진

    @Schema(description = "텍스트 에디터", defaultValue = "해운대 물이 깨끗하고, 친구들과 여행하기 너무 좋았어요!")
    @NotEmpty(message = "내용은 비어 있을 수 없습니다.")
    private String content; // 텍스트 에디터

    // RequestDto를 Destination 엔티티로 변환하는 메서드
    public Destination toEntity(Member member) {
        Destination destination = new Destination();
        destination.setMember(member);
        destination.setName(this.name);
        destination.setPictureLink(this.pictureLink);
        destination.setContent(this.content);
        destination.setLocation(this.location);
        destination.setLongitude(this.longitude);
        destination.setLatitude(this.latitude);
        return destination;
    }
}
