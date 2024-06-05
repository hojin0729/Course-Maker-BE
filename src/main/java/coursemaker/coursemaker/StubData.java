package coursemaker.coursemaker;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.dto.RequestDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.member.dto.SignUpRequest;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.domain.tag.dto.TagPostDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StubData implements CommandLineRunner {

    private final TagService tagService;
    private final MemberService memberService;
    private final DestinationService destinationService;
    private final CourseService courseService;

    @Autowired
    public StubData(TagService tagService, MemberService memberService, DestinationService destinationService, CourseService courseService) {
        this.tagService = tagService;
        this.memberService = memberService;
        this.destinationService = destinationService;
        this.courseService = courseService;
    }

    public void MemberStubData() throws Exception { // 1 ~ 5번 회원 생성
        // 일반 회원 생성
        SignUpRequest dto;
        for (long i = 1; i <= 5; i++) {
            dto = new SignUpRequest();
            dto.setName("User" + i);
            dto.setEmail("User" + i + "@example.com");
            dto.setNickname("nickname" + i);
            dto.setPassword("password" + i);
            dto.setPhoneNumber("010-0000-000" + i);
            dto.setProfileImgUrl("http://example.com/user" + i + ".jpg");
            dto.setProfileDescription("Profile description for User" + i);
            memberService.signUp(dto);
        }
    }

    public void TagStubData() throws Exception { // 1 ~ 5번 태그 생성
        TagPostDto dto;
        for (long i = 1; i <= 5; i++) {
            dto = new TagPostDto();
            dto.setName("Tag" + i);
            dto.setDescription("Tag Description" + i);
            tagService.createTag(dto.toEntity());
        }
    }

    public void DestinationStubData() throws Exception {
        for (long i = 1; i <= 5; i++) {
            Member member = memberService.findById(i);
            List<Tag> tags = tagService.findAllTags();

            RequestDto dto = new RequestDto();
            dto.setName("Destination" + i);
            dto.setContent("Destination Content" + i);
            dto.setLatitude(BigDecimal.valueOf(11.1234 + i));
            dto.setLongitude(BigDecimal.valueOf(22.2345 + i));
            dto.setLocation("Destination Location" + i);
            dto.setPictureLink("http://example.com/destination" + i + ".jpg");
            dto.setNickname(member.getNickname());
            dto.setTags(tags.stream().map(Tag::toResponseDto).collect(Collectors.toList()));
            destinationService.save(dto);
        }
    }

    private DestinationDto toDestinationDto(Destination destination, List<Tag> tags) {
        DestinationDto dto = new DestinationDto();
        dto.setId(destination.getId());
        dto.setNickname(destination.getMember().getNickname());
        dto.setName(destination.getName());
        dto.setPictureLink(destination.getPictureLink());
        dto.setContent(destination.getContent());
        dto.setLocation(destination.getLocation());
        dto.setLongitude(destination.getLongitude());
        dto.setLatitude(destination.getLatitude());
        dto.setTags(tags.stream().map(Tag::toResponseDto).collect(Collectors.toList()));
        dto.setCreatedAt(destination.getCreatedAt());
        dto.setUpdatedAt(destination.getUpdatedAt());
        return dto;
    }

    public void CourseStubData() throws Exception {
        for (long i = 1; i <= 3; i++) {
            Member member = memberService.findById(i);
            List<Tag> tags = tagService.findAllTags();
            List<Destination> destinations = destinationService.findAll();

            long finalI = i;
            List<AddCourseDestinationRequest> courseDestinations = destinations.stream().map(destination -> {
                AddCourseDestinationRequest courseDestinationRequest = new AddCourseDestinationRequest();
                courseDestinationRequest.setVisitOrder((short) finalI);
                courseDestinationRequest.setDate((short) finalI);
                courseDestinationRequest.setDestination(toDestinationDto(destination, tags));
                return courseDestinationRequest;
            }).collect(Collectors.toList());

            AddTravelCourseRequest travelCourseRequest = new AddTravelCourseRequest();
            travelCourseRequest.setTitle("Course Title" + i);
            travelCourseRequest.setContent("Course Content" + i);
            travelCourseRequest.setDuration((int)i);
            travelCourseRequest.setTravelerCount((int)i);
            travelCourseRequest.setTravelType((int)i);
            travelCourseRequest.setPictureLink("http://example.com/course" + i + ".jpg");
            travelCourseRequest.setNickname(member.getNickname());
            travelCourseRequest.setCourseDestinations(courseDestinations);
            travelCourseRequest.setTags(tags.stream().map(Tag::toResponseDto).collect(Collectors.toList()));

            courseService.save(travelCourseRequest);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        MemberStubData();
        TagStubData();
        DestinationStubData();
        CourseStubData();
    }
}

