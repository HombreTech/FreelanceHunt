package tech.hombre.freelancehunt.ui.menu


import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import kotlinx.android.synthetic.main.bottom_menu_propose_conditions.*
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.WorkspaceDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.*
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

    override fun viewReady() {
        arguments?.let {
            ids = it.getInt(EXTRA_1, -1)
            val budget_con =
                it.getParcelable(EXTRA_2) as WorkspaceDetail.Data.Attributes.Conditions.Budget
            val safe_type = it.getString(EXTRA_3)
            safe = SafeType.values().find { it.type == safe_type }
            budget = MyBidsList.Data.Attributes.Budget(budget_con.amount, budget_con.currency)
            day = it.getInt(EXTRA_4, -1)

            costValue.setText(budget.amount.toString())
            costType.setSelection(
                CurrencyType.values().find { it.currency == budget.currency }?.ordinal ?: 0
            )
            safeType.setSelection(
                SafeType.values().find { it.type == safe!!.type }?.ordinal ?: 0
            )
            days.setText(day.toString())

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
        val costVerified: Boolean = when {
            currency == CurrencyType.UAH && cost < 200 -> {
                showError(getString(R.string.safe_cost_minimal))
                return false
            }
            currency == CurrencyType.RUB && cost < 600 -> {
                showError(getString(R.string.safe_cost_minimal))
                return false
            }
            else -> true
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

        fun newInstance(
            ids: Int,
            budget: WorkspaceDetail.Data.Attributes.Conditions.Budget,
            safe: String,
            days: Int
        ): ProposeConditionsBottomDialogFragment {
            val fragment = ProposeConditionsBottomDialogFragment()
            val extra = Bundle()
            extra.putInt(EXTRA_1, ids)
            extra.putParcelable(EXTRA_2, budget)
            extra.putString(EXTRA_3, safe)
            extra.putInt(EXTRA_4, days)
            fragment.arguments = extra
            return fragment
        }
    }
}