package coursemaker.coursemaker.domain.destination.dto;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RequestDto {
    private String nickname; // 유저 이름
    private String name; // 여행지 이름
    private List<TagResponseDto> tags; // 태그 리스트
    private String location; // 위치 이름
    private BigDecimal latitude; // 위도
    private BigDecimal longitude; // 경도
    private String pictureLink; // 대표 사진
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
