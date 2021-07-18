package net.ferraSolution.food.data.client.network


sealed class UseCaseResult<out T: Any>{
    class ShowProgress
    class HideProgress
    class OnSuccess<out T: Any>(val data: T) : UseCaseResult<T>()
    class OnError(val exception: Throwable): UseCaseResult<Nothing>()
}