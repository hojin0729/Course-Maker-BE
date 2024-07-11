package coursemaker.coursemaker.domain.wish.service;


import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import coursemaker.coursemaker.domain.wish.entity.DestinationWish;
import coursemaker.coursemaker.domain.wish.repository.CourseWishRepository;
import coursemaker.coursemaker.domain.wish.repository.DestinationWishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class WishServiceImpl implements WishService {
    private CourseWishRepository courseWishRepository;
    private DestinationWishRepository destinationWishRepository;


    /**
     * 여행 코스에 대한 찜하기를 추가하거나, 이미 존재하는 경우 카운트를 증가시킨다.
     *
     * @param courseId 찜하고자 하는 여행 코스의 ID.
     * @return 업데이트되거나 새로 생성된 CourseWish 객체.
     */
    @Transactional
    public CourseWish addCourseWish(Long courseId) {
        Optional<CourseWish> existingWish = courseWishRepository.findById(courseId); //주어진 courseId에 대한 기존 찜하기를 찾는다.
        CourseWish courseWish;

        // 찜하기가 이미 존재하는 경우, 카운트를 증가시킴.
        if (existingWish.isPresent()) {
            courseWish = existingWish.get();
            courseWish.setCount(courseWish.getCount() + 1);
        } else { // 찜하기가 존재하지 않는 경우, 새로 생성하고 카운트를 1로 설정.
            courseWish = new CourseWish();
            courseWish.setTravelCourse(new TravelCourse(courseId));
            courseWish.setCount(1L);
        }
        return courseWishRepository.save(courseWish); // courseWish 객체를 저장하고 반환
    }



    /**
     * 목적지에 대한 찜하기를 추가하거나, 이미 존재하는 경우 카운트를 증가시킨다.
     *
     * @param destinationId 찜하고자 하는 목적지의 ID.
     * @return 업데이트되거나 새로 생성된 DestinationWish 객체.
     */
    @Transactional
    public DestinationWish addOrIncrementDestinationWish(Long destinationId) {
        // 주어진 destinationId에 대한 기존 찜하기를 찾는다.
        Optional<DestinationWish> existingWish = destinationWishRepository.findById(destinationId);
        DestinationWish destinationWish;
        if (existingWish.isPresent()) {  // 찜하기가 이미 존재하는 경우, 카운트를 증가시킨다.
            destinationWish = existingWish.get();
            destinationWish.setCount(destinationWish.getCount() + 1);
        } else { // 찜하기가 존재하지 않는 경우, 새로 생성하고 카운트를 1로 설정.
            destinationWish = new DestinationWish();
            destinationWish.setDestination(new Destination(destinationId));
            destinationWish.setCount(1L);
        }
        // destinationWish 객체를 저장하고 반환
        return destinationWishRepository.save(destinationWish);
    }


    /**
     * 여행 코스에 대한 찜하기의 카운트를 감소시키거나, 카운트가 0이 되면 삭제한다.
     *
     * @param courseId 카운트를 감소시키거나 삭제할 여행 코스의 ID.
     */
    @Transactional
    public void decrementOrRemoveCourseWish(Long courseId) {
        // 주어진 courseId에 대한 기존 찜하기를 찾는다.
        Optional<CourseWish> existingWish = courseWishRepository.findById(courseId);
        existingWish.ifPresent(courseWish -> { // 찜하기가 존재하는 경우, 카운트를 감소시키거나 0이 되면 삭제
            if (courseWish.getCount() > 1) {
                courseWish.setCount(courseWish.getCount() - 1);
                courseWishRepository.save(courseWish);
            } else {
                courseWishRepository.delete(courseWish);
            }
        });
    }


    /**
     * 목적지에 대한 찜하기의 카운트를 감소시키거나, 카운트가 0이 되면 삭제한다.
     *
     * @param destinationId 카운트를 감소시키거나 삭제할 목적지의 ID.
     */
    @Transactional
    public void decrementOrRemoveDestinationWish(Long destinationId) {
        // 주어진 destinationId에 대한 기존 찜하기를 찾는다.
        Optional<DestinationWish> existingWish = destinationWishRepository.findById(destinationId);

        // 찜하기가 존재하는 경우, 카운트를 감소시키거나 0이 되면 삭제.
        existingWish.ifPresent(destinationWish -> {
            if (destinationWish.getCount() > 1) {
                destinationWish.setCount(destinationWish.getCount() - 1);
                destinationWishRepository.save(destinationWish);
            } else {
                destinationWishRepository.delete(destinationWish);
            }
        });
    }
}
