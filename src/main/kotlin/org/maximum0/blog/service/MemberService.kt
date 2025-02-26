package org.maximum0.blog.service

import org.maximum0.blog.domain.member.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
){

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<MemberResponse> =
        memberRepository.findMembers(pageable).map {
            it.toDto()
        }

    @Transactional(readOnly = true)
    fun findById(id: Long): MemberResponse =
        memberRepository.findById(id).orElseThrow().toDto()

    @Transactional
    fun save(dto: MemberSaveRequest): MemberResponse =
        memberRepository.save(dto.toEntity()).toDto()


    @Transactional
    fun deleteById(id: Long) =
        memberRepository.deleteById(id)

}