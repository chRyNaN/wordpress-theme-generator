package com.chrynan.wordpress

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

object ThemeBuilder {

    fun buildTheme(theme: Theme) {
        runBlocking {
            theme.apply {
                val themeDirectory = File("${theme.rootLocation}/${theme.name}", theme.versionName)

                themeDirectory.mkdirs()

                launch {
                    templates.forEach {
                        generateTemplateOutputFile(
                            themeRootLocation = location,
                            template = it
                        )
                    }
                }

                launch {
                    phpScripts.forEach {
                        generatePhpOutputFile(
                            themeRootLocation = location,
                            script = it
                        )
                    }
                }

                launch {
                    styles.forEach {
                        generateStylesheetOutputFile(
                            themeRootLocation = location,
                            template = it
                        )
                    }
                }
            }
        }
    }
}