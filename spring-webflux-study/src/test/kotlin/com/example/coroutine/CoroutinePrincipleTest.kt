package com.example.coroutine

class CoroutinePrincipleTest {



    /**
     * 디컴파일해서 확인해보는 용도(마지막 인자에 Continuation이 들어간다)
     */
    private suspend fun f(a: Int, b: Int) = a + b
}
