package coursemaker.coursemaker.domain.course.repository;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CourseDestinationRepository extends JpaRepository<CourseDestination, Long> {

//    Map<Destination, Long> mapSave(Map<Destination, Integer> visitOrders);
    // visitOrder DB에 필드로 저장되어 있다. 그러면 그거를 orderby로 조회를 할 수 있다.
    // 그러면 orderby로 조회를 하고 그 결과값을 서비스에서 쓸 수 있다.
    // 태그로 먼저 필터링을 하는데, 일단은 저는 visitOrder쪽을 컨트롤러에서 이 요청을 하면은 visitOrder해서 조회수, 이름순, 좋아요순으로 나오게 하고
    // 서비스는 구현 안 하고, 컨트롤러로 이렇게 하면 이렇게 되지 않을까 컨트롤러로 먼저 해보자.
    // 태그로 필터링하다 보니 태그로 해야할 것 같다.
    // 다시 다시 코스 순서.
    // 프론트쪽에서 먼저 순서를 지정하고, 그게 바로 DB에 저장되어서 그걸 제가 가져다 쓰는거다?
    // dto로 만들어서 코스에서 dto로 해서 어떤 것은 여행지를 리스트 형태로 받고, 리스트 형태로 받을 때 visitorder도 같이 받는다.

    // 코스={
    //리스트 {여행지, visitOrder, 날짜(1일차, 2일차)}
    //}
    // 프론트에서 이렇게 받아라. 어떤 여행지가 몇번째 여행지고 몇일차인지 컨트롤러에서 리스트형태로 정보를 받아라.
}