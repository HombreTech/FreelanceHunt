package tech.hombre.freelancehunt.ui.menu


import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.bottom_menu_create_thread.*
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.ui.base.BaseBottomDialogFragment


class CreateThreadBottomDialogFragment : BaseBottomDialogFragment() {

    override fun getLayout() = R.layout.bottom_menu_create_thread

    private var listener: OnCreateThreadListener? = null

    private var profileId = -1

    override fun viewReady() {
        arguments?.let {
            profileId = it.getInt(EXTRA_1, -1)

            buttonCreateThread.setOnClickListener {
                if (correctInputs()) {
                    listener?.onThreadCreated(
                        subject.savedText.toString(),
                        message.savedText.toString(),
                        profileId
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
        return subject.savedText.isNotEmpty() && message.savedText.isNotEmpty()
    }

    interface OnCreateThreadListener {
        fun onThreadCreated(
            subject: String,
            message: String,
            toProfileId: Int
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is OnCreateThreadListener) {
            listener = parentFragment as OnCreateThreadListener
        } else if (context is OnCreateThreadListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        val TAG = CreateThreadBottomDialogFragment::class.java.simpleName

        fun newInstance(profileId: Int): CreateThreadBottomDialogFragment {
            val fragment = CreateThreadBottomDialogFragment()
            val extra = Bundle()
            extra.putInt(EXTRA_1, profileId)
            fragment.arguments = extra
            return fragment
        }
    }
}