package coursemaker.coursemaker.domain.wish.service;

import coursemaker.coursemaker.domain.wish.entity.CourseWish;
import coursemaker.coursemaker.domain.wish.entity.DestinationWish;

public interface WishService {


    void decrementOrRemoveCourseWish(Long courseWish);

    void decrementOrRemoveDestinationWish(Long destinationWish);
}
