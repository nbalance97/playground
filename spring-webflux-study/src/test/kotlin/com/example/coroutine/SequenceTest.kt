package com.example.coroutine

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SequenceTest {

    @Test
    fun sequence_builder를_테스트한다() {
        /**
         * sequence builder의 내부를 보면 Coroutine(Continuation 객체)을 사용하고 있는 것을 알 수 있음
         * SequenceBuilderIterator#next 참고
         */
        val sequence = sequence {
            yield(1)
        }

        val iterator = sequence.iterator()
        val value = iterator.next()

        assertThat(value).isEqualTo(1)
    }
}
