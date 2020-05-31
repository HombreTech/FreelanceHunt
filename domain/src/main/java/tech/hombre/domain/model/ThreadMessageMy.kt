package tech.hombre.domain.model

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel

data class ThreadMessageMy(
    val `data`: ThreadMessageList.Data = ThreadMessageList.Data()
) : ViewModel