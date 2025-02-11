package org.maximum0.blog.service

import org.maximum0.blog.domain.post.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
) {

    @Transactional(readOnly = true)
    fun findPosts(): List<PostResponse> =
        postRepository.findAll().map {
            it.toDto()
        }
}