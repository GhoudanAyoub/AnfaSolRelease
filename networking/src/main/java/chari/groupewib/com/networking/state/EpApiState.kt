package ghoudan.anfaSolution.com.networking.state


sealed class EpApiState<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : EpApiState<T>(data)
    class Loading<T>(data: T? = null) : EpApiState<T>(data)
    class Failure<T>(data: T? = null) : EpApiState<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null) : EpApiState<T>(data, throwable)
}
