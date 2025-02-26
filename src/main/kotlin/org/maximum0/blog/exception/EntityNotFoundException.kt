package org.maximum0.blog.exception

sealed class EntityNotFoundException(message: String?): CustomException(message) {}

class MemberNotFoundException(id: Long): EntityNotFoundException("$id not Found") {}