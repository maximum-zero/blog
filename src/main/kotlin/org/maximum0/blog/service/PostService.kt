package org.maximum0.blog.service

import org.maximum0.blog.domain.post.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
) {

    @Transactional(readOnly = true)
    fun findPosts(pageable: Pageable): Page<PostResponse> =
        postRepository.findPosts(pageable).map {
            it.toDto()
        }
}