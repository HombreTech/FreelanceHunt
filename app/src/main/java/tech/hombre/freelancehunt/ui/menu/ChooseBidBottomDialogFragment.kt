package tech.hombre.freelancehunt.ui.menu


import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import kotlinx.android.synthetic.main.bottom_menu_choose_bid.*
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.ui.base.BaseBottomDialogFragment


class ChooseBidBottomDialogFragment : BaseBottomDialogFragment() {

    override fun getLayout() = R.layout.bottom_menu_choose_bid

    private var listener: OnChooseBidListener? = null

    private var projectId = -1

    private var bidId = -1

    override fun viewReady() {
        arguments?.let {
            projectId = it.getInt(EXTRA_1, -1)
            bidId = it.getInt(EXTRA_2, -1)

            buttonChooseBid.setOnClickListener {
                if (correctInputs()) {
                    listener?.onBidChoose(
                        projectId,
                        bidId,
                        comment.savedText.toString()
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
        return comment.savedText.isNotEmpty()
    }

    interface OnChooseBidListener {
        fun onBidChoose(
            projectId: Int,
            bidId: Int,
            comment: String
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is OnChooseBidListener) {
            listener = parentFragment as OnChooseBidListener
        } else if (context is OnChooseBidListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @Keep
        val TAG = ChooseBidBottomDialogFragment::class.java.simpleName

        fun newInstance(projectId: Int, bidId: Int): ChooseBidBottomDialogFragment {
            val fragment = ChooseBidBottomDialogFragment()
            val extra = Bundle()
            extra.putInt(EXTRA_1, projectId)
            extra.putInt(EXTRA_2, bidId)
            fragment.arguments = extra
            return fragment
        }
    }
}