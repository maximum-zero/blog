package org.maximum0.blog.exception

import org.springframework.validation.BindingResult

class ErrorResponse(
    errorCode: ErrorCode,
    var errors:List<FieldError> = ArrayList()
) {

    var code: Long = errorCode.code
        private set

    var message: String = errorCode.message
        private set

    class FieldError private constructor(
        val field: String,
        val value: String,
        val reason: String?,
    ) {
        companion object {

            fun of(bindingResult: BindingResult): List<FieldError> {

                val fieldErrors = bindingResult.fieldErrors
                return fieldErrors.map {
                    FieldError(
                        field = it.field,
                        value = if (it.rejectedValue == null) "" else it.rejectedValue.toString(),
                        reason = it.defaultMessage
                    )
                }

            }

        }

    }

    companion object {

        fun of (code: ErrorCode): ErrorResponse {
            return ErrorResponse(
                errorCode = code
            )
        }

        fun of (code: ErrorCode, bindingResult: BindingResult): ErrorResponse {
            return ErrorResponse(
                errorCode = code,
                errors = FieldError.of(bindingResult)
            )
        }

    }

}
