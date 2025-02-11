package org.maximum0.blog.domain.member

data class MemberSaveRequest(
    val email: String,
    val password: String,
    val role: Role,
)

fun MemberSaveRequest.toEntity(): Member {
    return Member(
        email = this.email,
        password = this.password,
        role = this.role,
    )
}

data class MemberResponse(
    val id: Long,
    val email: String,
    val password: String,
    val role: Role,
)
