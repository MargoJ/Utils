package pl.margoj.utils.javafx.utils

import javafx.fxml.FXMLLoader
import javafx.scene.Node
import org.apache.logging.log4j.LogManager
import pl.margoj.utils.javafx.api.CustomController

class FXMLJarLoader(val classLoader: ClassLoader, val path: String)
{
    private val logger = LogManager.getLogger(FXMLJarLoader::class.java)

    lateinit var node: Node
        private set

    lateinit var controller: CustomController
        private set

    fun load()
    {
        logger.trace("load()")
        val loader = FXMLLoader()
        val location = "view/$path.fxml"
        logger.debug("Loading view: $location")
        loader.location = this.classLoader.getResource(location)
        this.node = loader.load()
        this.controller = loader.getController()

        logger.debug("node=${this.node}, controller=${this.controller}")
    }
}