package pl.margoj.utils.javafx.operation

interface OperationCallback<T : Operation<T>>
{
    fun operationStarted(operation: T)

    fun operationProgress(operation: T, progress: Int, max: Int)

    fun operationError(operation: T, error: Throwable)

    fun operationFinished(operation: T)
}