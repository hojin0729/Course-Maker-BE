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
//            dto.setProfileImgUrl("http://example.com/user" + i + ".jpg");
//            dto.setProfileDescription("Profile description for User" + i);
            memberService.signUp(dto);
        }
    }

    public void TagStubData() throws Exception { // 1 ~ 5번 태그 생성
        TagPostDto dto;

        //  자연

        dto = new TagPostDto();
        dto.setName("바다");
        dto.setDescription("자연");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("자연속휴식");
        dto.setDescription("자연");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("등산");
        dto.setDescription("자연");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("둘레길");
        dto.setDescription("자연");
        tagService.createTag(dto);

        // 동행

        dto = new TagPostDto();
        dto.setName("나홀로 여행");
        dto.setDescription("동행");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("유아동반");
        dto.setDescription("동행");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("장애인");
        dto.setDescription("동행");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("어린이여행");
        dto.setDescription("동행");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("부모님");
        dto.setDescription("동행");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("연인");
        dto.setDescription("동행");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("배우자");
        dto.setDescription("동행");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("친구");
        dto.setDescription("동행");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("애견동반");
        dto.setDescription("동행");
        tagService.createTag(dto);

        // 활동

        dto = new TagPostDto();
        dto.setName("액티비티");
        dto.setDescription("활동");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("문화거리");
        dto.setDescription("활동");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("축제");
        dto.setDescription("활동");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("체험여행");
        dto.setDescription("활동");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("힐링");
        dto.setDescription("활동");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("문화/유적지");
        dto.setDescription("활동");
        tagService.createTag(dto);

        // 음식

        dto = new TagPostDto();
        dto.setName("식도락");
        dto.setDescription("음식");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("전통시장");
        dto.setDescription("음식");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("방송 맛집");
        dto.setDescription("음식");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("빵지순례");
        dto.setDescription("음식");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("카페투어");
        dto.setDescription("음식");
        tagService.createTag(dto);

        // 날씨

        dto = new TagPostDto();
        dto.setName("우천시 운영");
        dto.setDescription("날씨");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("실내공간");
        dto.setDescription("날씨");
        tagService.createTag(dto);

        dto = new TagPostDto();
        dto.setName("피서지");
        dto.setDescription("날씨");
        tagService.createTag(dto);

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

        // 자연
        Tag seaTag = tags.stream().filter(tag -> "바다".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "바다 태그"));
        Tag natureTag = tags.stream().filter(tag -> "자연속휴식".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "자연속휴식 태그"));
        Tag hikingTag = tags.stream().filter(tag -> "등산".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "등산 태그"));
        Tag walkingTag = tags.stream().filter(tag -> "둘레길".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "둘레길 태그"));

        // 동행
        Tag aloneTag = tags.stream().filter(tag -> "나홀로 여행".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "나홀로 여행 태그"));
        Tag withChildTag = tags.stream().filter(tag -> "유아동반".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "유아동반 태그"));
        Tag disabledTag = tags.stream().filter(tag -> "장애인".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "장애인 태그"));
        Tag ChildrenTag = tags.stream().filter(tag -> "어린이여행".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "어린이여행 태그"));
        Tag parentTag = tags.stream().filter(tag -> "부모님".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "부모님 태그"));
        Tag coupleTag = tags.stream().filter(tag -> "연인".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "연인 태그"));
        Tag withHoneyTag = tags.stream().filter(tag -> "배우자".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "배우자 태그"));
        Tag friendTag = tags.stream().filter(tag -> "친구".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "친구 태그"));
        Tag PetTag = tags.stream().filter(tag -> "애견동반".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "애견동반 태그"));


        // 활동
        Tag activityTag = tags.stream().filter(tag -> "액티비티".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "액티비티 태그"));
        Tag cultureStreetTag = tags.stream().filter(tag -> "문화거리".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "문화거리 태그"));
        Tag festivalTag = tags.stream().filter(tag -> "축제".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "축제 태그"));
        Tag experienceTag = tags.stream().filter(tag -> "체험여행".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "체험여행 태그"));
        Tag healingTag = tags.stream().filter(tag -> "힐링".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "힐링 태그"));
        Tag historyTag = tags.stream().filter(tag -> "문화/유적지".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "문화/유적지 태그"));


        // 음식
        Tag foodTripTag = tags.stream().filter(tag -> "식도락".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "식도락 태그"));
        Tag TraditionalTag = tags.stream().filter(tag -> "전통시장".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "전통시장 태그"));
        Tag famousRestaurantTag = tags.stream().filter(tag -> "방송 맛집".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "방송 맛집 태그"));
        Tag breadTag = tags.stream().filter(tag -> "빵지순례".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "빵지순례 태그"));
        Tag cafeTag = tags.stream().filter(tag -> "카페투어".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "카페투어 태그"));


        // 날씨
        Tag rainTag = tags.stream().filter(tag -> "우천시 운영".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "우천시 운영 태그"));
        Tag indoorTag = tags.stream().filter(tag -> "실내공간".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "실내공간 태그"));
        Tag summerResortTag = tags.stream().filter(tag -> "피서지".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "피서지 태그"));

        // ---------- 1 번째 여행지 ----------

        RequestDto dto1 = new RequestDto();
        dto1.setName("해운대 해수욕장");
        dto1.setContent("부산의 대표적인 해변으로, 아름다운 백사장과 푸른 바다가 인상적입니다.");

        // Location 객체 생성 및 설정
        LocationDto location1 = new LocationDto();
        location1.setLatitude(BigDecimal.valueOf(35.1586975));
        location1.setLongitude(BigDecimal.valueOf(129.1603842));
        location1.setAddress("부산 해운대구 우동 해운대 해수욕장");
        dto1.setLocation(location1);

        dto1.setPictureLink("https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20150831_219%2F1440992305953sjrCF_JPEG%2F157155537056075_0.jpg");
        dto1.setNickname(member1.getNickname());
        dto1.setTags(List.of(seaTag.toResponseDto(), disabledTag.toResponseDto(), PetTag.toResponseDto(), friendTag.toResponseDto(), aloneTag.toResponseDto(), coupleTag.toResponseDto(), withChildTag.toResponseDto(), summerResortTag.toResponseDto()));
        destinationService.save(dto1);

        // ---------- 2 번째 여행지 ----------

        RequestDto dto2 = new RequestDto();
        dto2.setName("허심청");
        dto2.setContent("동래온천장에 위치한 국내에서 가장 큰 대형 온천 시설로, 다양한 온천탕과 스파를 즐길 수 있습니다.");

        // Location 객체 생성 및 설정
        LocationDto location2 = new LocationDto();
        location2.setLatitude(BigDecimal.valueOf(35.2210076));
        location2.setLongitude(BigDecimal.valueOf(129.0826365));
        location2.setAddress("부산 동래구 온천장로107번길 32");
        dto2.setLocation(location2);

        dto2.setPictureLink("https://www.hotelnongshim.com/kr/_Img/Contents/hsc_gall_img01.jpg");
        dto2.setNickname(member2.getNickname());
        dto2.setTags(List.of(friendTag.toResponseDto(), parentTag.toResponseDto(), aloneTag.toResponseDto(), indoorTag.toResponseDto()));
        destinationService.save(dto2);

        // ---------- 3 번째 여행지 ----------

        RequestDto dto3 = new RequestDto();
        dto3.setName("씨라이프부산아쿠아리움");
        dto3.setContent("다양한 해양 생물을 관찰하고 체험할 수 있는 대형 아쿠아리움입니다.");

        // Location 객체 생성 및 설정
        LocationDto location3 = new LocationDto();
        location3.setLatitude(BigDecimal.valueOf(35.159346428095));
        location3.setLongitude(BigDecimal.valueOf(129.16099019727));
        location3.setAddress("부산 해운대구 해운대해변로 266");
        dto3.setLocation(location3);

        dto3.setPictureLink("https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/SEA_LIFE_%EB%B6%80%EC%82%B0%EC%95%84%EC%BF%A0%EC%95%84%EB%A6%AC%EC%9B%80_%EB%A1%9C%EA%B3%A0.png/440px-SEA_LIFE_%EB%B6%80%EC%82%B0%EC%95%84%EC%BF%A0%EC%95%84%EB%A6%AC%EC%9B%80_%EB%A1%9C%EA%B3%A0.png");
        dto3.setNickname(member3.getNickname());
        dto3.setTags(List.of(coupleTag.toResponseDto(), ChildrenTag.toResponseDto(), withChildTag.toResponseDto(), rainTag.toResponseDto(), indoorTag.toResponseDto()));
        destinationService.save(dto3);

        // ---------- 4 번째 여행지 ----------

        RequestDto dto4 = new RequestDto();
        dto4.setName("흰여울문화마을");
        dto4.setContent("영도의 절벽 위에 위치한 마을로, 예술 작품과 벽화가 있는 곳입니다. 바다를 배경으로 산책하기에 좋습니다.");

        // Location 객체 생성 및 설정
        LocationDto location4 = new LocationDto();
        location4.setLatitude(BigDecimal.valueOf(35.077909762));
        location4.setLongitude(BigDecimal.valueOf(129.04528782086));
        location4.setAddress("부산 영도구 영선동4가 1044-6");
        dto4.setLocation(location4);

        dto4.setPictureLink("http://www.ydculture.com/wp-content/uploads/2019/08/tourspot03.jpg");
        dto4.setNickname(member4.getNickname());
        dto4.setTags(List.of(coupleTag.toResponseDto(), cafeTag.toResponseDto(), breadTag.toResponseDto(), cultureStreetTag.toResponseDto(), parentTag.toResponseDto(), PetTag.toResponseDto(), walkingTag.toResponseDto(), seaTag.toResponseDto(), healingTag.toResponseDto(), withHoneyTag.toResponseDto()));
        destinationService.save(dto4);

        // ---------- 5 번째 여행지 ----------

        RequestDto dto5 = new RequestDto();
        dto5.setName("송도 해상 케이블카");
        dto5.setContent("송도 해수욕장에서 출발하여 송도 스카이파크까지 연결되는 케이블카입니다. 케이블카를 타고 공중에서 바다를 감상하며 즐길 수 있습니다.");

        // Location 객체 생성 및 설정
        LocationDto location5 = new LocationDto();
        location5.setLatitude(BigDecimal.valueOf(35.076269817));
        location5.setLongitude(BigDecimal.valueOf(129.0236624271));
        location5.setAddress("부산 서구 송도해변로 171");
        dto5.setLocation(location5);

        dto5.setPictureLink("https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220801_106%2F1659333137309JPSiA_JPEG%2F%25BC%25DB%25B5%25B5_%25BD%25E6%25B3%25D7%25C0%25CF_%25C5%25B8%25C0%25CC%25C6%25B2X.jpg");
        dto5.setNickname(member5.getNickname());
        dto5.setTags(List.of(seaTag.toResponseDto(), coupleTag.toResponseDto(), withChildTag.toResponseDto(), friendTag.toResponseDto(), parentTag.toResponseDto(), disabledTag.toResponseDto()));
        destinationService.save(dto5);

        // ---------- 6 번째 여행지 ----------

        RequestDto dto6 = new RequestDto();
        dto6.setName("부산 현대미술관");
        dto6.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location6 = new LocationDto();
        location6.setLatitude(BigDecimal.valueOf(35.076269817));
        location6.setLongitude(BigDecimal.valueOf(129.0236624271));
        location6.setAddress("부산광역시 사하구 낙동남로 1191");
        dto6.setLocation(location6);

        dto6.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20230707174450394");
        dto6.setNickname(member5.getNickname());
        dto6.setTags(List.of(coupleTag.toResponseDto(), withHoneyTag.toResponseDto(), friendTag.toResponseDto(), historyTag.toResponseDto()));
        destinationService.save(dto6);

        // ---------- 7 번째 여행지 ----------

        RequestDto dto7 = new RequestDto();
        dto7.setName("전포 공구길");
        dto7.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location7 = new LocationDto();
        location7.setLatitude(BigDecimal.valueOf(35.076269817));
        location7.setLongitude(BigDecimal.valueOf(129.0236624271));
        location7.setAddress("부산광역시 부산진구 서전로37번길 20");
        dto7.setLocation(location7);

        dto7.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20220921114402310");
        dto7.setNickname(member5.getNickname());
        dto7.setTags(List.of(walkingTag.toResponseDto(), aloneTag.toResponseDto(), PetTag.toResponseDto(), withHoneyTag.toResponseDto(), friendTag.toResponseDto(), breadTag.toResponseDto(), cafeTag.toResponseDto(), coupleTag.toResponseDto()));
        destinationService.save(dto7);

        // ---------- 8 번째 여행지 ----------

        RequestDto dto8 = new RequestDto();
        dto8.setName("감전 야생화단지");
        dto8.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location8 = new LocationDto();
        location8.setLatitude(BigDecimal.valueOf(35.076269817));
        location8.setLongitude(BigDecimal.valueOf(129.0236624271));
        location8.setAddress("부산광역시 사상구 감전동 873");
        dto8.setLocation(location8);

        dto8.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20230723172025932");
        dto8.setNickname(member5.getNickname());
        dto8.setTags(List.of(natureTag.toResponseDto(), withHoneyTag.toResponseDto(), coupleTag.toResponseDto(), PetTag.toResponseDto(), healingTag.toResponseDto()));
        destinationService.save(dto8);

        // ---------- 9 번째 여행지 ----------

        RequestDto dto9 = new RequestDto();
        dto9.setName("해운대 수목원");
        dto9.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location9 = new LocationDto();
        location9.setLatitude(BigDecimal.valueOf(35.076269817));
        location9.setLongitude(BigDecimal.valueOf(129.0236624271));
        location9.setAddress("부산광역시 해운대구 석대동 77");
        dto9.setLocation(location9);

        dto9.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20230202161355598");
        dto9.setNickname(member5.getNickname());
        dto9.setTags(List.of(natureTag.toResponseDto(), withChildTag.toResponseDto(), parentTag.toResponseDto(), PetTag.toResponseDto(), withHoneyTag.toResponseDto(), coupleTag.toResponseDto(), healingTag.toResponseDto()));
        destinationService.save(dto9);

        // ---------- 10 번째 여행지 ----------

        RequestDto dto10 = new RequestDto();
        dto10.setName("산평소공원");
        dto10.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location10 = new LocationDto();
        location10.setLatitude(BigDecimal.valueOf(35.076269817));
        location10.setLongitude(BigDecimal.valueOf(129.0236624271));
        location10.setAddress("부산광역시 기장군 일광면 신평리 11-1");
        dto10.setLocation(location10);

        dto10.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20200514115750295");
        dto10.setNickname(member5.getNickname());
        dto10.setTags(List.of(seaTag.toResponseDto(), PetTag.toResponseDto(), ChildrenTag.toResponseDto(), coupleTag.toResponseDto()));
        destinationService.save(dto10);

        // ---------- 11 번째 여행지 ----------

        RequestDto dto11 = new RequestDto();
        dto11.setName("영도대교");
        dto11.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location11 = new LocationDto();
        location11.setLatitude(BigDecimal.valueOf(35.076269817));
        location11.setLongitude(BigDecimal.valueOf(129.0236624271));
        location11.setAddress("부산광역시 영도구 태종로 46");
        dto11.setLocation(location11);

        dto11.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191229150331682");
        dto11.setNickname(member5.getNickname());
        dto11.setTags(List.of(seaTag.toResponseDto(), withChildTag.toResponseDto(), parentTag.toResponseDto(), friendTag.toResponseDto(), festivalTag.toResponseDto()));
        destinationService.save(dto11);

        // ---------- 12 번째 여행지 ----------

        RequestDto dto12 = new RequestDto();
        dto12.setName("가덕도");
        dto12.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location12 = new LocationDto();
        location12.setLatitude(BigDecimal.valueOf(35.076269817));
        location12.setLongitude(BigDecimal.valueOf(129.0236624271));
        location12.setAddress("부산광역시 강서구 가덕해안로 21");
        dto12.setLocation(location12);

        dto12.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191227161822663");
        dto12.setNickname(member5.getNickname());
        dto12.setTags(List.of(seaTag.toResponseDto(), parentTag.toResponseDto(), withHoneyTag.toResponseDto(), healingTag.toResponseDto()));
        destinationService.save(dto12);

        // ---------- 13 번째 여행지 ----------

        RequestDto dto13 = new RequestDto();
        dto13.setName("국립부산과학관");
        dto13.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location13 = new LocationDto();
        location13.setLatitude(BigDecimal.valueOf(35.076269817));
        location13.setLongitude(BigDecimal.valueOf(129.0236624271));
        location13.setAddress("부산광역시 기장군 기장읍 동부산관광6로 59");
        dto13.setLocation(location13);

        dto13.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20230901132543063");
        dto13.setNickname(member5.getNickname());
        dto13.setTags(List.of(ChildrenTag.toResponseDto(), friendTag.toResponseDto(), experienceTag.toResponseDto(), rainTag.toResponseDto(), indoorTag.toResponseDto()));
        destinationService.save(dto13);

        // ---------- 14 번째 여행지 ----------

        RequestDto dto14 = new RequestDto();
        dto14.setName("백양산");
        dto14.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location14 = new LocationDto();
        location14.setLatitude(BigDecimal.valueOf(35.076269817));
        location14.setLongitude(BigDecimal.valueOf(129.0236624271));
        location14.setAddress("부산광역시 부산진구 당감동");
        dto14.setLocation(location14);

        dto14.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191231092729834");
        dto14.setNickname(member5.getNickname());
        dto14.setTags(List.of(natureTag.toResponseDto(), hikingTag.toResponseDto(), aloneTag.toResponseDto(), friendTag.toResponseDto(), activityTag.toResponseDto()));
        destinationService.save(dto14);

        // ---------- 15 번째 여행지 ----------

        RequestDto dto15 = new RequestDto();
        dto15.setName("벡스코(BEXCO)");
        dto15.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location15 = new LocationDto();
        location15.setLatitude(BigDecimal.valueOf(35.076269817));
        location15.setLongitude(BigDecimal.valueOf(129.0236624271));
        location15.setAddress("부산광역시 해운대구 APEC로 55");
        dto15.setLocation(location15);

        dto15.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191230210019945");
        dto15.setNickname(member5.getNickname());
        dto15.setTags(List.of(aloneTag.toResponseDto(), withChildTag.toResponseDto(), disabledTag.toResponseDto(), ChildrenTag.toResponseDto(), coupleTag.toResponseDto(), withHoneyTag.toResponseDto(), friendTag.toResponseDto(), activityTag.toResponseDto(), festivalTag.toResponseDto(), experienceTag.toResponseDto(), rainTag.toResponseDto(), indoorTag.toResponseDto()));
        destinationService.save(dto15);

        // ---------- 16 번째 여행지 ----------

        RequestDto dto16 = new RequestDto();
        dto16.setName("성지곡수원지");
        dto16.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location16 = new LocationDto();
        location16.setLatitude(BigDecimal.valueOf(35.076269817));
        location16.setLongitude(BigDecimal.valueOf(129.0236624271));
        location16.setAddress("부산광역시 부산진구 새싹로 295");
        dto16.setLocation(location16);

        dto16.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191231100856743");
        dto16.setNickname(member5.getNickname());
        dto16.setTags(List.of(natureTag.toResponseDto(), hikingTag.toResponseDto(), walkingTag.toResponseDto(), coupleTag.toResponseDto(), friendTag.toResponseDto(), PetTag.toResponseDto()));
        destinationService.save(dto16);

        // ---------- 17 번째 여행지 ----------

        RequestDto dto17 = new RequestDto();
        dto17.setName("아미동 비석문화마을");
        dto17.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location17 = new LocationDto();
        location17.setLatitude(BigDecimal.valueOf(35.076269817));
        location17.setLongitude(BigDecimal.valueOf(129.0236624271));
        location17.setAddress("부산광역시 서구 아미로49");
        dto17.setLocation(location17);

        dto17.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191226093554532");
        dto17.setNickname(member5.getNickname());
        dto17.setTags(List.of(walkingTag.toResponseDto(), aloneTag.toResponseDto(), withHoneyTag.toResponseDto(), coupleTag.toResponseDto(), PetTag.toResponseDto(), cultureStreetTag.toResponseDto(), historyTag.toResponseDto()));
        destinationService.save(dto17);

        // ---------- 18 번째 여행지 ----------

        RequestDto dto18 = new RequestDto();
        dto18.setName("오랑대공원");
        dto18.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location18 = new LocationDto();
        location18.setLatitude(BigDecimal.valueOf(35.076269817));
        location18.setLongitude(BigDecimal.valueOf(129.0236624271));
        location18.setAddress("부산광역시 기장군 기장읍 기장해안로 340");
        dto18.setLocation(location18);

        dto18.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191226101451768");
        dto18.setNickname(member5.getNickname());
        dto18.setTags(List.of(seaTag.toResponseDto(), aloneTag.toResponseDto(), withHoneyTag.toResponseDto(), coupleTag.toResponseDto(), PetTag.toResponseDto(), historyTag.toResponseDto()));
        destinationService.save(dto18);

        // ---------- 19 번째 여행지 ----------

        RequestDto dto19 = new RequestDto();
        dto19.setName("솔로몬로파크");
        dto19.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location19 = new LocationDto();
        location19.setLatitude(BigDecimal.valueOf(35.076269817));
        location19.setLongitude(BigDecimal.valueOf(129.0236624271));
        location19.setAddress("부산광역시 북구 낙동북로 755");
        dto19.setLocation(location19);

        dto19.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191226183133502");
        dto19.setNickname(member5.getNickname());
        dto19.setTags(List.of(withChildTag.toResponseDto(), ChildrenTag.toResponseDto(), friendTag.toResponseDto(), experienceTag.toResponseDto(), indoorTag.toResponseDto()));
        destinationService.save(dto19);

        // ---------- 20 번째 여행지 ----------

        RequestDto dto20 = new RequestDto();
        dto20.setName("수영사적공원");
        dto20.setContent("여행지 내용");

        // Location 객체 생성 및 설정
        LocationDto location20 = new LocationDto();
        location20.setLatitude(BigDecimal.valueOf(35.076269817));
        location20.setLongitude(BigDecimal.valueOf(129.0236624271));
        location20.setAddress("부산광역시 수영구 수영성로 43");
        dto20.setLocation(location20);

        dto20.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191226132135171");
        dto20.setNickname(member5.getNickname());
        dto20.setTags(List.of(natureTag.toResponseDto(), aloneTag.toResponseDto(), parentTag.toResponseDto(), historyTag.toResponseDto(), coupleTag.toResponseDto()));
        destinationService.save(dto20);


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

        // 자연
        Tag seaTag = tags.stream().filter(tag -> "바다".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "바다 태그"));
        Tag natureTag = tags.stream().filter(tag -> "자연속휴식".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "자연속휴식 태그"));
        Tag hikingTag = tags.stream().filter(tag -> "등산".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "등산 태그"));
        Tag walkingTag = tags.stream().filter(tag -> "둘레길".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "둘레길 태그"));

        // 동행
        Tag aloneTag = tags.stream().filter(tag -> "나홀로 여행".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "나홀로 여행 태그"));
        Tag withChildTag = tags.stream().filter(tag -> "유아동반".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "유아동반 태그"));
        Tag disabledTag = tags.stream().filter(tag -> "장애인".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "장애인 태그"));
        Tag ChildrenTag = tags.stream().filter(tag -> "어린이여행".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "어린이여행 태그"));
        Tag parentTag = tags.stream().filter(tag -> "부모님".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "부모님 태그"));
        Tag coupleTag = tags.stream().filter(tag -> "연인".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "연인 태그"));
        Tag withHoneyTag = tags.stream().filter(tag -> "배우자".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "배우자 태그"));
        Tag friendTag = tags.stream().filter(tag -> "친구".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "친구 태그"));
        Tag PetTag = tags.stream().filter(tag -> "애견동반".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "애견동반 태그"));


        // 활동
        Tag activityTag = tags.stream().filter(tag -> "액티비티".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "액티비티 태그"));
        Tag cultureStreetTag = tags.stream().filter(tag -> "문화거리".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "문화거리 태그"));
        Tag festivalTag = tags.stream().filter(tag -> "축제".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "축제 태그"));
        Tag experienceTag = tags.stream().filter(tag -> "체험여행".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "체험여행 태그"));
        Tag healingTag = tags.stream().filter(tag -> "힐링".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "힐링 태그"));
        Tag historyTag = tags.stream().filter(tag -> "문화/유적지".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "문화/유적지 태그"));


        // 음식
        Tag foodTripTag = tags.stream().filter(tag -> "식도락".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "식도락 태그"));
        Tag TraditionalTag = tags.stream().filter(tag -> "전통시장".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "전통시장 태그"));
        Tag famousRestaurantTag = tags.stream().filter(tag -> "방송 맛집".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "방송 맛집 태그"));
        Tag breadTag = tags.stream().filter(tag -> "빵지순례".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "빵지순례 태그"));
        Tag cafeTag = tags.stream().filter(tag -> "카페투어".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "카페투어 태그"));


        // 날씨
        Tag rainTag = tags.stream().filter(tag -> "우천시 운영".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "우천시 운영 태그"));
        Tag indoorTag = tags.stream().filter(tag -> "실내공간".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "실내공간 태그"));
        Tag summerResortTag = tags.stream().filter(tag -> "피서지".equals(tag.getName())).findFirst()
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "피서지 태그"));

        List<Destination> destinations = destinationService.findAll();

        // 1 번째 코스
        List<AddCourseDestinationRequest> courseDestinations1 = List.of(
                createCourseDestinationRequest(destinations.get(0), 1, 1),
                createCourseDestinationRequest(destinations.get(2), 1, 2),
                createCourseDestinationRequest(destinations.get(4), 2, 1)
        );

        AddTravelCourseRequest course = new AddTravelCourseRequest();
        course.setTitle("마음을 비우는 사찰여행");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191230190101989");
        course.setNickname(member1.getNickname());
        course.setCourseDestinations(courseDestinations1);
        course.setTags(List.of(withChildTag.toResponseDto()));
        courseService.save(course);

        // 2 번째 코스
        List<AddCourseDestinationRequest> courseDestinations2 = List.of(
                createCourseDestinationRequest(destinations.get(0), 1, 1),
                createCourseDestinationRequest(destinations.get(2), 1, 2),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 3, 1)
        );
        course.setTitle("해풍드는 숲속걷기");
        course.setContent("코스 내용");
        course.setDuration(3);
        course.setTravelerCount(2);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20200729150013892");
        course.setNickname(member2.getNickname());
        course.setCourseDestinations(courseDestinations2);
        course.setTags(List.of(seaTag.toResponseDto(), natureTag.toResponseDto(), walkingTag.toResponseDto(), aloneTag.toResponseDto(), parentTag.toResponseDto(), coupleTag.toResponseDto(), withHoneyTag.toResponseDto(), PetTag.toResponseDto(), friendTag.toResponseDto(), healingTag.toResponseDto()));
        courseService.save(course);


        // 3 번째 코스
        List<AddCourseDestinationRequest> courseDestinations3 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(4), 1, 2)
        );
        course.setTitle("박물관 여행 (우천시 운영)");
        course.setContent("코스 내용");
        course.setDuration(1);
        course.setTravelerCount(3);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20240528134839123");
        course.setNickname(member3.getNickname());
        course.setCourseDestinations(courseDestinations3);
        course.setTags(List.of(aloneTag.toResponseDto(), withChildTag.toResponseDto(), friendTag.toResponseDto(), historyTag.toResponseDto(), rainTag.toResponseDto(), indoorTag.toResponseDto()));
        courseService.save(course);


        // 4 번째 코스
        List<AddCourseDestinationRequest> courseDestinations4 = List.of(
                createCourseDestinationRequest(destinations.get(0), 1, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 1)
        );
        course.setTitle("여기저기 섬투어");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20200717114921294");
        course.setNickname(member4.getNickname());
        course.setCourseDestinations(courseDestinations4);
        course.setTags(List.of(seaTag.toResponseDto(), walkingTag.toResponseDto(), parentTag.toResponseDto(), coupleTag.toResponseDto(), withHoneyTag.toResponseDto()));
        courseService.save(course);


        // 5 번째 코스
        List<AddCourseDestinationRequest> courseDestinations5 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("야경맛집 5선");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191227160751479");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations5);
        course.setTags(List.of(aloneTag.toResponseDto(), withChildTag.toResponseDto(), parentTag.toResponseDto(), coupleTag.toResponseDto(), withHoneyTag.toResponseDto(), friendTag.toResponseDto(), healingTag.toResponseDto(), foodTripTag.toResponseDto(), TraditionalTag.toResponseDto(), famousRestaurantTag.toResponseDto()));
        courseService.save(course);


        // 6 번째 코스
        List<AddCourseDestinationRequest> courseDestinations6 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("전통가옥 여행기");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191226110347271");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations6);
        course.setTags(List.of(natureTag.toResponseDto(), withChildTag.toResponseDto(), parentTag.toResponseDto(), withHoneyTag.toResponseDto(), historyTag.toResponseDto(), cultureStreetTag.toResponseDto()));
        courseService.save(course);


        // 7 번째 코스
        List<AddCourseDestinationRequest> courseDestinations7 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("부산마을탐방");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191231184354051");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations7);
        course.setTags(List.of(aloneTag.toResponseDto(), friendTag.toResponseDto(), cultureStreetTag.toResponseDto()));
        courseService.save(course);


        // 8 번째 코스
        List<AddCourseDestinationRequest> courseDestinations8 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("부산 전통굿즈 투어");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20240524131803859");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations8);
        course.setTags(List.of(aloneTag.toResponseDto(), parentTag.toResponseDto(), coupleTag.toResponseDto(), withHoneyTag.toResponseDto() ,historyTag.toResponseDto()));
        courseService.save(course);


        // 9 번째 코스
        List<AddCourseDestinationRequest> courseDestinations9 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("부산을 한눈에, 고지대 코스");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20230921171143286");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations9);
        course.setTags(List.of(hikingTag.toResponseDto(), aloneTag.toResponseDto(), friendTag.toResponseDto()));
        courseService.save(course);


        // 10 번째 코스
        List<AddCourseDestinationRequest> courseDestinations10 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("둘레길로 명상하기");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20240201225043211");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations10);
        course.setTags(List.of(natureTag.toResponseDto(), walkingTag.toResponseDto(), aloneTag.toResponseDto(), friendTag.toResponseDto(), healingTag.toResponseDto()));
        courseService.save(course);


        // 11 번째 코스
        List<AddCourseDestinationRequest> courseDestinations11 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("문화마을코스");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20240527143901347");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations11);
        course.setTags(List.of(aloneTag.toResponseDto(), withChildTag.toResponseDto(), parentTag.toResponseDto(), coupleTag.toResponseDto(), withHoneyTag.toResponseDto(), PetTag.toResponseDto(), cultureStreetTag.toResponseDto()));
        courseService.save(course);


        // 12 번째 코스
        List<AddCourseDestinationRequest> courseDestinations12 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("예술있는 부산, 미술관코스");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20230914182558311");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations12);
        course.setTags(List.of(aloneTag.toResponseDto(), withChildTag.toResponseDto(), disabledTag.toResponseDto(), coupleTag.toResponseDto(), withHoneyTag.toResponseDto(), historyTag.toResponseDto(), rainTag.toResponseDto(), indoorTag.toResponseDto()));
        courseService.save(course);


        // 13 번째 코스
        List<AddCourseDestinationRequest> courseDestinations13 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("문화공간투어");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20201216170753435");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations13);
        course.setTags(List.of(aloneTag.toResponseDto(), coupleTag.toResponseDto(), withHoneyTag.toResponseDto(), friendTag.toResponseDto(), indoorTag.toResponseDto()));
        courseService.save(course);


        // 14 번째 코스
        List<AddCourseDestinationRequest> courseDestinations14 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("바다사진 촬영스팟");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20200514115750295");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations14);
        course.setTags(List.of(seaTag.toResponseDto(), aloneTag.toResponseDto(), healingTag.toResponseDto()));
        courseService.save(course);


        // 15 번째 코스
        List<AddCourseDestinationRequest> courseDestinations15 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("아이들도 좋아하는 온가족코스");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20240527173536003");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations15);
        course.setTags(List.of(withChildTag.toResponseDto(), ChildrenTag.toResponseDto(), activityTag.toResponseDto(), experienceTag.toResponseDto(), rainTag.toResponseDto(), indoorTag.toResponseDto()));
        courseService.save(course);


        // 16 번째 코스
        List<AddCourseDestinationRequest> courseDestinations16 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("수학여행, 체험학습 추천코스");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191229144834500");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations16);
        course.setTags(List.of(withChildTag.toResponseDto(), disabledTag.toResponseDto(), ChildrenTag.toResponseDto(), activityTag.toResponseDto(), experienceTag.toResponseDto(), historyTag.toResponseDto()));
        courseService.save(course);


        // 17 번째 코스
        List<AddCourseDestinationRequest> courseDestinations17 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("바다보며 멍때리기");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191225164921130");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations17);
        course.setTags(List.of(seaTag.toResponseDto(), aloneTag.toResponseDto(), disabledTag.toResponseDto(), coupleTag.toResponseDto(), withHoneyTag.toResponseDto(), healingTag.toResponseDto(), summerResortTag.toResponseDto()));
        courseService.save(course);


        // 18 번째 코스
        List<AddCourseDestinationRequest> courseDestinations18 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("분위기있는 일몰코스");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191225180243523");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations18);
        course.setTags(List.of(seaTag.toResponseDto(), aloneTag.toResponseDto(), disabledTag.toResponseDto(), coupleTag.toResponseDto(), withHoneyTag.toResponseDto(), healingTag.toResponseDto()));
        courseService.save(course);


        // 19 번째 코스
        List<AddCourseDestinationRequest> courseDestinations19 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("쉬면서 즐기는 피크닉 투어");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20191227194942971");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations19);
        course.setTags(List.of(aloneTag.toResponseDto(), withChildTag.toResponseDto(), parentTag.toResponseDto(), coupleTag.toResponseDto(), withHoneyTag.toResponseDto(), friendTag.toResponseDto(), PetTag.toResponseDto(), healingTag.toResponseDto()));
        courseService.save(course);


        // 20 번째 코스
        List<AddCourseDestinationRequest> courseDestinations20 = List.of(
                createCourseDestinationRequest(destinations.get(1), 1, 1),
                createCourseDestinationRequest(destinations.get(3), 2, 1),
                createCourseDestinationRequest(destinations.get(4), 2, 2)
        );
        course.setTitle("커플끼리 함께하는 특별한투어");
        course.setContent("코스 내용");
        course.setDuration(2);
        course.setTravelerCount(4);
        course.setTravelType(0);
        course.setPictureLink("https://www.visitbusan.net/uploadImgs/files/cntnts/20230525134753245");
        course.setNickname(member5.getNickname());
        course.setCourseDestinations(courseDestinations20);
        course.setTags(List.of(coupleTag.toResponseDto(), withHoneyTag.toResponseDto(), activityTag.toResponseDto(), festivalTag.toResponseDto(), healingTag.toResponseDto()));
        courseService.save(course);
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