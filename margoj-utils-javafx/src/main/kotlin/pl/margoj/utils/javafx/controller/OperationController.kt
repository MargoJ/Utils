package pl.margoj.utils.javafx.controller

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import org.apache.logging.log4j.LogManager
import pl.margoj.utils.javafx.api.CustomController
import pl.margoj.utils.javafx.api.CustomScene
import pl.margoj.utils.javafx.utils.QuickAlert
import pl.margoj.utils.javafx.operation.NotifyCancelOperation
import pl.margoj.utils.javafx.operation.Operation
import pl.margoj.utils.javafx.operation.OperationCallback
import java.net.URL
import java.util.ResourceBundle

class OperationController<T : Operation<T>> : CustomController, OperationCallback<T>
{
    private lateinit var operation: T
    private lateinit var scene: CustomScene<*>
    private lateinit var thread: Thread

    @FXML
    lateinit var labelCurrentOperation: Label

    @FXML
    lateinit var progressBar: ProgressBar

    @FXML
    lateinit var buttonClose: Button

    override fun loadData(data: Any)
    {
        @Suppress("UNCHECKED_CAST")
        this.operation = data as T

        this.thread = Thread(Runnable {
            this.operation.start(this)
        })
        this.thread.start()
    }

    override fun preInit(scene: CustomScene<*>)
    {
        this.scene = scene
    }

    override fun initialize(location: URL?, resources: ResourceBundle?)
    {
        this.buttonClose.setOnAction {
            val result = QuickAlert.create()
                    .confirmation()
                    .buttonTypes(ButtonType("Tak", ButtonBar.ButtonData.YES), ButtonType("Nie", ButtonBar.ButtonData.NO))
                    .header("Przerwać operacje: ${this.operation.name}?")
                    .content("Czy na pewno chcesz przerwać tę operację?")
                    .showAndWait()

            if (result?.buttonData == ButtonBar.ButtonData.YES)
            {
                (this.operation as? NotifyCancelOperation)?.notifyCancel()

                this.thread.interrupt()
                this.scene.stage.close()
            }
        }
    }

    override fun operationStarted(operation: T)
    {
        Platform.runLater {
            this.labelCurrentOperation.text = operation.name
        }
    }

    override fun operationProgress(operation: T, progress: Int, max: Int)
    {
        Platform.runLater {
            if (progress == -1)
            {
                this.progressBar.progress = ProgressBar.INDETERMINATE_PROGRESS
                return@runLater
            }

            this.progressBar.progress = (progress.toDouble() / max.toDouble())
        }
    }

    override fun operationError(operation: T, error: Throwable)
    {
        Platform.runLater {
            LogManager.getLogger("Operation-${operation.name}").error("An exception has occurred", error)
            QuickAlert.create().exception(error).content("Nie można wykonać operacji: ${operation.name}").showAndWait()
            this.scene.stage.close()
        }
    }

    override fun operationFinished(operation: T)
    {
        Platform.runLater {
            this.scene.stage.close()
        }
    }
}