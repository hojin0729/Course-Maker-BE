package coursemaker.coursemaker.domain.review.dto;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class ResponseDestinationDto {
    @Schema(description = "여행지 ID", example = "1")
    @NotNull(message = "여행지 ID를 입력하세요.")
    private Long destinationId;

    @Schema(description = "리뷰의 고유 id", example = "1")
    private Long reviewId;

    @Schema(description = "리뷰 작성자 닉네임", example = "traveler123")
    private String nickname;

    @Schema(description = "리뷰 설명", example = "이 여행지는 정말 환상적이었습니다. 자연경관이 뛰어나고 즐길 거리가 많아요.")
    @NotNull(message = "리뷰 설명을 입력하세요.")
    @NotBlank(message = "리뷰 설명은 공백 혹은 빈 문자는 허용하지 않습니다.")
    private String description;

    @Schema(description = "리뷰 사진 URL 목록", example = "[\"http://example.com/pic1.jpg\", \"http://example.com/pic2.jpg\"]")
    @NotNull(message = "리뷰 사진 URL 목록을 입력하세요.")
    private List<String> pictures;

    @Schema(description = "평점", example = "4.8")
    @NotNull(message = "평점을 입력하세요.")
    private Double rating;

    @Schema(description = "내가 작성한 리뷰인지 여부", example = "true")
    private Boolean isMyDestinationReview;

    @Schema(description = "내가 좋아요를 누른 리뷰인지 여부", example = "true")
    private Boolean isMyLikeReview;

    @Schema(description = "리뷰 좋아요 수", example = "10")
    private Integer recommendCount;

    @Schema(description = "리뷰 작성 날짜", example = "2024-09-14")
    private String reviewedAt;



    public static ResponseDestinationDto toDto(Destination destination, DestinationReview destinationReview, Boolean isMyDestinationReview, Boolean isMyLikeReview) {
        ResponseDestinationDto dto = new ResponseDestinationDto();
        dto.setDestinationId(destination.getId());
        dto.setNickname(destinationReview.getMember().getNickname());
        dto.setDescription(destinationReview.getDescription());
        dto.setPictures(destinationReview.getPictures());
        dto.setRating(destinationReview.getRating());
        dto.setReviewId(destinationReview.getId());
        dto.setIsMyDestinationReview(isMyDestinationReview);
        dto.setIsMyLikeReview(isMyLikeReview);
        dto.setRecommendCount(destinationReview.getRecommendCount());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dto.setReviewedAt(destinationReview.getReviewedAt().format(formatter));
        return dto;
    }
}