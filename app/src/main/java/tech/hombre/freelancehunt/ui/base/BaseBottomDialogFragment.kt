package tech.hombre.freelancehunt.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tech.hombre.freelancehunt.common.EMPTY_STRING
import tech.hombre.freelancehunt.common.extensions.toast


abstract class BaseBottomDialogFragment : BottomSheetDialogFragment() {

    abstract fun viewReady()

    abstract fun getLayout(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewReady()
    }

    fun showError(errorMessage: String?) =
        toast(errorMessage ?: EMPTY_STRING)
}