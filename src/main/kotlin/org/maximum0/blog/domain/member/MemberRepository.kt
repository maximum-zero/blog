package org.maximum0.blog.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor

interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {

}

interface MemberCustomRepository {

    fun findMembers(): List<Member>

}

class MemberCustomRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
    ): MemberCustomRepository {

    override fun findMembers(): List<Member> {
        return kotlinJdslJpqlExecutor.findAll {
            select(entity(Member::class))
                .from(entity(Member::class))
                .orderBy(path(Member::id).desc())
        }.filterNotNull()
    }

}