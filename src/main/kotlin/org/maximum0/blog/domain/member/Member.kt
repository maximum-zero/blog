package org.maximum0.blog.domain.member

import jakarta.persistence.*
import org.maximum0.blog.domain.AuditingEntity

@Entity
@Table(name = "tb_member")
class Member(
    id: Long = 0,
    email: String,
    password: String,
    role: Role = Role.USER,
) : AuditingEntity(id) {

    @Column(name = "email", nullable = false)
    var email: String = email
        protected set

    @Column(name = "password", nullable = false)
    var password: String = password
        protected set

    @Enumerated(EnumType.STRING)
    var role: Role = role
        protected set

    override fun toString(): String {
        return "Member(email='$email', password='$password', role=$role, createdAt=$createdAt, updatedAt=$updatedAt)"
    }

    companion object {
        fun createFakeMember(memberId: Long): Member {
            val member = Member(id = memberId, "admin@gmail.com", password = "1234")
            return member
        }
    }

    fun toDto(): MemberResponse {
        return MemberResponse(
            id = this.id!!,
            email = this.email,
            password = this.password,
            role = this.role,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
    }
}


enum class Role {
    USER, ADMIN
}