package ktplayground.func

sealed class FOption<out VALUE : Any> {
    object None : FOption<Nothing>()
    data class Some<out VALUE : Any> constructor(val value: VALUE) : FOption<VALUE>()

    companion object {
        operator fun <VALUE : Any> invoke(): FOption<VALUE> = None
        operator fun <VALUE : Any> invoke(value: VALUE): FOption<VALUE> = Some(value)
    }
}

fun <VALUE : Any> FOption<VALUE>.getOrElseNotLazy(value: VALUE): VALUE {
    return when (this) {
        is FOption.Some -> this.value
        is FOption.None -> value
    }
}

fun <VALUE : Any> FOption<VALUE>.getOrElse(default: () -> VALUE): VALUE {
    return when (this) {
        is FOption.Some -> this.value
        is FOption.None -> default()
    }
}

fun <VALUE: Any> FOption<VALUE>.orElse(default: () -> VALUE): FOption<VALUE> {
    return when (this) {
        is FOption.Some -> this
        is FOption.None -> FOption(default())
    }
}

fun <VALUE: Any> FOption<VALUE>.filter(fcn: (VALUE) -> Boolean): FOption<VALUE> {
    return when (this) {
        is FOption.Some-> if (fcn(value)) this else FOption()
        is FOption.None -> this
    }
}

