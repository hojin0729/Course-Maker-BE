package coursemaker.coursemaker.domain.course.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTravelCourse is a Querydsl query type for TravelCourse
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTravelCourse extends EntityPathBase<TravelCourse> {

    private static final long serialVersionUID = -914351763L;

    public static final QTravelCourse travelCourse = new QTravelCourse("travelCourse");

    public final coursemaker.coursemaker.QBaseEntity _super = new coursemaker.coursemaker.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> duration = createNumber("duration", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> travelerCount = createNumber("travelerCount", Integer.class);

    public final NumberPath<Integer> travelType = createNumber("travelType", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTravelCourse(String variable) {
        super(TravelCourse.class, forVariable(variable));
    }

    public QTravelCourse(Path<? extends TravelCourse> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTravelCourse(PathMetadata metadata) {
        super(TravelCourse.class, metadata);
    }

}

