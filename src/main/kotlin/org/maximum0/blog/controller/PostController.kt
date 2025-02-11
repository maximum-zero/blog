package org.maximum0.blog.controller

import org.maximum0.blog.domain.post.PostResponse
import org.maximum0.blog.service.PostService
import org.maximum0.blog.util.value.ComResDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService,
) {

    @GetMapping("/posts")
    fun findPosts(): ComResDto<List<PostResponse>> {
        return ComResDto(HttpStatus.OK, "Success", postService.findPosts())
    }

}