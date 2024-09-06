package coursemaker.coursemaker.domain.member.service;

import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {
    private final MemberService memberService;
    private final CourseService courseService;
    private final DestinationService destinationService;

    /**
     * 도메인 분리 이유: 순환참조
     * 코스 -> 멤버 참조(사용자 정보 필요),
     * 멤버 -> 코스 참조(코스 정보 필요)
     * => 순환참조 발생으로 서브도메인으로 분리함.
     * TODO: 마이페이지 기능 구현
     * */
}
