package org.maximum0.blog.config

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.serpro69.kfaker.faker
import org.maximum0.blog.domain.member.Member
import org.maximum0.blog.domain.member.MemberRepository
import org.maximum0.blog.domain.member.Role
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    private val memberRepository: MemberRepository,
){

    val faker = faker {  }
    private val logger = KotlinLogging.logger {}

    @EventListener(ApplicationReadyEvent::class)
    private fun init() {
        val member = Member(
            email = faker.internet.safeEmail(),
            password = faker.crypto.sha256(),
            role = Role.USER
        )

        logger.info { "create init data $member" }

        memberRepository.save(member)
    }

}