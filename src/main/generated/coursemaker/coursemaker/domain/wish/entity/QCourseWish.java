package coursemaker.coursemaker.domain.wish.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseWish is a Querydsl query type for CourseWish
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseWish extends EntityPathBase<CourseWish> {

    private static final long serialVersionUID = 1362869382L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseWish courseWish = new QCourseWish("courseWish");

    public final NumberPath<Long> count = createNumber("count", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final coursemaker.coursemaker.domain.course.entity.QTravelCourse travelCourse;

    public QCourseWish(String variable) {
        this(CourseWish.class, forVariable(variable), INITS);
    }

    public QCourseWish(Path<? extends CourseWish> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseWish(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseWish(PathMetadata metadata, PathInits inits) {
        this(CourseWish.class, metadata, inits);
    }

    public QCourseWish(Class<? extends CourseWish> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.travelCourse = inits.isInitialized("travelCourse") ? new coursemaker.coursemaker.domain.course.entity.QTravelCourse(forProperty("travelCourse"), inits.get("travelCourse")) : null;
    }

}

