package ktplayground.func

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import ktplayground.func.FList.Nil.flatMap
import org.spekframework.spek2.Spek

class FListSpec : Spek({
    test("impl invoke") {
        assertEquals(FList(1, 2, 3), FList.Cons(1, FList.Cons(2, FList.Cons(3, FList.Nil))))
        assertNotEquals(FList(1, 2, 3), FList.Cons(1, FList.Cons(2, FList.Cons(4, FList.Nil))))
    }

    test("impl setHead") {
        assertEquals(FList(1, 2, 3).setHead(4), FList(4, 2, 3))
    }

    test("impl prepend") {
        assertEquals(FList(1, 2, 3).prepend(4), FList(4, 1, 2, 3))
    }

    test("impl getTail") {
        assertEquals(FList(1, 2, 3).getTail(), FList(2, 3))
    }

    test("impl getTail") {
        assertEquals(FList(2, 3), FList(1, 2, 3).getTail())
    }

    test("impl dropWhile") {
        assertEquals(FList(2, 3), FList(1, 2, 3).dropWhile { it -> it < 2 })
    }

    test("impl dropWhileIndexed") {
        assertEquals(FList(3), FList(1, 2, 3).dropWhileIndexed(0) { index -> index < 2 })
    }

    test("impl append") {
        assertEquals(FList(1, 2, 3, 4), FList(1, 2, 3).append(4))
    }

    test("impl merge") {
        assertEquals(FList(1, 2, 3), FList(1, 2).merge(FList(3)))
    }

    test("impl copy") {
        assertEquals(FList(1, 2), FList(1, 2).copy())
    }

    test("impl plus") {
        assertEquals(FList(1, 2, 3, 4), FList(1, 2).plus(FList(3, 4)))
    }

    test("impl foldRight") {
        assertEquals(6, FList(1, 2, 3).foldRightTailRec(0, { acc, it -> acc + it }, { it }))
    }

    test("impl foldRight") {
        assertEquals(6, FList(1, 2, 3).foldRightTailRec(0, { acc, it -> acc + it }, { it }))
    }

    test("impl fold") {
        assertEquals(6, FList(1, 2, 3).fold(0) { acc, it -> acc + it })
    }

    test("impl reverse") {
        assertEquals(FList(3, 2, 1), FList(1, 2, 3).reverse())
    }

    test("impl append using foldRight") {
        assertEquals(FList(1, 2, 3, 4), FList(1, 2, 3).appendFoldRight(4))
    }

    test("impl foldRight using fold") {
        assertEquals(6, FList(1, 2, 3).foldRightUsingFold(0) { acc, it -> acc + it })
    }

    test("impl append using fold") {
        assertEquals(FList(1, 2, 3, 4), FList(1, 2, 3).appendUsingFold(4))
    }

    test("impl flatten") {
        assertEquals(FList(1, 2, 3), FList(FList(1), FList(2), FList(3)).flatten())
    }

    test("impl drop") {
        assertEquals(FList(1, 2, 3), FList(1, 2, 3, 4).drop())
    }

    test("impl flatten using drop") {
        assertEquals(FList(1, 2, 3), FList(FList(1), FList(2), FList(3)).flatten())
    }

    test("impl map") {
        assertEquals(FList(3, 6, 9), FList(1, 2, 3).map { it -> it * 3 })
    }

    test("impl increase using map") {
        assertEquals(FList(2, 3, 4), FList(1, 2, 3).increase())
    }

    test("impl filter") {
        assertEquals(FList(1, 2, 3), FList(1, 2, 3, 4).filter { it -> it < 4 })
    }

    test("impl flatMap") {
        assertEquals(FList("1-", "2-", "3-"), FList(1, 2, 3).flatMap { it -> FList("${it}-") })
    }

    test("impl flatMap using map") {
        assertEquals(FList("1-", "2-", "3-"), FList(1, 2, 3).flatMapUsingMap { it -> FList("${it}-") })
    }

    test("impl filter using flatMap") {
        assertEquals(FList(1, 2, 3, 4), FList(1, 2, 3, 4, 5).filterUsingFlatMap { it -> it < 5 })
    }

    test("impl toList using fold") {
        assertEquals(listOf(1, 2, 3, 4), FList(1, 2, 3, 4).toFList())
    }

    test("impl zipWith") {
        assertEquals(listOf(1, 4, 9), FList(1, 2, 3).zipWith(FList(1, 2, 3)) { a, b -> a * b }.toFList())
    }
})

