package coursemaker.coursemaker.domain.course.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseDestination is a Querydsl query type for CourseDestination
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseDestination extends EntityPathBase<CourseDestination> {

    private static final long serialVersionUID = -1848593829L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseDestination courseDestination = new QCourseDestination("courseDestination");

    public final NumberPath<Short> date = createNumber("date", Short.class);

    public final coursemaker.coursemaker.domain.destination.entity.QDestination destination;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QTravelCourse travelCourse;

    public final NumberPath<Short> visitOrder = createNumber("visitOrder", Short.class);

    public QCourseDestination(String variable) {
        this(CourseDestination.class, forVariable(variable), INITS);
    }

    public QCourseDestination(Path<? extends CourseDestination> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseDestination(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseDestination(PathMetadata metadata, PathInits inits) {
        this(CourseDestination.class, metadata, inits);
    }

    public QCourseDestination(Class<? extends CourseDestination> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.destination = inits.isInitialized("destination") ? new coursemaker.coursemaker.domain.destination.entity.QDestination(forProperty("destination")) : null;
        this.travelCourse = inits.isInitialized("travelCourse") ? new QTravelCourse(forProperty("travelCourse")) : null;
    }

}

