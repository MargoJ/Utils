package pl.margoj.utils.javafx.api

import javafx.fxml.Initializable

interface CustomController : Initializable
{
    fun loadData(data: Any)
    {
    }

    fun preInit(scene: CustomScene<*>)
    {
    }
}