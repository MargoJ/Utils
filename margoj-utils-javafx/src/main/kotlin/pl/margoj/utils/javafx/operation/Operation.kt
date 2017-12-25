package pl.margoj.utils.javafx.operation

interface Operation<T: Operation<T>>
{
    val name: String

    fun start(operationCallback: OperationCallback<T>)
}