package org.maximum0.blog.util.value

import org.springframework.http.HttpStatus

data class ComResDto<T>(
    val code: HttpStatus,
    val message: String,
    val data: T
)
