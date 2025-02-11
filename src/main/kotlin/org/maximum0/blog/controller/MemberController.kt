package org.maximum0.blog.controller

import org.maximum0.blog.domain.member.Member
import org.maximum0.blog.service.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService,
){

    @GetMapping("/members")
    fun findAll(): MutableList<Member> = memberService.findAll()



}