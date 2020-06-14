package tech.hombre.freelancehunt.ui.menu


import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.bottom_menu_add_bid.*
import tech.hombre.domain.model.MyBidsList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.CurrencyType
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.SafeType
import tech.hombre.freelancehunt.ui.base.BaseBottomDialogFragment


class AddBidBottomDialogFragment : BaseBottomDialogFragment() {

    override fun getLayout() = R.layout.bottom_menu_add_bid

    private var listener: OnBidAddedListener? = null

    private var ids = -1
    private var cost = 0
    private var day = 0
    private var budget = MyBidsList.Data.Attributes.Budget()
    private var safe: SafeType? = null
    private var comm = ""
    private var isHiddenBid = false
    private var isPlus = false

    override fun viewReady() {
        arguments?.let {
            isPlus = it.getBoolean(EXTRA_1)
            ids = it.getInt(EXTRA_2, -1)
            hiddenBid.isEnabled = isPlus
            buttonAddBid.setOnClickListener {
                if (correctInputs()) {
                    listener?.onBidAdded(
                        ids,
                        day,
                        budget,
                        safe!!,
                        comm,
                        isHiddenBid
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
        if (comm.length < 60) {
            showError(getString(R.string.bid_comment_min))
            return false
        }
        isHiddenBid = hiddenBid.isChecked
        return !(!costVerified || day < 1 || cost < 1 || comm.isEmpty() || safe == null)
    }

    interface OnBidAddedListener {
        fun onBidAdded(
            id: Int,
            days: Int,
            budget: MyBidsList.Data.Attributes.Budget,
            safeType: SafeType,
            comment: String,
            isHidden: Boolean
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is OnBidAddedListener) {
            listener = parentFragment as OnBidAddedListener
        } else if (context is OnBidAddedListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        val TAG = AddBidBottomDialogFragment::class.java.simpleName

        fun newInstance(isPlus: Boolean, ids: Int): AddBidBottomDialogFragment {
            val fragment = AddBidBottomDialogFragment()
            val extra = Bundle()
            extra.putBoolean(EXTRA_1, isPlus)
            extra.putInt(EXTRA_2, ids)
            fragment.arguments = extra
            return fragment
        }
    }
}