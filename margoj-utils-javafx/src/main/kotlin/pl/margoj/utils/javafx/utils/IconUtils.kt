package pl.margoj.utils.javafx.utils

import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyProperty
import javafx.embed.swing.SwingFXUtils
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.image.ImageView
import org.apache.logging.log4j.LogManager
import java.awt.image.BufferedImage
import java.io.IOException
import java.util.HashMap
import javax.imageio.ImageIO

@Suppress("LoopToCallChain")
object IconUtils
{
    private val logger = LogManager.getLogger(IconUtils::javaClass)
    private val iconCache = HashMap<String, ImageView>()

    var iconClassLoaderSource: ClassLoader? = null
    
    fun createBinding(graphicProperty: ObjectProperty<Node>, selectedProperty: ReadOnlyProperty<Boolean>, layer: String)
    {
        logger.trace("createBinding(graphicProperty = $graphicProperty, selectedProperty = $selectedProperty, layer = $layer)")
        graphicProperty.value = getIcon(layer, selectedProperty.value!!)
        selectedProperty.addListener { _, _, newValue -> graphicProperty.setValue(getIcon(layer, newValue!!)) }
    }

    fun removeDefaultClass(node: Node, defaultClass: String)
    {
        logger.trace("removeDefaultClass(node = $node, selectedProperty = $defaultClass)")

        node.styleClass.remove(defaultClass)
    }

    fun addTooltip(element: Control, text: String)
    {
        element.tooltip = Tooltip(text)
    }

    fun getIcon(name: String, selected: Boolean, rgb: Int = 0xFF66FFFF.toInt()): ImageView
    {
        val cacheKey = name + if (selected) "_selected" else ""

        val test = iconCache[cacheKey]
        if (test != null)
        {
            return test
        }

        val image: BufferedImage
        try
        {
            image = ImageIO.read(this.iconClassLoaderSource!!.getResourceAsStream("icons/$name.png"))
        }
        catch (e: IOException)
        {
            throw RuntimeException("Couldn't get icon: " + cacheKey)
        }

        if (selected)
        {
            for (x in 0 until image.width)
            {
                for (y in 0 until image.height)
                {
                    if (image.getRGB(x, y) == 0xFFFFFFFF.toInt())
                    {
                        image.setRGB(x, y, rgb)
                    }
                }
            }
        }

        val out = ImageView(SwingFXUtils.toFXImage(image, null))
        iconCache.put(cacheKey, out)
        return out
    }

    fun createFullIcon(control: Control, defaultClass: String, tooltip: String, graphics: ObjectProperty<Node>, selected: ReadOnlyProperty<Boolean>, icon: String)
    {
        IconUtils.removeDefaultClass(control, defaultClass)
        IconUtils.addTooltip(control, tooltip)
        IconUtils.createBinding(graphics, selected, icon)

        if (control is Labeled)
        {
            control.text = ""
        }
    }

    fun createFullCheckbox(checkbox: CheckBox, tooltip: String, icon: String)
    {
        IconUtils.createFullIcon(
                control = checkbox,
                defaultClass = "check-box",
                tooltip = tooltip,
                graphics = checkbox.graphicProperty(),
                selected = checkbox.selectedProperty(),
                icon = icon
        )
    }

    fun createFullButton(button: Button, tooltip: String, icon: String)
    {
        IconUtils.createFullIcon(
                control = button,
                defaultClass = "button",
                tooltip = tooltip,
                graphics = button.graphicProperty(),
                selected = button.armedProperty(),
                icon = icon
        )
    }
}
