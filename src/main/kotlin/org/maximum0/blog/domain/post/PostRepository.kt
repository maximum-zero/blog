package org.maximum0.blog.domain.post

import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>{
}