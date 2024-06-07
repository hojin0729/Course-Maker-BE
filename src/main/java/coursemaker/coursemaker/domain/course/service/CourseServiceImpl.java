package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.*;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.exception.IllegalTravelCourseArgumentException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseAlreadyDeletedException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseDuplicatedException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseNotFoundException;
import coursemaker.coursemaker.domain.course.repository.CourseDestinationRepository;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseDestinationService;
import coursemaker.coursemaker.domain.course.repository.TravelCourseRepository;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.destination.exception.PictureNotFoundException;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.service.TagService;
import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.util.CourseMakerPagination;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
@Transactional
public class CourseServiceImpl implements CourseService{

    private final CourseDestinationRepository courseDestinationRepository;

    private final TravelCourseRepository travelCourseRepository;

    private final TagService tagService;

    private final DestinationService destinationService;

    private final MemberService memberService;

    public CourseServiceImpl(CourseDestinationRepository courseDestinationRepository,
                             TravelCourseRepository travelCourseRepository,
                             @Lazy TagService tagService,
                             DestinationService destinationService,
                             MemberService memberService) {
        this.courseDestinationRepository = courseDestinationRepository;
        this.travelCourseRepository = travelCourseRepository;
        this.tagService = tagService;
        this.destinationService = destinationService;
        this.memberService = memberService;
    }

    @Override
    public TravelCourse save(AddTravelCourseRequest request) {
        // Optional<TravelCourse> existingCourse = travelCourseRepository.findByTitle(request.getTitle());
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 이름이 존재하지 않습니다.", "title is empty");
        }

        if (request.getContent() == null || request.getContent().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 내용이 존재하지 않습니다.", "course is empty");
        }

        if (request.getDuration() == null ) {
            throw new IllegalTravelCourseArgumentException("여행 기간이 존재하지 않습니다.", "duration is null");
        }

        if (request.getDuration() > 3 || request.getDuration() < 1) {
            throw new IllegalTravelCourseArgumentException("여행 기간은 1~3일 사이 입니다.", "duration: " + request.getDuration());
        }

        if (request.getTravelerCount() == null || request.getTravelerCount() < 1) {
            throw new IllegalTravelCourseArgumentException("여행 인원이 존재하지 않습니다.", "traveler count: " + request.getTravelerCount());
        }

        if (request.getTravelType() == null) {
            throw new IllegalTravelCourseArgumentException("여행 타입이 존재하지 않습니다.", "traveler type is null");
        }

        if (request.getPictureLink() == null || request.getPictureLink().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("이미지 링크가 존재하지 않습니다.", "image link is empty");
        }

        if (request.getCourseDestinations() == null || request.getCourseDestinations().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 여행지가 존재하지 않습니다.", "course destination is empty");
        }

        if(request.getTags() == null || request.getTags().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 태그가 존재하지 않습니다.", "tag is empty");
        }


        // TODO: ROW MAPPER로 엔티티 - DTO 매핑
        /***************DTO - entity 변환**************/

        /*travel course 설정*/
        Member member = memberService.findByNickname(request.getNickname());
        TravelCourse travelCourse = TravelCourse.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .duration(request.getDuration())
                .travelerCount(request.getTravelerCount())
                .travelType(request.getTravelType())
                .pictureLink(request.getPictureLink())
                .member(member)
                .build();
        travelCourse = travelCourseRepository.save(travelCourse);

        /*destination 설정*/
        for (AddCourseDestinationRequest courseDestination : request.getCourseDestinations()) {

            CourseDestination courseDestinationEntity = CourseDestination.builder()
                    .date(courseDestination.getDate())
                    .visitOrder(courseDestination.getVisitOrder())
                    .build();
            courseDestinationEntity.setTravelCourse(travelCourse);

            Destination destination = destinationService.findById(courseDestination.getDestination().getId());

            courseDestinationEntity.setDestination(destination);
            courseDestinationRepository.save(courseDestinationEntity);
        }


        /*태그 설정*/
        List<Long> tagIds = request.getTags().stream()
                .map(TagResponseDto::getId)
                .collect(Collectors.toList());
        tagService.addTagsByCourse(travelCourse.getId(), tagIds);


