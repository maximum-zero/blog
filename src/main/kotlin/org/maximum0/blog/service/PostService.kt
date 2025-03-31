package org.maximum0.blog.service

import org.maximum0.blog.domain.post.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
) {

//    @PreAuthorize("hasRole('SUPER')")
    @Secured("ROLE_SUPER", "ROLE_ADMIN")
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<PostResponse> =
        postRepository.findPosts(pageable).map {
            it.toDto()
        }

    @Transactional(readOnly = true)
    fun findById(id: Long): PostResponse =
        postRepository.findById(id).orElseThrow().toDto()

    @Transactional
    fun save(dto: PostSaveRequest): PostResponse =
        postRepository.save(dto.toEntity()).toDto()


    @Transactional
    fun deleteById(id: Long) =
        postRepository.deleteById(id)

}