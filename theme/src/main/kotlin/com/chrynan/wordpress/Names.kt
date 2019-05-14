package com.chrynan.wordpress

object DefaultTemplateNames {

    const val INDEX = "index.php"
    const val COMMENTS = "comments.php"
    const val FRONT_PAGE = "front-page.php"
    const val HOME = "home.php"
    const val HEADER = "header.php"
    const val SINGULAR = "singular.php"
    const val SINGLE = "single.php"
    const val PAGE = "page.php"
    const val CATEGORY = "category.php"
    const val TAG = "tag.php"
    const val TAXONOMY = "taxonomy.php"
    const val AUTHOR = "author.php"
    const val DATE = "date.php"
    const val ARCHIVE = "archive.php"
    const val SEARCH = "search.php"
    const val ATTACHMENT = "attachment.php"
    const val IMAGE = "image.php"
    const val PROJECT = "project.php"
    const val FUNCTIONS = "functions.php"
    const val NOT_FOUND = "404.php"

    fun page(slug: String) = "$PAGE-$slug"

    fun archive(postType: String) = "$ARCHIVE-$postType"

    fun single(postType: String) = "$SINGLE-$postType"

    fun project(slug: String) = "$PROJECT-$slug"
}

object DefaultPartialNames {

    const val HEADER = "header.php"
    const val FOOTER = "footer.php"
    const val SIDEBAR = "sidebar.php"
}

object DefaultCssFileNames {

    const val STYLE = "style.css"
    const val RTL = "rtl.css"
}