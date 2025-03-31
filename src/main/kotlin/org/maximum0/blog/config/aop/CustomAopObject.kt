package org.maximum0.blog.config.aop

import io.github.oshai.kotlinlogging.KotlinLogging

object CustomAopObject {

    private val log = KotlinLogging.logger {  }


    /**
     * 일급 시민
     * 1. 인자를 넘겨줄 수 있다.
     * 2. 리턴타입으로 정의할 수 있다.
     * 3. 값에 할당할 수 있다.
     *
     * 고차 함수를 통해서, AOP를 구현해볼 수 있다.
     */
    fun highOrder(func: () -> Unit) {
        log.info { "before" }

        log.info { "after" }
    }

}
