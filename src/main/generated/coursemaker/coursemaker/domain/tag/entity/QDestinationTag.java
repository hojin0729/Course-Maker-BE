package coursemaker.coursemaker.domain.tag.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDestinationTag is a Querydsl query type for DestinationTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDestinationTag extends EntityPathBase<DestinationTag> {

    private static final long serialVersionUID = 491259715L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDestinationTag destinationTag = new QDestinationTag("destinationTag");

    public final coursemaker.coursemaker.domain.destination.entity.QDestination destination;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QTag tag;

    public QDestinationTag(String variable) {
        this(DestinationTag.class, forVariable(variable), INITS);
    }

    public QDestinationTag(Path<? extends DestinationTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDestinationTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDestinationTag(PathMetadata metadata, PathInits inits) {
        this(DestinationTag.class, metadata, inits);
    }

    public QDestinationTag(Class<? extends DestinationTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.destination = inits.isInitialized("destination") ? new coursemaker.coursemaker.domain.destination.entity.QDestination(forProperty("destination")) : null;
        this.tag = inits.isInitialized("tag") ? new QTag(forProperty("tag")) : null;
    }

}

