package ktplayground.func

sealed class FStream<out ITEM : Any> {
    object Empty : FStream<Nothing>()

    class Cons<out ITEM : Any>(head: () -> ITEM, tail: () -> FStream<ITEM>) : FStream<ITEM>() {
        private var mHead: @UnsafeVariance ITEM? = null
        private var mTail: @UnsafeVariance FStream<ITEM>? = null
        val head: () -> ITEM = { mHead ?: head().also { mHead = it } }
        val tail: () -> FStream<ITEM> = { mTail ?: tail().also { mTail = it } }

        override fun toString(): String {
            return "${this.head()}"
        }
    }

    companion object {
        operator fun <ITEM : Any> invoke(): FStream<ITEM> = Empty
        operator fun <ITEM : Any> invoke(
            head: () -> ITEM,
            tail: () -> FStream<ITEM>,
        ): FStream<ITEM> = Cons(head, tail)

        operator fun <ITEM : Any> invoke(vararg items: ITEM): FStream<ITEM> = items
            .foldRight(invoke()) { it, acc -> /* println(acc); */ Cons({ it }) { acc } }
    }
}

fun <ITEM : Any> FStream.Companion.constant(item: ITEM): FStream<ITEM> {
    return FStream({ item }) { constant(item) }
}

fun <ITEM : Any, OTHER : Any> FStream<ITEM>.fold(
    default: () -> OTHER,
    stream: (curr: () -> ITEM, acc: () -> OTHER) -> OTHER
): OTHER = if (this is FStream.Cons) stream(head) { tail().fold(default, stream) } else default()

fun <ITEM : Any> FStream<ITEM>.toFList(): FList<ITEM> {
    return fold<ITEM, FList<ITEM>>(
        default = { FList() },
        stream = { head, tail ->
            tail().append(head())
        }
    ).reverse()
}

fun <ITEM : Any> FStream<ITEM>.toList(): List<ITEM> {
    return fold(
        default = { listOf<ITEM>() },
        stream = { head, tail ->
            tail().plus(head())
        }
    ).reversed()
}

fun <ITEM : Any> FStream<ITEM>.toListWithoutReverse(): List<ITEM> {
    return fold(
        default = { listOf() },
        stream = { head, tail ->
            listOf(head()).plus(tail())
        }
    )
}

fun <ITEM : Any> FStream<ITEM>.toFListWithoutReverse(): FList<ITEM> {
    return fold(
        default = { FList() },
        stream = { head, tail ->
            FList.Cons(head(), tail())
        }
    )
}

fun <ITEM : Any> FStream<ITEM>.headOption(): FOption<ITEM> {
    return fold({ FOption() }) { head, _ -> FOption(head()) }
}

fun <ITEM : Any, OTHER : Any> FStream<ITEM>.map(fcn: (ITEM) -> OTHER): FStream<OTHER> {
    return fold({ FStream() }) { head, tail ->
        FStream.Cons({ fcn(head()) }, tail)
    }
}

fun <ITEM : Any> FStream<ITEM>.take(n: Int): FStream<ITEM> {
    return if (this is FStream.Cons && n > 0) FStream(head) { tail().take(n - 1) } else FStream()
}

tailrec fun <ITEM : Any> FStream<ITEM>.drop(n: Int): FStream<ITEM> {
    return if (this is FStream.Cons && n > 0) tail().drop(n - 1) else this
}

fun sequenceFrom(item: Int): FStream<Int> {
    return FStream({ item }) { sequenceFrom(item + 1) }
}

fun <ITEM : Any> FStream<ITEM>.takeWhile(predicate: (ITEM) -> Boolean): FStream<ITEM> {
    return if (this is FStream.Cons && predicate(head())) FStream(head) { tail().takeWhile(predicate) } else FStream()
}

fun <ITEM : Any> FStream<ITEM>.takeWhileUsingFold(predicate: (ITEM) -> Boolean): FStream<ITEM> {
    return fold({ FStream() }) { head, tail -> if (predicate(head())) FStream(head, tail) else FStream() }
}

fun <ITEM : Any> FStream<ITEM>.any(fcn: (ITEM) -> Boolean): Boolean {
    return fold({ false }) { head, tail -> fcn(head()) || tail() }
}

fun <ITEM : Any> FStream<ITEM>.all(fcn: (ITEM) -> Boolean): Boolean {
    return fold({ true }) { head, tail -> fcn(head()) && tail() }
}

