package org.maximum0.blog.domain.member

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class LoginDto(
    @field:NotNull(message = "required email")
    val email: String?,
    val password: String?,
    val role: Role?,
)

fun LoginDto.toEntity(): Member {
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
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
