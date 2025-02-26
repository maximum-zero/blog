package org.maximum0.blog.exception

sealed class CustomException : RuntimeException {

    constructor(message: String?):super(message) {

    }

}