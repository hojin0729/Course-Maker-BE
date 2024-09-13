package coursemaker.coursemaker.domain.review.dto;

import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RequestDestinationDto {
    @Schema(hidden = true)
    private String nickname;

    @Schema(description = "리뷰 제목", example = "환상적인 여행지!")
    @NotNull(message = "리뷰 제목을 입력하세요.")
    @NotBlank(message = "리뷰 제목은 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String title;

    @Schema(description = "리뷰 설명", example = "이 여행지는 정말 환상적이었습니다. 자연경관이 뛰어나고 즐길 거리가 많아요.")
    @NotNull(message = "리뷰 설명을 입력하세요.")
    @NotBlank(message = "리뷰 설명은 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String description;

    @Schema(description = "리뷰 사진 URL 목록", example = "[\"http://example.com/destination1.jpg\", \"http://example.com/destination2.jpg\"]")
    @NotNull(message = "리뷰 사진 URL을 입력하세요.")
    private List<String> pictures;

    @Schema(description = "평점", example = "4.8")
    @NotNull(message = "평점을 입력하세요.")
    private Double rating;

    public DestinationReview toEntity(Member member) {
        DestinationReview destinationReview = new DestinationReview();
        destinationReview.setMember(member);
        destinationReview.setTitle(this.title);
        destinationReview.setDescription(this.description);
        destinationReview.setPictures(this.pictures);
        destinationReview.setRating(this.rating);
        return destinationReview;
    }
}
