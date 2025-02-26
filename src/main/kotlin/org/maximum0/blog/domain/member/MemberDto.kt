package org.maximum0.blog.domain.member

import jakarta.validation.constraints.NotNull

data class MemberSaveRequest(
    @field:NotNull(message = "required email")
    val email: String?,
    val password: String?,
    val role: Role?,
)

fun MemberSaveRequest.toEntity(): Member {
    return Member(
        email = this.email ?: "",
        password = this.password ?: "",
        role = this.role ?: Role.USER,
    )
}

data class MemberResponse(
    val id: Long,
    val email: String,
    val password: String,
    val role: Role,
)
