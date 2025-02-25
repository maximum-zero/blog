package org.maximum0.blog.controller

import org.maximum0.blog.domain.post.PostResponse
import org.maximum0.blog.service.PostService
import org.maximum0.blog.util.value.ComResDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService,
) {

    @GetMapping("/posts")
    fun findPosts(@PageableDefault(size = 10) pageable: Pageable): ComResDto<Page<PostResponse>> {
        return ComResDto(HttpStatus.OK, "Success", postService.findPosts(pageable))
    }

}