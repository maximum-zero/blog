package org.maximum0.blog.controller

import org.maximum0.blog.domain.member.MemberResponse
import org.maximum0.blog.service.MemberService
import org.maximum0.blog.util.value.ComResDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService,
){

    @GetMapping("/members")
    fun findAll(@PageableDefault(size = 10) pageable: Pageable): ComResDto<Page<MemberResponse>> {
        return ComResDto(HttpStatus.OK, "Success", memberService.findAll(pageable))
    }


}