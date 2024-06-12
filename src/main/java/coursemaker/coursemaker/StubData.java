package coursemaker.coursemaker;

import coursemaker.coursemaker.domain.course.dto.AddCourseDestinationRequest;
import coursemaker.coursemaker.domain.course.dto.AddTravelCourseRequest;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.dto.LocationDto;
import coursemaker.coursemaker.domain.destination.dto.RequestDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.member.dto.SignUpRequest;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.domain.tag.dto.TagPostDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.exception.TagNotFoundException;
import coursemaker.coursemaker.domain.tag.service.TagService;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
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
            dto.setName("User");
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

        dto = new TagPostDto();
        dto.setName("연인");
        dto.setDescription("연인과 함께 하면 좋은");
        tagService.createTag(dto.toEntity());

        dto = new TagPostDto();
        dto.setName("부모님");
        dto.setDescription("부모님과 함께 하면 좋은");
        tagService.createTag(dto.toEntity());

        dto = new TagPostDto();
        dto.setName("친구");
        dto.setDescription("친구와 함께 하면 좋은");
        tagService.createTag(dto.toEntity());

        dto = new TagPostDto();
        dto.setName("유아동반");
        dto.setDescription("아이들과 함께 하면 좋은");
        tagService.createTag(dto.toEntity());

        dto = new TagPostDto();
        dto.setName("장애인");
        dto.setDescription("장애인과 함께 하면 좋은");
        tagService.createTag(dto.toEntity());
    }

    public void DestinationStubData() throws Exception {

        // ---------- 초기 세팅 ----------

        Member member1 = memberService.findById(1L);
        Member member2 = memberService.findById(2L);
        Member member3 = memberService.findById(3L);
        Member member4 = memberService.findById(4L);
        Member member5 = memberService.findById(5L);

        List<Tag> tags = tagService.findAllTags();
        // 태그 찾기
        Tag coupleTag = tags.stream().filter(tag -> "연인".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "연인 태그"));
        Tag parentTag = tags.stream().filter(tag -> "부모님".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "부모님 태그"));
        Tag friendTag = tags.stream().filter(tag -> "친구".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "친구 태그"));
        Tag childrenTag = tags.stream().filter(tag -> "유아동반".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "유아동반 태그"));
        Tag disabledTag = tags.stream().filter(tag -> "장애인".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "장애우 태그"));

        // ---------- 첫 번째 여행지 ----------

        RequestDto dto1 = new RequestDto();
        dto1.setName("해운대 해수욕장(1)");
        dto1.setContent("부산의 대표적인 해변으로, 아름다운 백사장과 푸른 바다가 인상적입니다.");

        // Location 객체 생성 및 설정
        LocationDto location1 = new LocationDto();
        location1.setLatitude(BigDecimal.valueOf(35.1586975));
        location1.setLongitude(BigDecimal.valueOf(129.1603842));
        location1.setAddress("부산 해운대구 우동 해운대 해수욕장");
        dto1.setLocation(location1);

        dto1.setPictureLink("https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20150831_219%2F1440992305953sjrCF_JPEG%2F157155537056075_0.jpg");
        dto1.setNickname(member1.getNickname());
        dto1.setTags(List.of(disabledTag.toResponseDto(), coupleTag.toResponseDto(), childrenTag.toResponseDto()));
        destinationService.save(dto1);

        // ---------- 두 번째 여행지 ----------

        RequestDto dto2 = new RequestDto();
        dto2.setName("허심청(2)");
        dto2.setContent("동래온천장에 위치한 국내에서 가장 큰 대형 온천 시설로, 다양한 온천탕과 스파를 즐길 수 있습니다.");

        // Location 객체 생성 및 설정
        LocationDto location2 = new LocationDto();
        location2.setLatitude(BigDecimal.valueOf(35.2210076));
        location2.setLongitude(BigDecimal.valueOf(129.0826365));
        location2.setAddress("부산 동래구 온천장로107번길 32");
        dto2.setLocation(location2);

        dto2.setPictureLink("https://www.hotelnongshim.com/kr/_Img/Contents/hsc_gall_img01.jpg");
        dto2.setNickname(member2.getNickname());
        dto2.setTags(List.of(friendTag.toResponseDto(), parentTag.toResponseDto()));
        destinationService.save(dto2);

        // ---------- 세 번째 여행지 ----------

        RequestDto dto3 = new RequestDto();
        dto3.setName("씨라이프부산아쿠아리움(3)");
        dto3.setContent("다양한 해양 생물을 관찰하고 체험할 수 있는 대형 아쿠아리움입니다.");

        // Location 객체 생성 및 설정
        LocationDto location3 = new LocationDto();
        location3.setLatitude(BigDecimal.valueOf(35.159346428095));
        location3.setLongitude(BigDecimal.valueOf(129.16099019727));
        location3.setAddress("부산 해운대구 해운대해변로 266");
        dto3.setLocation(location3);

        dto3.setPictureLink("https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/SEA_LIFE_%EB%B6%80%EC%82%B0%EC%95%84%EC%BF%A0%EC%95%84%EB%A6%AC%EC%9B%80_%EB%A1%9C%EA%B3%A0.png/440px-SEA_LIFE_%EB%B6%80%EC%82%B0%EC%95%84%EC%BF%A0%EC%95%84%EB%A6%AC%EC%9B%80_%EB%A1%9C%EA%B3%A0.png");
        dto3.setNickname(member3.getNickname());
        dto3.setTags(List.of(coupleTag.toResponseDto(), childrenTag.toResponseDto()));
        destinationService.save(dto3);

        // ---------- 네 번째 여행지 ----------

        RequestDto dto4 = new RequestDto();
        dto4.setName("흰여울문화마을(4)");
        dto4.setContent("영도의 절벽 위에 위치한 마을로, 예술 작품과 벽화가 있는 곳입니다. 바다를 배경으로 산책하기에 좋습니다.");

        // Location 객체 생성 및 설정
        LocationDto location4 = new LocationDto();
        location4.setLatitude(BigDecimal.valueOf(35.077909762));
        location4.setLongitude(BigDecimal.valueOf(129.04528782086));
        location4.setAddress("부산 영도구 영선동4가 1044-6");
        dto4.setLocation(location4);

        dto4.setPictureLink("http://www.ydculture.com/wp-content/uploads/2019/08/tourspot03.jpg");
        dto4.setNickname(member4.getNickname());
        dto4.setTags(List.of(coupleTag.toResponseDto(), parentTag.toResponseDto()));
        destinationService.save(dto4);

        // ---------- 다섯 번째 여행지 ----------

        RequestDto dto5 = new RequestDto();
        dto5.setName("송도 해상 케이블카(5)");
        dto5.setContent("송도 해수욕장에서 출발하여 송도 스카이파크까지 연결되는 케이블카입니다. 케이블카를 타고 공중에서 바다를 감상하며 즐길 수 있습니다.");

        // Location 객체 생성 및 설정
        LocationDto location5 = new LocationDto();
        location5.setLatitude(BigDecimal.valueOf(35.076269817));
        location5.setLongitude(BigDecimal.valueOf(129.0236624271));
        location5.setAddress("부산 서구 송도해변로 171");
        dto5.setLocation(location5);

        dto5.setPictureLink("https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220801_106%2F1659333137309JPSiA_JPEG%2F%25BC%25DB%25B5%25B5_%25BD%25E6%25B3%25D7%25C0%25CF_%25C5%25B8%25C0%25CC%25C6%25B2X.jpg");
        dto5.setNickname(member5.getNickname());
        dto5.setTags(List.of(coupleTag.toResponseDto(), childrenTag.toResponseDto(), friendTag.toResponseDto(), parentTag.toResponseDto(), disabledTag.toResponseDto()));
        destinationService.save(dto5);


    }

    private DestinationDto toDestinationDto(Destination destination) {
        List<Tag> destinationTags = tagService.findAllByDestinationId(destination.getId());
        List<TagResponseDto> tagDtos = destinationTags.stream()
                .map(Tag::toResponseDto)
                .collect(Collectors.toList());

        DestinationDto dto = new DestinationDto();
        dto.setId(destination.getId());
        dto.setNickname(destination.getMember().getNickname());
        dto.setName(destination.getName());
        dto.setPictureLink(destination.getPictureLink());
        dto.setContent(destination.getContent());
        dto.setLocation(new LocationDto(destination.getLocation(), destination.getLongitude(), destination.getLatitude()));
        dto.setTags(tagDtos);
        return dto;
    }

    public void CourseStubData() throws Exception {
        Member member1 = memberService.findById(1L);
        Member member2 = memberService.findById(2L);
        Member member3 = memberService.findById(3L);
        Member member4 = memberService.findById(4L);
        Member member5 = memberService.findById(5L);

        List<Tag> tags = tagService.findAllTags();
        // 태그 찾기
        Tag coupleTag = tags.stream().filter(tag -> "연인".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "연인 태그"));
        Tag parentTag = tags.stream().filter(tag -> "부모님".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "부모님 태그"));
        Tag friendTag = tags.stream().filter(tag -> "친구".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "친구 태그"));
        Tag childrenTag = tags.stream().filter(tag -> "유아동반".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "유아동반 태그"));
        Tag disabledTag = tags.stream().filter(tag -> "장애인".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "장애우 태그"));

        List<Destination> destinations = destinationService.findAll();

        // 첫 번째 코스
        List<AddCourseDestinationRequest> courseDestinations1 = List.of(
                createCourseDestinationRequest(destinations.get(0), 1, 1),
                createCourseDestinationRequest(destinations.get(2), 1, 2),
                createCourseDestinationRequest(destinations.get(4), 2, 1)
        );

        AddTravelCourseRequest course1 = new AddTravelCourseRequest();
        course1.setTitle("아이들과 함께 코스(1)");
        course1.setContent("해운대, 아쿠아리움, 송도 해상 케이블카 코스");
        course1.setDuration(2);
        course1.setTravelerCount(4);
        course1.setTravelType(0);
        course1.setPictureLink("https://png.pngtree.com/png-clipart/20190604/original/pngtree-cycling-cartoon-children-png-image_839002.jpg");
        course1.setNickname(member1.getNickname());
        course1.setCourseDestinations(courseDestinations1);
        course1.setTags(List.of(childrenTag.toResponseDto()));
        courseService.save(course1);

        // 두 번째 코스
        List<AddCourseDestinationRequest> courseDestinations2 = List.of(
                createCourseDestinationRequest(destinations.get(0), 1, 1),
                createCourseDestinationRequest(destinations.get(2), 1, 2),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 3, 1)
        );

        AddTravelCourseRequest course2 = new AddTravelCourseRequest();
        course2.setTitle("연인과 함께 코스(2)");
        course2.setContent("해운대, 아쿠아리움, 흰여울 문화마을, 송도 해상 케이블카");
        course2.setDuration(3);
        course2.setTravelerCount(2);
        course2.setTravelType(0);
        course2.setPictureLink("https://png.pngtree.com/png-vector/20210204/ourmid/pngtree-valentines-day-couple-winter-clothing-illustration-png-image_2879396.png");
        course2.setNickname(member2.getNickname());
        course2.setCourseDestinations(courseDestinations2);
        course2.setTags(List.of(coupleTag.toResponseDto()));
        courseService.save(course2);

        // 세 번째 코스
        List<AddCourseDestinationRequest> courseDestinations3 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(4), 1, 2)
        );

        AddTravelCourseRequest course3 = new AddTravelCourseRequest();
        course3.setTitle("친구와 함께 코스(3)");
        course3.setContent("허심청, 송도 해상 케이블카");
        course3.setDuration(1);
        course3.setTravelerCount(3);
        course3.setTravelType(0);
        course3.setPictureLink("https://png.pngtree.com/png-clipart/20190916/ourmid/pngtree-hand-drawn-friends-party-hug-illustration-png-image_1729501.jpg");
        course3.setNickname(member3.getNickname());
        course3.setCourseDestinations(courseDestinations3);
        course3.setTags(List.of(friendTag.toResponseDto()));
        courseService.save(course3);

        // 네 번째 코스
        List<AddCourseDestinationRequest> courseDestinations4 = List.of(
                createCourseDestinationRequest(destinations.get(0), 1, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 1)
        );

        AddTravelCourseRequest course4 = new AddTravelCourseRequest();
        course4.setTitle("장애인과 함께(4)");
        course4.setContent("해운대, 송도 해상 케이블카");
        course4.setDuration(2);
        course4.setTravelerCount(4);
        course4.setTravelType(0);
            course4.setPictureLink("https://img.freepik.com/free-vector/men-and-women-welcoming-people-with-disabilities-group-of-people-meeting-blind-female-character-and-male-in-wheelchair_74855-18436.jpg");
        course4.setNickname(member4.getNickname());
        course4.setCourseDestinations(courseDestinations4);
        course4.setTags(List.of(disabledTag.toResponseDto()));
        courseService.save(course4);

        // 다섯 번째 코스
        List<AddCourseDestinationRequest> courseDestinations5 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );

        AddTravelCourseRequest course5 = new AddTravelCourseRequest();
        course5.setTitle("부모님과 함께(5)");
        course5.setContent("허심청, 흰여울 문화마을, 송도 해상 케이블카");
        course5.setDuration(2);
        course5.setTravelerCount(4);
        course5.setTravelType(0);
        course5.setPictureLink("https://cdn.pixabay.com/photo/2018/02/15/10/49/african-american-3154942_1280.jpg");
        course5.setNickname(member5.getNickname());
        course5.setCourseDestinations(courseDestinations5);
        course5.setTags(List.of(parentTag.toResponseDto()));
        courseService.save(course5);
    }

    private AddCourseDestinationRequest createCourseDestinationRequest(Destination destination, int date, int visitOrder) {
        AddCourseDestinationRequest request = new AddCourseDestinationRequest();
        request.setDate((short) date);
        request.setVisitOrder((short) visitOrder);
        request.setDestination(toDestinationDto(destination));
        return request;
    }


    @Override
    public void run(String... args) throws Exception {
        MemberStubData();
        TagStubData();
        DestinationStubData();
        CourseStubData();
    }
}