package org.maximum0.blog.domain.comment

import jakarta.persistence.*
import org.maximum0.blog.domain.AuditingEntity
import org.maximum0.blog.domain.post.Post

@Entity
@Table(name = "tb_comment")
class Comment (
    id: Long = 0,
    content: String,
    post: Post
) : AuditingEntity(id) {

    @Column(name = "content")
    var content: String = content
        protected set


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Post::class)
    var post: Post = post
        protected set

}