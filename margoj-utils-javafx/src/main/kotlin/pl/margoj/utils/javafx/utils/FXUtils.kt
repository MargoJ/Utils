package pl.margoj.utils.javafx.utils

import javafx.scene.control.Alert
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.stage.Modality
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import pl.margoj.utils.javafx.api.CustomScene
import pl.margoj.utils.javafx.operation.Operation
import pl.margoj.utils.javafx.scene.DialogScene
import pl.margoj.utils.javafx.scene.OperationScene
import javax.imageio.ImageIO

object FXUtils
{
    private val logger = LogManager.getLogger(FXUtils::class.java)

    private fun isValidNumber(str: String, negative: Boolean, empty: Boolean = false): Boolean
    {
        if (empty && str.isEmpty())
        {
            return true
        }

        val chars = str.toCharArray()
        if (chars.isEmpty())
        {
            return false
        }
        var dot = false
        for (i in 0 until str.length)
        {
            if (chars[i] == '-')
            {
                if (i != 0 || !negative)
                {
                    return false
                }
            }
            else if (chars[i] == '.')
            {
                if (dot)
                {
                    return false
                }
                else
                {
                    dot = true
                }
            }
            else if (!Character.isDigit(chars[i]))
            {
                return false
            }
        }

        return true
    }

    fun makeNumberField(field: TextField, negative: Boolean, empty: Boolean = false)
    {
        if (!isValidNumber(field.text, negative, empty))
        {
            field.text = "0"
        }

        field.textProperty().addListener { _, oldValue, newValue ->
            if (newValue.isEmpty())
            {
                if (!empty)
                {
                    field.text = newValue
                }
            }
            else if (!isValidNumber(newValue, negative, empty))
            {
                field.text = oldValue
            }
        }
    }

    fun setAlertIcon(alert: Alert, icon: Image)
    {
        (alert.dialogPane.scene.window as Stage).icons.setAll(icon)
    }

    fun setStageIcon(stage: Stage, iconName: String)
    {
        stage.icons.setAll(Image(CustomScene::class.java.classLoader.getResourceAsStream(iconName)))
    }

    fun loadDialog(classLoader: ClassLoader, resource: String, title: String, owner: Stage? = null, data: Any? = null): Stage
    {
        logger.trace("loadDialog(resources = $resource, title = $title, owner = $owner, data = $data)")
        val scene = DialogScene("dialog/" + resource, title, data)
        val stage = Stage()
        if (owner != null)
        {
            stage.initModality(Modality.WINDOW_MODAL)
            stage.initOwner(owner)
        }

        scene.stage = stage
        scene.load(classLoader)
        return stage
    }

    fun startOperation(operation: Operation<*>, owner: Stage? = null)
    {
        val scene = OperationScene(operation)
        val stage = Stage()

        if (owner != null)
        {
            stage.initModality(Modality.WINDOW_MODAL)
            stage.initOwner(owner)
        }

        scene.stage = stage
        scene.load(FXUtils::class.java.classLoader)
        stage.show()
    }

    fun showMultipleErrorsAlert(header: String, errors: List<String>)
    {
        logger.trace("showMultipleErrorsAlert(header = $header, errors = $errors)")

        if (errors.isEmpty())
        {
            return
        }

        QuickAlert.create().error().header(header).content("Popraw następujące błedy: \n - " + errors.joinToString("\n - ")).showAndWait()
    }

    fun loadIcon(name: String): Image
    {
        return Image(FXUtils::class.java.classLoader.getResourceAsStream("icons/$name"))
    }

    fun loadAwtImage(name: String): java.awt.Image
    {
        return ImageIO.read(FXUtils::class.java.classLoader.getResourceAsStream("icons/$name"))
    }

    fun pannableOnScrollclick(event: MouseEvent, pane: ScrollPane): Boolean
    {
        if (event.button != MouseButton.MIDDLE)
        {
            return false
        }
        if (event.eventType == MouseEvent.MOUSE_PRESSED)
        {
            pane.isPannable = true
        }
        else if (event.eventType == MouseEvent.MOUSE_RELEASED)
        {
            pane.isPannable = false
        }
        return true
    }
}
