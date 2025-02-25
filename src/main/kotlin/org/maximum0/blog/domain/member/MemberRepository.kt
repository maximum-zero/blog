package org.maximum0.blog.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {

}

interface MemberCustomRepository {

    fun findMembers(pageable: Pageable): Page<Member>

}

class MemberCustomRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
    ): MemberCustomRepository {

    override fun findMembers(pageable: Pageable): Page<Member> {
        val results =  kotlinJdslJpqlExecutor.findPage(pageable) {
            select(entity(Member::class))
                .from(entity(Member::class))
                .orderBy(path(Member::id).desc())
        }.filterNotNull()

        val countQuery: () -> Long = {
            kotlinJdslJpqlExecutor.findAll {
                select(count(Member::id))
                    .from(entity(Member::class))
            }.firstOrNull() ?: 0L
        }

        return PageableExecutionUtils.getPage(results, pageable, countQuery)
    }

}