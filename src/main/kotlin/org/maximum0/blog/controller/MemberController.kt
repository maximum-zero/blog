package org.maximum0.blog.controller

import org.maximum0.blog.domain.member.MemberResponse
import org.maximum0.blog.service.MemberService
import org.maximum0.blog.util.value.ComResDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService,
){

    @GetMapping("/members")
    fun findAll(): ComResDto<List<MemberResponse>> {
        return ComResDto(HttpStatus.OK, "Success", memberService.findAll())
    }


}