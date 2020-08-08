package tech.hombre.data.networking.model

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream


class RequestBodyWithProgress(
    private val file: File,
    private val contentType: String,
    private val progressCallback: ((progress: Float) -> Unit)?
) : RequestBody() {

    override fun contentType(): MediaType? = MediaType.parse(contentType)

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        val fileLength = contentLength()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inSt = FileInputStream(file)
        var uploaded = 0L
        inSt.use {
            var read: Int = inSt.read(buffer)
            val handler = Handler(Looper.getMainLooper())
            while (read != -1) {
                progressCallback?.let {
                    uploaded += read
                    val progress = (uploaded.toDouble() / fileLength.toDouble()).toFloat()
                    handler.post { it(progress) }

                    sink.write(buffer, 0, read)
                }
                read = inSt.read(buffer)
            }
        }
    }
}