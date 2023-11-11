package ktplayground.func

import kotlin.test.assertEquals
import org.spekframework.spek2.Spek

class FStreamSpec : Spek({
    test("impl toFList") {
        assertEquals(FList(1, 2, 3), FStream(1, 2, 3).toFList())
    }

    test("impl toList") {
        assertEquals(listOf(1, 2, 3), FStream(1, 2, 3).toList())
    }

    test("impl toFList without reverse") {
        assertEquals(listOf(1, 2, 3), FStream(1, 2, 3).toListWithoutReverse())
    }

    test("impl toFList without reverse") {
        assertEquals(FList(1, 2, 3), FStream(1, 2, 3).toFListWithoutReverse())
    }

    test("impl headOption") {
        assertEquals(FOption(1), FStream(1, 2, 3).headOption())
    }

    test("impl map") {
        assertEquals(FStream(2, 4, 6).headOption(), FStream(1, 2, 3).map { it * 2 }.headOption())
        assertEquals(FStream(2, 4, 6).toList(), FStream(1, 2, 3).map { it * 2 }.toList())
        assertEquals(FStream(2, 4, 6).toFList(), FStream(1, 2, 3).map { it * 2 }.toFList())
    }

    test("impl const && take") {
        assertEquals(listOf(1, 1), FStream.constant(1).take(5).drop(3).toList())
    }

    test("impl sequenceFrom") {
        assertEquals(6, sequenceFrom(1)
            .take(3)
            .toFList()
            .fold(0) { acc, item -> acc + item }
        )
    }

    test("impl takeWhile") {
        assertEquals(listOf(1, 2, 3), sequenceFrom(1).takeWhile { it < 4 }.toList())
    }

    test("impl takeWhileUsingFold") {
        assertEquals(listOf(1, 2, 3), sequenceFrom(1).takeWhileUsingFold { it < 4 }.toList())
    }

    test("impl any") {
        assertEquals(true, FStream(1, 2, 3).any { it < 2 })
    }

    test("impl all") {
        assertEquals(false, FStream(1, 2, 3).all { it < 2 })
    }
})

