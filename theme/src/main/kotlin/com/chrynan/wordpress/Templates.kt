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

interface PartialHtmlTemplate {

    fun <T> TagConsumer<T>.layout(): T
}

interface HtmlTemplate : Template,
    PartialHtmlTemplate

fun HTMLTag.include(template: PartialHtmlTemplate) {
    +buildString {
        appendHTML(prettyPrint = true).apply {
            template.apply {
                layout()
            }
        }
    }
}

fun include(template: PartialHtmlTemplate) =
    buildString {
        appendHTML(prettyPrint = true).apply {
            template.apply {
                layout()
            }
        }
    }

open class PhpBuilder {

    protected val sb = StringBuilder()

    operator fun plus(php: String) {
        sb.append(php)
    }

    operator fun plus(template: HtmlTemplate) {
        sb.appendHTML(prettyPrint = true).apply {
            template.apply {
                layout()
            }
        }
    }

    operator fun plus(template: PartialHtmlTemplate) {
        sb.appendHTML(prettyPrint = true).apply {
            template.apply {
                layout()
            }
        }
    }

    internal open fun build() = "<?php \n $sb \n ?>"
}

class PhpConditionBuilder(private val condition: String) : PhpBuilder() {

    override fun build() =
        """
            <?php if ( $condition ) : ?>
                $sb
            <?php endif; ?>
        """
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

fun HTMLTag.php(builder: PhpBuilder.() -> Unit) {
    val phpBuilder = PhpBuilder()
    builder.invoke(phpBuilder)
    unsafe { raw(phpBuilder.build()) }
}

fun HTMLTag.php(template: PhpScriptTemplate) {
    val phpBuilder = PhpBuilder()
    phpBuilder.apply { template.apply { script() } }
    unsafe { raw(phpBuilder.build()) }
}

fun HTMLTag.phpIf(condition: String, builder: PhpConditionBuilder.() -> Unit) {
    val phpBuilder = PhpConditionBuilder(condition)
    builder.invoke(phpBuilder)
    unsafe { raw(phpBuilder.build()) }
}

fun HTMLTag.phpIf(condition: String, template: PhpScriptTemplate) {
    val phpBuilder = PhpConditionBuilder(condition)
    phpBuilder.apply { template.apply { script() } }
    unsafe { raw(phpBuilder.build()) }
}

fun php(builder: PhpBuilder.() -> Unit): String {
    val phpBuilder = PhpBuilder()
    builder.invoke(phpBuilder)
    return phpBuilder.build()
}

fun php(template: PhpScriptTemplate): String {
    val phpBuilder = PhpBuilder()
    phpBuilder.apply { template.apply { script() } }
    return phpBuilder.build()
}

fun phpIf(condition: String, builder: PhpConditionBuilder.() -> String): String {
    val phpBuilder = PhpConditionBuilder(condition)
    builder.invoke(phpBuilder)
    return phpBuilder.build()
}

fun phpIf(condition: String, template: PhpScriptTemplate): String {
    val phpBuilder = PhpConditionBuilder(condition)
    phpBuilder.apply { template.apply { script() } }
    return phpBuilder.build()
}

interface StyleTemplate : Template {

    fun style(): Stylesheet
}