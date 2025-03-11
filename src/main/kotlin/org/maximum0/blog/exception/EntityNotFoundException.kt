package org.maximum0.blog.exception

sealed class EntityNotFoundException(message: String?): CustomException(message) {}

class MemberNotFoundException(id: String): EntityNotFoundException("$id not Found") {}
