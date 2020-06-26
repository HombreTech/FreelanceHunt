package tech.hombre.freelancehunt.ui.menu


import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import kotlinx.android.synthetic.main.bottom_menu_propose_conditions.*
import tech.hombre.domain.model.MyBidsList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.CurrencyType
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.SafeType
import tech.hombre.freelancehunt.ui.base.BaseBottomDialogFragment


class ProposeConditionsBottomDialogFragment : BaseBottomDialogFragment() {

    override fun getLayout() = R.layout.bottom_menu_propose_conditions

    private var listener: OnConditionsListener? = null

    private var ids = -1
    private var cost = 0
    private var day = 0
    private var budget = MyBidsList.Data.Attributes.Budget()
    private var safe: SafeType? = null
    private var comm = ""
    private var isHiddenBid = false

    override fun viewReady() {
        arguments?.let {
            ids = it.getInt(EXTRA_1, -1)
            buttonAddConditions.setOnClickListener {
                if (correctInputs()) {
                    listener?.onConditionsChanged(
                        ids,
                        day,
                        budget,
                        safe!!,
                        comm
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
        cost = costValue.text.toString().toIntOrNull() ?: 0
        val currency = CurrencyType.values()[costType.selectedItemPosition]
        budget = MyBidsList.Data.Attributes.Budget(cost, currency.currency)
        day = days.text.toString().toIntOrNull() ?: 0
        safe = SafeType.values()[safeType.selectedItemPosition]
        var costVerified = true
        if (safe != SafeType.DIRECT_PAYMENT) {
            costVerified = when {
                currency == CurrencyType.UAH && cost < 200 -> {
                    showError(getString(R.string.safe_cost_minimal))
                    return false
                }
                currency == CurrencyType.RUR && cost < 600 -> {
                    showError(getString(R.string.safe_cost_minimal))
                    return false
                }
                else -> true
            }
        }
        if (!costVerified) return false
        comm = comment.savedText.toString()
        if (comm.isEmpty()) {
            showError(getString(R.string.propose_comment_min))
            return false
        }
        return !(!costVerified || day < 1 || cost < 1 || comm.isEmpty() || safe == null)
    }

    interface OnConditionsListener {
        fun onConditionsChanged(
            id: Int,
            days: Int,
            budget: MyBidsList.Data.Attributes.Budget,
            safeType: SafeType,
            comment: String
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is OnConditionsListener) {
            listener = parentFragment as OnConditionsListener
        } else if (context is OnConditionsListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @Keep
        val TAG = ProposeConditionsBottomDialogFragment::class.java.simpleName

        fun newInstance(ids: Int): ProposeConditionsBottomDialogFragment {
            val fragment = ProposeConditionsBottomDialogFragment()
            val extra = Bundle()
            extra.putInt(EXTRA_1, ids)
            fragment.arguments = extra
            return fragment
        }
    }
}