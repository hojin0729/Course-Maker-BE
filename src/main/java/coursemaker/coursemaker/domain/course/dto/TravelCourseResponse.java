package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "여행 코스 응답 DTO")
@Data
public class TravelCourseResponse {

    @Schema(description = "코스 id")
    private Long id;

    @Schema(description = "코스 타이틀", example = "Course Title1")
    private final String title;

    @Schema(description = "코스 내용", example = "Course Content1")
    private final String content;

    @Schema(description = "코스 조회 수", example = "777")
    private final Integer views;

    @Schema(description = "여행 기간", example = "3")
    private final Integer duration;

    @Schema(description = "여행 인원", example = "5")
    private final Integer travelerCount;

    @Schema(description = "여행 타입, 0이면 자동차, 1이면 대중교통에 해당합니다.", example = "0")
    private final Integer travelType;

    @Schema(description = "코스 대표 이미지 주소", example = "http://example.com/course1.jpg")
    private final String pictureLink;

    @Schema(description = "코스 여행지 목록")
    private final List<CourseDestinationResponse> courseDestinations;

    @Schema(description = "코스 태그 목록")
    private final List<TagResponseDto> tags;

    @Schema(description = "코스 만든 멤버")
    private final CourseMemberResponse member;

    @Schema(description = "해당 코스가 로그인 한 사용자가 작성한 코스인지 여부")
    private final boolean isMine;

    @Schema(description = "코스의 평균 평점", example = "4.5")
    private final Double averageRating;

    @Schema(description = "코스 찜 갯수", example = "70")
    private final Integer wishCount;

    @Schema(description = "코스 리뷰 갯수", example = "60")
    private final Integer reviewCount;

    @Schema(description = "코스 좋아요 갯수", example = "50")
    private final Integer likeCount;

    public TravelCourseResponse(TravelCourse travelCourse, List<CourseDestinationResponse> courseDestinationResponses,
                                List<TagResponseDto> tags, boolean isMine, Double averageRating, Integer reviewCount,
                                Integer wishCount, Integer likeCount) {
        this.id = travelCourse.getId();
        this.title = travelCourse.getTitle();
        this.content = travelCourse.getContent();
        this.views = travelCourse.getViews();
        this.duration = travelCourse.getDuration();
        this.travelerCount = travelCourse.getTravelerCount();
        this.travelType = travelCourse.getTravelType();
        this.pictureLink = travelCourse.getPictureLink();
        this.member = new CourseMemberResponse(travelCourse.getMember());
        this.courseDestinations = courseDestinationResponses;
        this.tags = tags;
        this.isMine = isMine;
        this.averageRating = averageRating;
        this.wishCount = wishCount;
        this.reviewCount = reviewCount;
        this.likeCount = likeCount;
        // TODO: 코스태그 - 코스간에 연관관계를 잘 공부해보세여

        // 코스태그 - 코스간에 연관관계

//        this.courseTags = travelCourse.getCourseTags().stream()
//                .map(courseTag -> {
//                    TagResponseDto tagResponseDto = new TagResponseDto();
//                    tagResponseDto.setId(courseTag.getTag().getId());
//                    tagResponseDto.setName(courseTag.getTag().getName());
//                    tagResponseDto.setDescription(courseTag.getTag().getDescription());
//                    return tagResponseDto;
//                })
//                .collect(Collectors.toList());
    }
}