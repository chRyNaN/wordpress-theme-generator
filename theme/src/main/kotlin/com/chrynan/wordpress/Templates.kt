package com.chrynan.wordpress

import azadev.kotlin.css.Stylesheet
import kotlinx.html.HTMLTag
import kotlinx.html.TagConsumer
import kotlinx.html.stream.appendHTML
import kotlinx.html.unsafe
import java.io.File

interface Template {

    val name: String

    val location: String
}

interface HtmlTemplate : Template {

    fun <T> TagConsumer<T>.layout(): T
}

interface PartialHtmlTemplate : HtmlTemplate {

    override val location
        get() = ""

    override val name: String
        get() = ""
}

fun HTMLTag.include(template: HtmlTemplate) {
    +buildString {
        appendHTML(prettyPrint = true).apply {
            template.apply {
                layout()
            }
        }
    }
}

fun include(template: HtmlTemplate) =
    buildString {
        appendHTML(prettyPrint = true).apply {
            template.apply {
                layout()
            }
        }
    }

class PhpBuilder {

    fun include(template: HtmlTemplate): String =
        buildString {
            appendHTML(prettyPrint = true).apply {
                template.apply {
                    layout()
                }
            }
        }
}

interface PhpScriptTemplate : Template {

    fun PhpBuilder.script(): String
}

interface PhpScriptFileTemplate : PhpScriptTemplate {

    val inputFileLocation: String

    val inputFile: File
        get() = File(inputFileLocation)

    override fun PhpBuilder.script() = inputFile.inputStream().bufferedReader().use { it.readText() }
}

fun HTMLTag.php(builder: PhpBuilder.() -> String) {
    val phpBuilder = PhpBuilder()
    unsafe { raw("<?php \n ${builder.invoke(phpBuilder)} \n ?>") }
}

fun php(builder: PhpBuilder.() -> String): String {
    val phpBuilder = PhpBuilder()
    return "<?php \n ${builder.invoke(phpBuilder)} \n ?>"
}

fun HTMLTag.php(template: PhpScriptTemplate) {
    val phpBuilder = PhpBuilder()
    unsafe { raw("<?php \n ${phpBuilder.apply { template.apply { script() } }} \n ?>") }
}

fun php(template: PhpScriptTemplate): String {
    val phpBuilder = PhpBuilder()
    return "<?php \n ${phpBuilder.apply { template.apply { script() } }} \n ?>"
}

interface StyleTemplate : Template {

    fun style(): Stylesheet
}