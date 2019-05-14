package com.chrynan.wordpress

interface Theme {

    val name: String

    val rootLocation: String

    val versionName: String

    val versionCode: Long

    val templates: List<HtmlTemplate>

    val phpScripts: List<PhpScriptTemplate>

    val styles: List<StyleTemplate>

    val location: String
        get() = "$rootLocation/$name/$versionName"
}