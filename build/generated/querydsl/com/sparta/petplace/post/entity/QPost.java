package com.sparta.petplace.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -1782087075L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.sparta.petplace.common.QTimestamped _super = new com.sparta.petplace.common.QTimestamped(this);

    public final StringPath aboolean1 = createString("aboolean1");

    public final StringPath aboolean2 = createString("aboolean2");

    public final StringPath address = createString("address");

    public final StringPath category = createString("category");

    public final StringPath ceo = createString("ceo");

    public final StringPath closedDay = createString("closedDay");

    public final StringPath contents = createString("contents");

    public final StringPath cost = createString("cost");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final StringPath endTime = createString("endTime");

    public final StringPath feature1 = createString("feature1");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<PostImage, QPostImage> image = this.<PostImage, QPostImage>createList("image", PostImage.class, QPostImage.class, PathInits.DIRECT2);

    public final StringPath lat = createString("lat");

    public final StringPath lng = createString("lng");

    public final com.sparta.petplace.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath resizeImage = createString("resizeImage");

    public final ListPath<com.sparta.petplace.review.entity.Review, com.sparta.petplace.review.entity.QReview> reviews = this.<com.sparta.petplace.review.entity.Review, com.sparta.petplace.review.entity.QReview>createList("reviews", com.sparta.petplace.review.entity.Review.class, com.sparta.petplace.review.entity.QReview.class, PathInits.DIRECT2);

    public final NumberPath<Integer> star = createNumber("star", Integer.class);

    public final StringPath startTime = createString("startTime");

    public final StringPath telNum = createString("telNum");

    public final StringPath title = createString("title");

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.sparta.petplace.member.entity.QMember(forProperty("member")) : null;
    }

}

