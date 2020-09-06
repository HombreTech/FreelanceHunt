package tech.hombre.freelancehunt.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import tech.hombre.data.local.LocalProperties
import tech.hombre.freelancehunt.common.EMPTY_STRING
import tech.hombre.freelancehunt.common.extensions.toast


abstract class BaseBottomDialogFragment : BottomSheetDialogFragment() {

    protected val appPreferences: LocalProperties by inject { parametersOf(this) }

    abstract fun viewReady()

    abstract fun getLayout(): Int

    protected var is_cancelable: Boolean = true

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
        dialog?.setCanceledOnTouchOutside(is_cancelable)
        dialog?.setCancelable(is_cancelable)
    }


    fun showError(errorMessage: String?) =
        toast(errorMessage ?: EMPTY_STRING)
}