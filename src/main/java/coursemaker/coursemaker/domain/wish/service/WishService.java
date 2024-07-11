package coursemaker.coursemaker.domain.wish.service;

import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import coursemaker.coursemaker.domain.wish.entity.DestinationWish;

public interface WishService {
    CourseWish addWish(CourseWish courseWish);

    DestinationWish addDestinationWish(DestinationWish destinationWish);

    void decrementOrRemoveCourseWish(Long courseWish);

    void decrementOrRemoveDestinationWish(Long destinationWish);
}
