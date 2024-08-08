package coursemaker.coursemaker.domain.review.dto;

import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestDestinationDto {
    @Schema(hidden = true)
    private String nickname;
    private String title;
    private String description;
    private String picture;
    private Double rating;

    public DestinationReview toEntity(Member member) {
        DestinationReview destinationReview = new DestinationReview();
        destinationReview.setMember(member);
        destinationReview.setTitle(this.title);
        destinationReview.setDescription(this.description);
        destinationReview.setPicture(this.picture);
        destinationReview.setRating(this.rating);
        return destinationReview;
    }
}
