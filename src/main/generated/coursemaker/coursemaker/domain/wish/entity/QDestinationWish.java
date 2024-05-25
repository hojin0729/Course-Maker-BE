package coursemaker.coursemaker.domain.wish.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDestinationWish is a Querydsl query type for DestinationWish
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDestinationWish extends EntityPathBase<DestinationWish> {

    private static final long serialVersionUID = -1323562191L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDestinationWish destinationWish = new QDestinationWish("destinationWish");

    public final NumberPath<Long> count = createNumber("count", Long.class);

    public final coursemaker.coursemaker.domain.destination.entity.QDestination destination;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QDestinationWish(String variable) {
        this(DestinationWish.class, forVariable(variable), INITS);
    }

    public QDestinationWish(Path<? extends DestinationWish> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDestinationWish(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDestinationWish(PathMetadata metadata, PathInits inits) {
        this(DestinationWish.class, metadata, inits);
    }

    public QDestinationWish(Class<? extends DestinationWish> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.destination = inits.isInitialized("destination") ? new coursemaker.coursemaker.domain.destination.entity.QDestination(forProperty("destination")) : null;
    }

}

