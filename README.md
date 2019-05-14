A simple tool to assist in the creation of Wordpress Themes using the Kotlin programming language.

## About

This tool provides a way to write Wordpress Themes using Kotlin DSLs. The code executes and generates output that can be used as a Theme (as long as it's written in a way that is compatible with the Wordpress Theme API).
The code does not run on the server and therefore has no access to different API functions. Because of this, the Php code is written simply as a String. However, there also is the ability to copy a Php file into the theme output directory using the `PhpScriptFileTemplate` interface.

## Executing

* Create the templates

**index.php:**
```kotlin
object IndexTemplate : HtmlTemplate {
    
    override val location = DefaultLocations.ROOT
    
    override val name = DefaultTemplateNames.INDEX
    
    override fun <T> TagConsumer<T>.layout() = html {
        head { ... }
        body { 
            div {
                php("execute_some_wordpress_function('')")
            }
        }
    }
}
```
**functions.php:**
```kotlin
object FunctionsScript : PhpScriptTemplate {

    override val location = DefaultLocations.ROOT
    
    override val name = DefaultTemplateNames.FUNCTIONS
    
    override fun PhpBuilder.script() = php {
        """
            // Php Code is written as strings
            function phpCode() { ... }
        """
    }
}
```
**style.css:**
```kotlin
object StyleCss : StyleTemplate {

    override val location = DefaultLocations.ROOT
    
    override val name = DefaultCssFileNames.STYLE
    
    override fun style() = Stylesheet {
        div {
            backgroundColor = "#FFFFFF"
        }
    }
}
```

* Create the Theme object

```kotlin
object MyTheme : Theme {

    override val name = "MyTheme"
    
    override val rootLocation = "Theme-Output"
    
    override val versionCode = 1L
    
    override val versionName = "1.0.0"
    
    override val templates = listOf(IndexTemplate)
    
    override val phpScripts = listOf(FunctionsScript)
    
    override val styles = listOf(StyleCss)
}
```

* Create the Theme using the ThemeBuilder

```kotlin
fun main() {
    ThemeBuilder.buildTheme(theme = MyTheme)
}
```

The theme should be generated relative to the Project folder (for this case, the output would be "/Theme-Output/MyTheme/..").