        return travelCourse;
    }

    @Override
    public CourseMakerPagination<TravelCourse> findAll(Pageable pageable) {
        Page<TravelCourse> page = travelCourseRepository.findAllByDeletedAtIsNull(pageable);// db에서 페이지 단위로 가져옴
        CourseMakerPagination<TravelCourse> courseMakerPagination = new CourseMakerPagination<>(pageable, page);// 페이지네이션 객체 변환

        return courseMakerPagination;
    }

    @Override
    public CourseMakerPagination<TravelCourse> getAllOrderByViewsDesc(Pageable pageable) {
        Page<TravelCourse> page = travelCourseRepository.findAllByDeletedAtIsNullOrderByViewsDesc(pageable);// db에서 페이지 단위로 가져옴
        CourseMakerPagination<TravelCourse> courseMakerPagination = new CourseMakerPagination<>(pageable, page);// 페이지네이션 객체 변환
        return courseMakerPagination;
    }

    @Override
    public TravelCourse findById(Long id) {
        return travelCourseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new TravelCourseNotFoundException("존재하지 않는 코스입니다.", "Course ID: " + id));
    }

    @Override
    public TravelCourse update(Long id, UpdateTravelCourseRequest request) {

        // Optional<TravelCourse> existingCourse = travelCourseRepository.findByTitle(request.getTitle());

        travelCourseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new TravelCourseNotFoundException("수정할 코스가 존재하지 않습니다.", "course ID: " + id));

        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 이름이 존재하지 않습니다.", "title is empty");
        }


        if (request.getContent() == null || request.getContent().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 내용이 존재하지 않습니다.", "course is empty");
        }

        if (request.getDuration() == null ) {
            throw new IllegalTravelCourseArgumentException("여행 기간이 존재하지 않습니다.", "duration is null");
        }

        if (request.getDuration() > 3 || request.getDuration() < 1) {
            throw new IllegalTravelCourseArgumentException("여행 기간은 1~3일 사이 입니다.", "duration: " + request.getDuration());
        }

        if (request.getTravelerCount() == null || request.getTravelerCount() < 1) {
            throw new IllegalTravelCourseArgumentException("여행 인원이 존재하지 않습니다.", "traveler count: " + request.getTravelerCount());
        }

        if (request.getTravelType() == null) {
            throw new IllegalTravelCourseArgumentException("여행 타입이 존재하지 않습니다.", "traveler type is null");
        }

        if (request.getPictureLink() == null || request.getPictureLink().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("이미지 링크가 존재하지 않습니다.", "image link is empty");
        }

        if (request.getCourseDestinations() == null || request.getCourseDestinations().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 여행지가 존재하지 않습니다.", "course destination is empty");
        }

        if(request.getTags() == null || request.getTags().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 태그가 존재하지 않습니다.", "tag is empty");
        }


        // TODO: ROW MAPPER로 엔티티 - DTO 매핑
        /***************DTO - entity 변환**************/

        /*travel course 설정*/
        /*TODO: 멤버 닉네임을 기반으로 객체 가져오는부분 연결하기!*/
        Member member = memberService.findByNickname(request.getNickname());
        TravelCourse travelCourse = TravelCourse.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .duration(request.getDuration())
                .travelerCount(request.getTravelerCount())
                .travelType(request.getTravelType())
                .pictureLink(request.getPictureLink())
                .member(member)
                .build();
        travelCourse.setId(id);// id가 있으면 update함
        travelCourse = travelCourseRepository.save(travelCourse);


        /****TODO: ROW MAPPER로 엔티티 - DTO 매핑****/
        /*destination 설정*/
        courseDestinationRepository.deleteAllByTravelCourseId(id);// 여행지 초기화
        for (UpdateCourseDestinationRequest courseDestination : request.getCourseDestinations()) {

            CourseDestination courseDestinationEntity = CourseDestination.builder()
                    .date(courseDestination.getDate())
                    .visitOrder(courseDestination.getVisitOrder())
                    .build();
            courseDestinationEntity.setTravelCourse(travelCourse);

            Destination destination = destinationService.findById(courseDestination.getDestination().getId());

            courseDestinationEntity.setDestination(destination);
            courseDestinationRepository.save(courseDestinationEntity);
        }


        /*태그 설정*/
        tagService.deleteAllTagByCourse(id);// 태그 초기화
        List<Long> tagIds = request.getTags().stream()
                .map(TagResponseDto::getId)
                .collect(Collectors.toList());
        tagService.addTagsByCourse(id, tagIds);

        return travelCourseRepository.save(travelCourse);
    }

    @Override
    public void delete(Long id) {
        TravelCourse travelCourse = travelCourseRepository.findById(id)
                .orElseThrow(() -> new TravelCourseNotFoundException("삭제할 코스가 존재하지 않습니다.", "Course ID: " + id));

        if (travelCourse.getDeletedAt() != null) {
            throw new TravelCourseAlreadyDeletedException("해당 코스는 이미 삭제되었습니다.", "Course ID: " + id);
        }

        travelCourse.setDeletedAt(LocalDateTime.now());
        travelCourseRepository.save(travelCourse);
//        if (!travelCourseRepository.existsById(id)) {
//            throw new TravelCourseNotFoundException("삭제할 코스가 존재하지 않습니다.", "Course ID: " + id);
//        }
//        // Todo:  실제로 날리지 말고 soft 딜리트로 하자. 컬럼 추가: deleteAt 추가. deleteAt에 특정 날짜 추가 (디폴트 벨류 null)
//        // 오늘 삭제하면 오늘 들어가게 된다. 데이터는 날리면 안 된다.
//        // null이면 데이터 조회가 되고, 날짜값이 있으면 조회가 안 되게 해야한다.
//        courseDestinationRepository.deleteAllByTravelCourseId(id);
//        travelCourseRepository.deleteById(id);
    }

    @Override
    public TravelCourse incrementViews(Long id) {
        TravelCourse travelCourse = travelCourseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new TravelCourseNotFoundException("코스가 존재하지 않습니다.", "Course ID: " + id));
        travelCourse.incrementViews();
        return travelCourseRepository.save(travelCourse);
    }

    @Override
    public void addPictureLink(Long courseId, String pictureLink) {
        TravelCourse travelCourse = travelCourseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new TravelCourseNotFoundException("해당하는 코스를 찾을수 없습니다: " + courseId, "Course id: " + courseId));
        travelCourse.setPictureLink(pictureLink);
        travelCourseRepository.save(travelCourse);
    }

    // 코스 id로 여행지의 대표사진 URL을 조회하는 메서드
    @Override
    public String getPictureLink(Long courseId) {
        TravelCourse travelCourse = travelCourseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new TravelCourseNotFoundException("해당하는 코스를 찾을수 없습니다: " + courseId, "Course id: " + courseId));
        String pictureLink = travelCourse.getPictureLink();
        if (pictureLink.isEmpty()) {
            throw new PictureNotFoundException(ErrorCode.PICTURE_NOT_FOUND, "Course id: " + courseId);
        }
        return pictureLink;
    }

    // 기존 코스의 대표사진 URL을 변경하는 메서드.
    @Override
    public void updatePictureLink(Long courseId, String newPictureLink) {
        TravelCourse travelCourse = travelCourseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new TravelCourseNotFoundException("해당하는 코스를 찾을수 없습니다: " + courseId, "Course id: " + courseId));
        travelCourse.setPictureLink(newPictureLink);
        travelCourseRepository.save(travelCourse);
    }

    // 특정 코스의 대표사진 링크를 삭제하는 메서드.
    @Override
    public void deletePictureLink(Long courseId) {
        TravelCourse travelCourse = travelCourseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new TravelCourseNotFoundException("해당하는 코스를 찾을수 없습니다: " + courseId, "Course id: " + courseId));
        if (travelCourse.getPictureLink().isEmpty()) {
            throw new PictureNotFoundException(ErrorCode.PICTURE_NOT_FOUND, "Course id: " + courseId);
        }
        // 대표사진 링크만 삭제
        travelCourse.setPictureLink(null);
        travelCourseRepository.save(travelCourse);
    }
}