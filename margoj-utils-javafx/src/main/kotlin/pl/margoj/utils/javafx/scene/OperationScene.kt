package pl.margoj.utils.javafx.scene

import javafx.scene.Scene
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import pl.margoj.utils.javafx.api.CustomScene
import pl.margoj.utils.javafx.controller.OperationController
import pl.margoj.utils.javafx.operation.Operation

class OperationScene(val operation: Operation<*>) : CustomScene<OperationController<*>>("operation", operation)
{
    private val logger = LogManager.getLogger(this::class.java)

    override fun setup(stage: Stage, scene: Scene, controller: OperationController<*>)
    {
        logger.trace("setup(stage = $stage, scene = $scene, controller = $controller)")

        this.setIcon("icon.png")

        stage.title = "Operacja: ${this.operation.name}"

        stage.isResizable = false
        stage.sizeToScene()
    }
}