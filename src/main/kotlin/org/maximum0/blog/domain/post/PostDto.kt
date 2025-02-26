package org.maximum0.blog.domain.post

import jakarta.validation.constraints.NotNull
import org.maximum0.blog.domain.member.*

data class PostSaveRequest(
    @field:NotNull(message = "required title")
    val title: String?,

    val content: String?,

    @field:NotNull(message = "required memberId")
    val memberId: Long?,
)

fun PostSaveRequest.toEntity(): Post {
    return Post(
        title = this.title ?: "",
        content = this.content ?: "",
        member = Member.createFakeMember(this.memberId!!)
    )
}

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val member: MemberResponse
)
