package tech.hombre.freelancehunt.common.extensions

fun getFileTypeByExtension(extension: String): String {
    return when (extension) {
        "docx" -> "doc"
        else -> extension
    }
}