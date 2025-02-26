package org.maximum0.blog.controller

import org.maximum0.blog.domain.post.PostResponse
import org.maximum0.blog.domain.post.PostSaveRequest
import org.maximum0.blog.service.PostService
import org.maximum0.blog.util.value.ComResDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/post")
@RestController
class PostController(
    private val postService: PostService,
) {

    @GetMapping("/list")
    fun findPosts(@PageableDefault(size = 10) pageable: Pageable): ComResDto<Page<PostResponse>> {
        return ComResDto(HttpStatus.OK, "Success", postService.findAll(pageable))
    }


    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ComResDto<PostResponse> {
        return ComResDto(HttpStatus.OK, "Success", postService.findById(id))
    }

    @PostMapping
    fun save(@RequestBody dto: PostSaveRequest): ComResDto<PostResponse> {
        return ComResDto(HttpStatus.OK, "Success", postService.save(dto))
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ComResDto<Unit> {
        return ComResDto(HttpStatus.OK, "Success", postService.deleteById(id))
    }

}