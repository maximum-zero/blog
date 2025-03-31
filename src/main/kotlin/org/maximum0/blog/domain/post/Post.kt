package org.maximum0.blog.domain.post

import jakarta.persistence.*
import org.maximum0.blog.domain.AuditingEntity
import org.maximum0.blog.domain.member.Member

@Entity
@Table(name = "tb_post")
class Post(
    id: Long = 0,
    title: String,
    content: String,
    member: Member
) : AuditingEntity(id) {

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Lob
    @Column(name = "content")
    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    var member: Member = member
        protected set

    override fun toString(): String {
        return "Post(title='$title', content='$content', member=$member)"
    }

}

fun Post.toDto(): PostResponse {
    return PostResponse(
        id = this.id!!,
        title = this.title,
        content = this.content,
        member = this.member.toDto()
    )
}