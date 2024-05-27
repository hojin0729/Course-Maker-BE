package coursemaker.coursemaker.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDestinationReview is a Querydsl query type for DestinationReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDestinationReview extends EntityPathBase<DestinationReview> {

    private static final long serialVersionUID = 800469457L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDestinationReview destinationReview = new QDestinationReview("destinationReview");

    public final coursemaker.coursemaker.QBaseEntity _super = new coursemaker.coursemaker.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final coursemaker.coursemaker.domain.destination.entity.QDestination destination;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> rating = createNumber("rating", java.math.BigDecimal.class);

    public final StringPath text = createString("text");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDestinationReview(String variable) {
        this(DestinationReview.class, forVariable(variable), INITS);
    }

    public QDestinationReview(Path<? extends DestinationReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDestinationReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDestinationReview(PathMetadata metadata, PathInits inits) {
        this(DestinationReview.class, metadata, inits);
    }

    public QDestinationReview(Class<? extends DestinationReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.destination = inits.isInitialized("destination") ? new coursemaker.coursemaker.domain.destination.entity.QDestination(forProperty("destination")) : null;
    }

}

