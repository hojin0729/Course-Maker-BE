package coursemaker.coursemaker.domain.tag.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseTag is a Querydsl query type for CourseTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseTag extends EntityPathBase<CourseTag> {

    private static final long serialVersionUID = 894141576L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseTag courseTag = new QCourseTag("courseTag");

    public final coursemaker.coursemaker.domain.course.entity.QTravelCourse course;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QTag tag;

    public QCourseTag(String variable) {
        this(CourseTag.class, forVariable(variable), INITS);
    }

    public QCourseTag(Path<? extends CourseTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseTag(PathMetadata metadata, PathInits inits) {
        this(CourseTag.class, metadata, inits);
    }

    public QCourseTag(Class<? extends CourseTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new coursemaker.coursemaker.domain.course.entity.QTravelCourse(forProperty("course")) : null;
        this.tag = inits.isInitialized("tag") ? new QTag(forProperty("tag")) : null;
    }

}

