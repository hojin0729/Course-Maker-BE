package coursemaker.coursemaker.domain.destination.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDestination is a Querydsl query type for Destination
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDestination extends EntityPathBase<Destination> {

    private static final long serialVersionUID = -507642909L;

    public static final QDestination destination = new QDestination("destination");

    public final coursemaker.coursemaker.QBaseEntity _super = new coursemaker.coursemaker.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public final NumberPath<java.math.BigDecimal> latitude = createNumber("latitude", java.math.BigDecimal.class);

    public final StringPath location = createString("location");

    public final NumberPath<java.math.BigDecimal> longitude = createNumber("longitude", java.math.BigDecimal.class);

    public final StringPath name = createString("name");

    public final StringPath pictureLink = createString("pictureLink");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QDestination(String variable) {
        super(Destination.class, forVariable(variable));
    }

    public QDestination(Path<? extends Destination> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDestination(PathMetadata metadata) {
        super(Destination.class, metadata);
    }

}

