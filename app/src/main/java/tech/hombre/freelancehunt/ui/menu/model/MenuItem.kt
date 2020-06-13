package tech.hombre.freelancehunt.ui.menu.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MenuItem(
    val title: String,
    val tag: String,
    @DrawableRes val icon: Int
): ViewModel, Parcelable