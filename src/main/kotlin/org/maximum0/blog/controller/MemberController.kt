package org.maximum0.blog.controller

import jakarta.validation.Valid
import org.maximum0.blog.domain.member.MemberResponse
import org.maximum0.blog.domain.member.LoginDto
import org.maximum0.blog.service.MemberService
import org.maximum0.blog.util.value.ComResDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/member")
@RestController
class MemberController(
    private val memberService: MemberService,
){

    @GetMapping("/list")
    fun findAll(@PageableDefault(size = 10) pageable: Pageable): ComResDto<Page<MemberResponse>> {
        return ComResDto(HttpStatus.OK, "Success", memberService.findAll(pageable))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ComResDto<MemberResponse> {
        return ComResDto(HttpStatus.OK, "Success", memberService.findById(id))
    }

    @PostMapping
    fun save(@Valid @RequestBody dto: LoginDto): ComResDto<MemberResponse> {
        return ComResDto(HttpStatus.OK, "Success", memberService.save(dto))
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ComResDto<Unit> {
        return ComResDto(HttpStatus.OK, "Success", memberService.deleteById(id))
    }


}