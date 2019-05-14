package com.chrynan.wordpress

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.stream.appendHTML
import java.io.File
import java.io.FileWriter

suspend fun generateTemplateOutputFile(themeRootLocation: String, template: HtmlTemplate) {
    withContext(Dispatchers.IO) {
        val file = File("$themeRootLocation/${template.location}", template.name)

        file.parentFile.mkdirs()

        val writer = FileWriter(file)

        val output = buildString {
            appendHTML(prettyPrint = true).run {
                template.run { layout() }
            }
        }

        writer.write(output)
        writer.close()
    }
}

suspend fun generatePhpOutputFile(themeRootLocation: String, script: PhpScriptTemplate) {
    withContext(Dispatchers.IO) {
        val file = File("$themeRootLocation/${script.location}", script.name)

        file.parentFile.mkdirs()

        val writer = FileWriter(file)

        val outputString = PhpBuilder().run {
            script.run {
                script()
            }
        }

        writer.write(outputString)
        writer.close()
    }
}

suspend fun generateStylesheetOutputFile(themeRootLocation: String, template: StyleTemplate) {
    withContext(Dispatchers.IO) {
        val file = File("$themeRootLocation/${template.location}/", template.name)

        file.parentFile.mkdirs()

        val css = template.style()

        css.renderToFile(file)
    }
}