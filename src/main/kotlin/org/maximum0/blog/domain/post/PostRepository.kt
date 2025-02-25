package org.maximum0.blog.domain.post

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.support.PageableExecutionUtils

interface PostRepository : JpaRepository<Post, Long>, PostCustomRepository {
}

interface PostCustomRepository {

    fun findPosts(pageable: Pageable): Page<Post>

}

class PostCustomRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
): PostCustomRepository {

    override fun findPosts(pageable: Pageable): Page<Post> {
        val results =  kotlinJdslJpqlExecutor.findPage(pageable) {
            select(entity(Post::class))
                .from(entity(Post::class))
                .orderBy(path(Post::id).desc())
        }.filterNotNull()

        val countQuery: () -> Long = {
            kotlinJdslJpqlExecutor.findAll {
                select(count(Post::id))
                    .from(entity(Post::class))
            }.firstOrNull() ?: 0L
        }

        return PageableExecutionUtils.getPage(results, pageable, countQuery)
    }

}