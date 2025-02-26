package org.maximum0.blog.exception

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(
    val code: Long,
    val message: String,
) {

    INVALID_INPUT_VALUE(101, "Invalid Input Value"),
    ENTITY_NOT_FOUND(102, "Entity Not Found"),

}