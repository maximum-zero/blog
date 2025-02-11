package org.maximum0.blog.domain.post

import jakarta.persistence.*
import org.maximum0.blog.domain.AuditingEntity
import org.maximum0.blog.domain.member.Member

@Entity
@Table(name = "tb_post")
class Post(
    title: String,
    content: String,
    member: Member
) : AuditingEntity() {

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "content")
    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    var member: Member = member
        protected set

}