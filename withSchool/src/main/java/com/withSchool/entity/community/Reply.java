package com.withSchool.entity.community;

import com.withSchool.entity.base.BaseEntity;
import com.withSchool.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reply")
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id", unique = true, nullable = false)
    @Comment("댓글ID")
    private Long replyId;

    @Column(name = "content", nullable = false)
    @Comment("내용")
    private String content;

    @Column(name = "like_count", nullable = false, columnDefinition = "int default 0")
    @Comment("좋아요 수")
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reply_id")
    @Comment("부모댓글ID")
    private Reply parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @Comment("게시글ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("사용자ID")
    private User user;
}
