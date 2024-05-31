package coursemaker.coursemaker.domain.course.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTravelCourse is a Querydsl query type for TravelCourse
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTravelCourse extends EntityPathBase<TravelCourse> {

    private static final long serialVersionUID = -914351763L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTravelCourse travelCourse = new QTravelCourse("travelCourse");

    public final coursemaker.coursemaker.QBaseEntity _super = new coursemaker.coursemaker.QBaseEntity(this);

    public final StringPath content = createString("content");

    public final ListPath<CourseDestination, QCourseDestination> courseDestinations = this.<CourseDestination, QCourseDestination>createList("courseDestinations", CourseDestination.class, QCourseDestination.class, PathInits.DIRECT2);

    public final ListPath<coursemaker.coursemaker.domain.tag.entity.CourseTag, coursemaker.coursemaker.domain.tag.entity.QCourseTag> courseTags = this.<coursemaker.coursemaker.domain.tag.entity.CourseTag, coursemaker.coursemaker.domain.tag.entity.QCourseTag>createList("courseTags", coursemaker.coursemaker.domain.tag.entity.CourseTag.class, coursemaker.coursemaker.domain.tag.entity.QCourseTag.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> duration = createNumber("duration", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final coursemaker.coursemaker.domain.member.entity.QMember member;

    public final StringPath pictureLink = createString("pictureLink");

    public final StringPath title = createString("title");

    public final NumberPath<Integer> travelerCount = createNumber("travelerCount", Integer.class);

    public final NumberPath<Integer> travelType = createNumber("travelType", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QTravelCourse(String variable) {
        this(TravelCourse.class, forVariable(variable), INITS);
    }

    public QTravelCourse(Path<? extends TravelCourse> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTravelCourse(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTravelCourse(PathMetadata metadata, PathInits inits) {
        this(TravelCourse.class, metadata, inits);
    }

    public QTravelCourse(Class<? extends TravelCourse> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new coursemaker.coursemaker.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

