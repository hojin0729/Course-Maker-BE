package coursemaker.coursemaker.domain.destination.dto;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DestinationDto {
    @Schema(description = "여행지 Id", example = "1")
    @NotNull(message = "여행지 ID를 입력해주세요.")
    private Long id;

    @Schema(description = "유저 닉네임", example = "coursemaker")
    private String nickname; // 유저 이름
//      Todo: 고도화때 예정
//    @Schema(description = "회원 등급", example = "준회원")
//    private String roles;

    @Schema(description = "여행지 이름", defaultValue = "역시 부산은 해운대!")
    @NotNull(message = "여행지 이름을 입력하세요.")
    @NotBlank(message = "여행지 이름은 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String name; // 여행지 이름

    @Schema(description = "태그 리스트")
    @NotNull(message = "태그 리스트는 비어 있을 수 없습니다.")
    @Size(min = 1, message = "적어도 하나의 태그가 있어야 합니다.")
    private List<@Valid TagResponseDto> tags; // 태그 리스트

    @Schema(description = "위치 정보")
    @NotNull(message = "위치 정보는 비어 있을 수 없습니다.")
    @Valid
    private LocationDto location; // 위치

    @Schema(description = "대표 사진", defaultValue = "http://example.com/coursemaker.jpg")
    @NotNull(message = "대표 사진 링크를 입력하세요.")
    @NotBlank(message = "대표 사진 링크는 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String pictureLink; // 대표 사진

    @Schema(description = "텍스트 에디터", defaultValue = "해운대 물이 깨끗하고, 친구들과 여행하기 너무 좋았어요!")
    @NotNull(message = "내용을 입력하세요.")
    @NotBlank(message = "내용은 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String content; // 텍스트 에디터


    // Destination 엔티티를 DestinationDto로 변환하는 메서드
    public static DestinationDto toDto(Destination destination, List<TagResponseDto> tagDtos) {
        DestinationDto dto = new DestinationDto();
        dto.setId(destination.getId());
        dto.setNickname(destination.getMember().getNickname()); // 누가 만들었는지 설정
//        dto.setRoles(destination.getMember().getRoles());
        dto.setName(destination.getName());
        dto.setPictureLink(destination.getPictureLink());
        dto.setContent(destination.getContent());
        dto.setTags(tagDtos);
        LocationDto location = new LocationDto(
                destination.getLocation(),
                destination.getLongitude(),
                destination.getLatitude()
        );
        dto.setLocation(location);
        return dto;
    }
}

