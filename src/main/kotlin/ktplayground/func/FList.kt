package ktplayground.func

sealed class FList<out ITEM : Any> {
    object Nil : FList<Nothing>()
    data class Cons<out ITEM : Any> @PublishedApi internal constructor(
        inline val head: ITEM, inline val tail: FList<ITEM>
    ) : FList<ITEM>()

    companion object {
        operator fun <ITEM : Any> invoke(): FList<ITEM> = Nil
        operator fun <ITEM : Any> invoke(vararg items: ITEM): FList<ITEM> =
            items.foldRight(invoke()) { head, tail -> Cons(head, tail) }
    }

    fun <ITEM : Any> FList<ITEM>.appendFoldRight(item: ITEM): FList<ITEM> {
        return foldRight2(invoke(item), ::Cons)
    }


    fun <ITEM : Any> FList<FList<ITEM>>.flatten(): FList<ITEM> {
        return foldRight2(invoke()) { it, acc -> it.foldRight2(acc, ::Cons) }
    }

    fun <ITEM : Any> FList<FList<ITEM>>.flattenUsingDrop(): FList<ITEM> {
        return when (this) {
            is Cons -> drop().fold(head) { acc, item -> acc.merge(item) }
            is Nil -> Nil
        }
    }

    fun <ITEM : Any, OTHER : Any> FList<ITEM>.flatMap(block: (ITEM) -> FList<OTHER>): FList<OTHER> {
        return foldRight2(invoke()) { it, acc ->
            when (val v = block(it)) {
                is Cons -> v.foldRight2(acc, ::Cons)
                is Nil -> acc
            }
        }
    }
}

fun <ITEM: Any> FList<ITEM>.filterUsingFlatMap(fcn: (ITEM) -> Boolean): FList<ITEM> {
    return flatMap { if(fcn(it)) FList(it) else FList.Nil }
}

fun <ITEM: Any, OTHER: Any> FList<ITEM>.flatMapUsingMap(fcn: (ITEM) -> FList<OTHER>): FList<OTHER> {
    return map { it -> fcn(it) }.flatten()
}


fun <ITEM : Any> FList<ITEM>.filter(fcn: (ITEM) -> Boolean): FList<ITEM> {
    return foldRight2(FList()) { it, acc -> if (fcn(it)) FList.Cons(it, acc) else acc }
}

fun FList<Int>.increase(): FList<Int> {
    return map { it -> it + 1 }
}

fun <ITEM : Any> FList<FList<ITEM>>.flattenUsingDrop(): FList<ITEM> {
    return this.flattenUsingDrop()
}

fun <ITEM : Any> FList<FList<ITEM>>.flatten(): FList<ITEM> {
    return flatten()
}

fun <ITEM : Any> FList<ITEM>.appendFoldRight(item: ITEM): FList<ITEM> {
    return this.appendFoldRight(item)
}


fun <ITEM : Any> FList<ITEM>.setHead(item: ITEM): FList<ITEM> {
    return when (this) {
        is FList.Nil -> this
        is FList.Cons -> FList.Cons(item, this.tail)
    }
}

fun <ITEM : Any> FList<ITEM>.prepend(item: ITEM): FList<ITEM> {
    return FList.Cons(item, this)
}

fun <ITEM : Any> FList<ITEM>.getTail(): FList<ITEM> {
    return if (this is FList.Cons) tail else this
}

tailrec fun <ITEM : Any> FList<ITEM>.dropWhile(predicate: (ITEM) -> Boolean): FList<ITEM> {
    return if (this is FList.Cons && predicate(head)) tail.dropWhile(predicate) else this
}

fun <ITEM : Any> FList<ITEM>.dropWhileIndexed(index: Int, predicate: (Int) -> Boolean): FList<ITEM> {
    return if (this is FList.Cons && predicate(index)) tail.dropWhileIndexed(index + 1, predicate) else this
}

fun <ITEM : Any> FList<ITEM>.append(item: ITEM): FList<ITEM> {
    return when (this) {
        is FList.Cons -> FList.Cons(head, tail.append(item))
        is FList.Nil -> FList.Cons(item, FList.Nil)
    }
}

fun <ITEM : Any> FList<ITEM>.merge(list: FList<ITEM>): FList<ITEM> {
    return when (this) {
        is FList.Cons -> FList.Cons(head, tail.merge(list))
        is FList.Nil -> list
    }
}

fun <ITEM : Any> FList<ITEM>.copy(): FList<ITEM> {
    return merge(FList.Nil)
}

fun <ITEM : Any> FList<ITEM>.plus(list: FList<ITEM>): FList<ITEM> {
    return merge(list)
}

fun <ITEM : Any, ACC : Any> FList<ITEM>.foldRight(acc: ACC, fcn: (ACC, ITEM) -> ACC): ACC {
    return when (this) {
        is FList.Cons -> fcn(tail.foldRight(acc, fcn), head)
        is FList.Nil -> acc
    }
}

fun <ITEM : Any, ACC : Any> FList<ITEM>.foldRight2(acc: ACC, fcn: (ITEM, ACC) -> ACC): ACC {
    return when (this) {
        is FList.Cons -> fcn(head, tail.foldRight2(acc, fcn))
        is FList.Nil -> acc
    }
}

tailrec fun <ITEM : Any, ACC : Any> FList<ITEM>.foldRightTailRec(
    acc: ACC,
    fcn: (ACC, ITEM) -> ACC,
    stack: (ACC) -> ACC
): ACC {
    return when (this) {
        is FList.Cons -> when (tail) {
            is FList.Cons -> tail.foldRightTailRec(acc, fcn) { stack(fcn(it, head)) }
            is FList.Nil -> stack(fcn(acc, head))
        }

        is FList.Nil -> acc
    }
}

fun <ITEM : Any, ACC : Any> FList<ITEM>.fold(acc: ACC, fcn: (acc: ACC, item: ITEM) -> ACC): ACC {
    return when (this) {
        is FList.Cons -> tail.fold(fcn(acc, head), fcn)
        is FList.Nil -> acc
    }
}

fun <ITEM : Any> FList<ITEM>.reverse(): FList<ITEM> {
    return fold(FList()) { acc, it -> FList.Cons(it, acc) }
}

fun <ITEM : Any, ACC : Any> FList<ITEM>.foldRightUsingFold(
    zero: ACC,
    fcn: (acc: ACC, item: ITEM) -> ACC
): ACC {
    return reverse().fold(zero) { acc, it -> fcn(acc, it) }
}

fun <ITEM : Any> FList<ITEM>.appendUsingFold(item: ITEM): FList<ITEM> {
    return reverse().fold(FList(item)) { acc, it -> FList.Cons(it, acc) }
}

fun <ITEM : Any> FList<ITEM>.drop(): FList<ITEM> {
    return when (this) {
        is FList.Cons -> if (tail is FList.Nil) FList.Nil else FList.Cons(head, tail.drop())
        is FList.Nil -> this
    }
}

fun <ITEM : Any, OTHER: Any> FList<ITEM>.map(fcn: (ITEM) -> OTHER): FList<OTHER> {
    return foldRight2(FList()) { item, acc -> FList.Cons(fcn(item), acc) }
}

