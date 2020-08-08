package tech.hombre.domain.model

data class UploadedThreadMessage(
    val `data`: ThreadMessageList.Data = ThreadMessageList.Data(),
    val links: ThreadMessageList.Links = ThreadMessageList.Links()
)