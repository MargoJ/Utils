package pl.margoj.utils.javafx.scene

import javafx.scene.Scene
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import pl.margoj.utils.javafx.api.CustomController
import pl.margoj.utils.javafx.api.CustomScene

class DialogScene(resource: String, val title: String, data: Any? = null) : CustomScene<CustomController>(resource, data)
{
    private val logger = LogManager.getLogger(this::javaClass)

    override fun setup(stage: Stage, scene: Scene, controller: CustomController)
    {
        logger.trace("setup(stage = $stage, scene = $scene, controller = $controller)")
        this.setIcon("icon.png")
        stage.title = this.title
        stage.isResizable = false
        stage.sizeToScene()
    }
}