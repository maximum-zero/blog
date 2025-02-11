package org.maximum0.blog.config

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.serpro69.kfaker.faker
import org.maximum0.blog.domain.member.*
import org.maximum0.blog.domain.post.*
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
){

    val faker = faker {  }
    private val logger = KotlinLogging.logger {}

    @EventListener(ApplicationReadyEvent::class)
    private fun init() {

        val members = generateMembers(100)
        memberRepository.saveAll(members)
        val posts = generatePosts(100)
        postRepository.saveAll(posts)
    }

    private fun generateMembers(count: Int): MutableList<Member> {
        val members = mutableListOf<Member>()
        for (i in 1 .. count) {
            val member = generateMember()
            logger.info { "create init data $member" }
            members.add(member)
        }
        return members
    }
    private fun generateMember(): Member = MemberSaveRequest(
        email = faker.internet.safeEmail(),
        password = faker.crypto.sha256(),
        role = Role.USER,
    ).toEntity()

    private fun generatePosts(count: Int): MutableList<Post> {
        val posts = mutableListOf<Post>()
        for (i in 1 .. count) {
            val post = generatePost()
            logger.info { "create init data $post" }
            posts.add(post)
        }
        return posts
    }
    private fun generatePost(): Post = PostSaveRequest(
        title = faker.theExpanse.ships(),
        content = faker.quote.matz(),
        memberId = 1,
    ).toEntity()

}