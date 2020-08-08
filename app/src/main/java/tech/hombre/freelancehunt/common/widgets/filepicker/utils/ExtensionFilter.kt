package tech.hombre.freelancehunt.common.widgets.filepicker.utils

import tech.hombre.freelancehunt.common.widgets.filepicker.model.DialogConfigs
import tech.hombre.freelancehunt.common.widgets.filepicker.model.DialogProperties
import java.io.File
import java.io.FileFilter
import java.util.*

class ExtensionFilter(properties: DialogProperties) : FileFilter {
	private val validExtensions: Array<String>
	private val properties: DialogProperties
	override fun accept(file: File): Boolean {
		if (file.isDirectory && file.canRead()) {
			return true
		} else if (properties.selection_type == DialogConfigs.DIR_SELECT) {
			return false
		} else {
			val name = file.name.toLowerCase(Locale.getDefault())
			for (ext in validExtensions) {
				if (name.endsWith(ext, true)) {
					return true
				}
			}
		}
		return false
	}

	init {
		if (properties.extensions != null) {
			validExtensions = properties.extensions!!
		} else {
			validExtensions = arrayOf("")
		}
		this.properties = properties
	}
}