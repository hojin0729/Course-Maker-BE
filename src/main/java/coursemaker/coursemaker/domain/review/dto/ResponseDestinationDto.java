package coursemaker.coursemaker.domain.review.dto;

import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.review.entity.DestinationReview;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResponseDestinationDto {
    private Long destinationId;
    private String nickname;
    private String title;
    private String description;
    private String picture;
    private BigDecimal rating;

    public static ResponseDestinationDto toDto(Destination destination, DestinationReview destinationReview) {
        ResponseDestinationDto dto = new ResponseDestinationDto();
        dto.setDestinationId(destination.getId());
        dto.setNickname(destinationReview.getMember().getNickname());
        dto.setTitle(destinationReview.getTitle());
        dto.setDescription(destinationReview.getDescription());
        dto.setPicture(destinationReview.getPicture());
        dto.setRating(destinationReview.getRating());
        return dto;
    }
}