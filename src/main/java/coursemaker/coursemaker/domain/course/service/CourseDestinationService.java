package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.CourseDestinationResponse;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseDestinationService {

    @Autowired
    @Lazy
    private TagService tagService;

    public CourseDestinationResponse toResponse(CourseDestination courseDestination) {
        List<TagResponseDto> tags = tagService.findAllByDestinationId(courseDestination.getDestination().getId())
                .stream()
                .map(Tag::toResponseDto)
                .collect(Collectors.toList());
        DestinationDto destinationDto = DestinationDto.toDto(courseDestination.getDestination(), tags);
        return new CourseDestinationResponse(courseDestination, destinationDto);
    }
}