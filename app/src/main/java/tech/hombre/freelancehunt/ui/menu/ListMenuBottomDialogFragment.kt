package tech.hombre.freelancehunt.ui.menu


import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.BaseViewRenderer
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter
import com.github.vivchar.rendererrecyclerviewadapter.ViewFinder
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer
import kotlinx.android.synthetic.main.bottom_menu_list.*
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.EXTRA_3
import tech.hombre.freelancehunt.ui.base.BaseBottomDialogFragment
import tech.hombre.freelancehunt.ui.menu.model.MenuItem


class ListMenuBottomDialogFragment : BaseBottomDialogFragment() {

    override fun getLayout() = R.layout.bottom_menu_list

    private var listener: BottomListMenuListener? = null

    private lateinit var adapter: RendererRecyclerViewAdapter

    private lateinit var items: ArrayList<MenuItem>

    private var projectId = 0

    private var bidId = 0

    override fun viewReady() {
        arguments?.let {
            items = it.getParcelableArrayList(EXTRA_1)
            projectId = it.getInt(EXTRA_2)
            bidId = it.getInt(EXTRA_3)
            adapter = RendererRecyclerViewAdapter()
            adapter.registerRenderer(
                ViewRenderer(
                    R.layout.item_menu_item,
                    MenuItem::class.java,
                    BaseViewRenderer.Binder { model: MenuItem, finder: ViewFinder, payloads: List<Any?>? ->
                        finder
                            .setImageResource(R.id.icon, model.icon)
                            .setText(R.id.title, model.title)
                            .setOnClickListener {
                                listener?.onMenuItemSelected(projectId, bidId, items.indexOf(model), model)
                                dismiss()
                            }
                    }
                )
            )
            list.layoutManager = LinearLayoutManager(activity)
            list.adapter = adapter
            adapter.setItems(items)
        }
    }


    interface BottomListMenuListener {
        fun onMenuItemSelected(
            projectId: Int,
            bidId: Int,
            position: Int,
            model: MenuItem
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is BottomListMenuListener) {
            listener = parentFragment as BottomListMenuListener
        } else if (context is BottomListMenuListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        val TAG = ListMenuBottomDialogFragment::class.java.simpleName

        fun newInstance(projectId: Int, bidId: Int, items: ArrayList<MenuItem>): ListMenuBottomDialogFragment {
            val fragment = ListMenuBottomDialogFragment()
            val extra = Bundle()
            extra.putParcelableArrayList(EXTRA_1, items)
            extra.putInt(EXTRA_2, projectId)
            extra.putInt(EXTRA_3, bidId)
            fragment.arguments = extra
            return fragment
        }
    }
}