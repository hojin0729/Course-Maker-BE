package coursemaker.coursemaker.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseReview is a Querydsl query type for CourseReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseReview extends EntityPathBase<CourseReview> {

    private static final long serialVersionUID = 2068784040L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseReview courseReview = new QCourseReview("courseReview");

    public final coursemaker.coursemaker.QBaseEntity _super = new coursemaker.coursemaker.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> rating = createNumber("rating", java.math.BigDecimal.class);

    public final StringPath text = createString("text");

    public final coursemaker.coursemaker.domain.course.entity.QTravelCourse travelCourse;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCourseReview(String variable) {
        this(CourseReview.class, forVariable(variable), INITS);
    }

    public QCourseReview(Path<? extends CourseReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseReview(PathMetadata metadata, PathInits inits) {
        this(CourseReview.class, metadata, inits);
    }

    public QCourseReview(Class<? extends CourseReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.travelCourse = inits.isInitialized("travelCourse") ? new coursemaker.coursemaker.domain.course.entity.QTravelCourse(forProperty("travelCourse"), inits.get("travelCourse")) : null;
    }

}

