package org.maximum0.blog.domain.post

import org.maximum0.blog.domain.member.*

data class PostSaveRequest(
    val title: String,
    val content: String,
    val memberId: Long,
)

fun PostSaveRequest.toEntity(): Post {
    return Post(
        title = this.title,
        content = this.content,
        member = Member.createFakeMember(this.memberId)
    )
}

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val member: MemberResponse
)
