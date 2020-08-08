package tech.hombre.freelancehunt.common.widgets.filepicker.model

import java.io.File

class DialogProperties {
	var selection_mode: Int = DialogConfigs.SINGLE_MODE
	var selection_type: Int = DialogConfigs.FILE_SELECT
	var root: File = File(DialogConfigs.DEFAULT_DIR)
	var error_dir: File = File(DialogConfigs.DEFAULT_DIR)
	var offset: File = File(DialogConfigs.DEFAULT_DIR)
	var extensions: Array<String>? = arrayOf("gif","jpeg","jpg","png","pdf","psd","gz","7z","docx","doc","zip","rar","rtf","odt","ott","ods","sxw","ai","gzip","cdr","mp3","xlsx","xls","txt","pptx","ppt","css","c","h","js","ico","htm","html","csv","yml","json","avi","eps","sketch")
	var show_hidden_files: Boolean = false

}