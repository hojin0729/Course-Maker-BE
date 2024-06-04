package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TravelCourseResponse {

    private Long id;
    private final String title;
    private final String content;
    private final Integer views;
    private final Integer duration;
    private final Integer travelerCount;
    private final Integer travelType;
    private final String pictureLink;
    private final List<CourseDestinationResponse> courseDestinations;
    private final List<TagResponseDto> courseTags;
    private final CourseMemberResponse member;

    public TravelCourseResponse(TravelCourse travelCourse, List<CourseDestinationResponse> courseDestinationResponses) {
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
        this.courseTags = travelCourse.getCourseTags().stream()
                .map(courseTag -> {
                    TagResponseDto tagResponseDto = new TagResponseDto();
                    tagResponseDto.setId(courseTag.getTag().getId());
                    tagResponseDto.setName(courseTag.getTag().getName());
                    tagResponseDto.setDescription(courseTag.getTag().getDescription());
                    return tagResponseDto;
                })
                .collect(Collectors.toList());
    }
}