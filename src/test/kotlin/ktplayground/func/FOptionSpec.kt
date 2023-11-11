package ktplayground.func

import kotlin.test.assertEquals
import org.spekframework.spek2.Spek

class FOptionSpec : Spek({
    test("impl getOrElse (not lazy)") {
        assertEquals(1, FOption(1).getOrElseNotLazy(0))
    }

    test("impl getOrElse (lazy)") {
        assertEquals(1, FOption(1).getOrElse { 0 })
    }

    test("impl orElse (lazy)") {
        assertEquals(FOption(1), FOption(1).orElse { 0 })
    }

    test("impl filter") {
        assertEquals(FOption(1), FOption(1).filter { it -> it > 0 })
    }
})

