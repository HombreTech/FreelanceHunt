package tech.hombre.freelancehunt.ui.menu


import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import kotlinx.android.synthetic.main.bottom_menu_complete_workspace.*
import tech.hombre.domain.model.CompleteGrades
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.utils.Utilities
import tech.hombre.freelancehunt.ui.base.BaseBottomDialogFragment

class CompleteWorkspaceBottomDialogFragment : BaseBottomDialogFragment() {

    override fun getLayout() = R.layout.bottom_menu_complete_workspace

    private var listener: OnCompleteDialogSubmitListener? = null

    private var ids = -1

    private var isComplete = false

    override fun viewReady() {
        arguments?.let {
            ids = it.getInt(EXTRA_1, -1)
            isComplete = it.getBoolean(EXTRA_2, false)

            qualityValue.filters = arrayOf(Utilities.InputFilterMinMax(1, 10))
            professionalismValue.filters = arrayOf(Utilities.InputFilterMinMax(1, 10))
            costValue.filters = arrayOf(Utilities.InputFilterMinMax(1, 10))
            connectivityValue.filters = arrayOf(Utilities.InputFilterMinMax(1, 10))
            scheduleValue.filters = arrayOf(Utilities.InputFilterMinMax(1, 10))

            buttonSubmit.setOnClickListener {
                if (correctInputs()) {
                    listener?.onCompleteDialogSubmit(
                        ids,
                        isComplete,
                        text.savedText.toString(),
                        CompleteGrades(
                            qualityValue.text.toString().toInt(),
                            professionalismValue.text.toString().toInt(),
                            costValue.text.toString().toInt(),
                            connectivityValue.text.toString().toInt(),
                            scheduleValue.text.toString().toInt()
                        )
                    )
                    dismiss()
                } else {
                    showError(getString(R.string.check_inputs))
                }
            }
            return
        }
        error(getString(R.string.init_error))
    }

    private fun correctInputs(): Boolean {
        return text.savedText.isNotEmpty() && !qualityValue.text.isNullOrEmpty() && !professionalismValue.text.isNullOrEmpty() && !costValue.text.isNullOrEmpty() && !connectivityValue.text.isNullOrEmpty() && !scheduleValue.text.isNullOrEmpty()
    }

    interface OnCompleteDialogSubmitListener {
        fun onCompleteDialogSubmit(
            primaryId: Int,
            isComplete: Boolean,
            review: String,
            grades: CompleteGrades
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is OnCompleteDialogSubmitListener) {
            listener = parentFragment as OnCompleteDialogSubmitListener
        } else if (context is OnCompleteDialogSubmitListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @Keep
        val TAG = CompleteWorkspaceBottomDialogFragment::class.java.simpleName

        fun newInstance(primaryId: Int, isComplete: Boolean): CompleteWorkspaceBottomDialogFragment {
            val fragment = CompleteWorkspaceBottomDialogFragment()
            val extra = Bundle()
            extra.putInt(EXTRA_1, primaryId)
            extra.putBoolean(EXTRA_2, isComplete)
            fragment.arguments = extra
            return fragment
        }
    }
}