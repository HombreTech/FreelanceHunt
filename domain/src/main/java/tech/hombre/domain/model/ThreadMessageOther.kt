package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel

data class ThreadMessageOther(
    val `data`: ThreadMessageList.Data = ThreadMessageList.Data()
) : ViewModel