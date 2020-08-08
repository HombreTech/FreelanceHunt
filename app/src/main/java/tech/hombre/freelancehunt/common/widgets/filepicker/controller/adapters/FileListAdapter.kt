package tech.hombre.freelancehunt.common.widgets.filepicker.controller.adapters

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.ATTACH_MAX_FILESIZE
import tech.hombre.freelancehunt.common.extensions.gone
import tech.hombre.freelancehunt.common.extensions.humanReadableBytes
import tech.hombre.freelancehunt.common.extensions.toast
import tech.hombre.freelancehunt.common.extensions.visible
import tech.hombre.freelancehunt.common.widgets.filepicker.controller.NotifyItemChecked
import tech.hombre.freelancehunt.common.widgets.filepicker.model.DialogConfigs
import tech.hombre.freelancehunt.common.widgets.filepicker.model.DialogProperties
import tech.hombre.freelancehunt.common.widgets.filepicker.model.FileListItem
import tech.hombre.freelancehunt.common.widgets.filepicker.model.MarkedItemList
import tech.hombre.freelancehunt.common.widgets.filepicker.widget.MaterialCheckbox
import tech.hombre.freelancehunt.common.widgets.filepicker.widget.OnCheckedChangeListener
import java.util.*


class FileListAdapter(
	private val listItem: ArrayList<FileListItem>, private val context: Context,
	private val properties: DialogProperties
) : BaseAdapter() {
	private var notifyItemChecked: NotifyItemChecked? = null
	override fun getCount(): Int {
		return listItem.size
	}

	override fun getItem(i: Int): FileListItem {
		return listItem[i]
	}

	override fun getItemId(i: Int): Long {
		return i.toLong()
	}

	override fun getView(i: Int, converView: View?, viewGroup: ViewGroup): View {
		val holder: ViewHolder
		var view = converView
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
				R.layout.dialog_file_list_item,
					viewGroup, false)
			holder = ViewHolder(view!!)
			view.tag = holder
		} else {
			holder = view.tag as ViewHolder
		}
		val item = listItem[i]
		if (MarkedItemList.hasItem(item.location)) {
			val animation = AnimationUtils.loadAnimation(
					context,
					R.anim.marked_item_animation
			)
			view.animation = animation
		} else {
			val animation = AnimationUtils.loadAnimation(
					context,
					R.anim.unmarked_item_animation
			)
			view.animation = animation
		}
		if (item.isDirectory) {
			holder.type_icon.setImageResource(R.drawable.folder)
			if (properties.selection_type == DialogConfigs.FILE_SELECT) {
				holder.checkbox.visibility = View.INVISIBLE
			} else {
				holder.checkbox.visibility = View.VISIBLE
			}
			holder.size.gone()
		} else {
			holder.type_icon.setImageResource(R.drawable.file)
			if (properties.selection_type == DialogConfigs.DIR_SELECT) {
				holder.checkbox.visibility = View.INVISIBLE
			} else {
				holder.checkbox.visibility = View.VISIBLE
			}
			holder.size.text = String.format(context.getString(R.string.size), item.size.humanReadableBytes())
			holder.size.visible()
		}
		holder.type_icon.contentDescription = item.filename
		holder.name.text = item.filename
		val dateFormatter = DateFormat.getMediumDateFormat(context)
		val timeFormatter = DateFormat.getTimeFormat(context)
		val date = Date(item.time)
		if (i == 0 && item.filename.startsWith(context.getString(R.string.label_parent_dir))) {
			holder.type.setText(R.string.label_parent_directory)
		} else {
			holder.type.text = String.format(context.getString(R.string.last_edit),
					dateFormatter.format(date), timeFormatter.format(date)
			)
		}
		if (holder.checkbox.visibility == View.VISIBLE) {
			if (i == 0 && item.filename.startsWith(context.getString(R.string.label_parent_dir))) {
				holder.checkbox.visibility = View.INVISIBLE
			}
			holder.checkbox.setChecked(MarkedItemList.hasItem(item.location))
		}
		holder.checkbox.setOnCheckedChangedListener(object : OnCheckedChangeListener {
			override fun onCheckedChanged(checkbox: MaterialCheckbox?, isChecked: Boolean) {
				if (item.size > ATTACH_MAX_FILESIZE) {
					toast(String.format(context.getString(R.string.attach_max_size), ATTACH_MAX_FILESIZE.humanReadableBytes()))
					MarkedItemList.removeSelectedItem(item.location)
					notifyItemChecked!!.notifyCheckBoxIsClicked()
					return
				} else {
				item.isMarked = isChecked
				if (item.isMarked) {
					if (properties.selection_mode == DialogConfigs.MULTI_MODE) {
						MarkedItemList.addSelectedItem(item)
					} else {
						MarkedItemList.addSingleFile(item)
					}
				} else {
					MarkedItemList.removeSelectedItem(item.location)
				}
				notifyItemChecked!!.notifyCheckBoxIsClicked()
			}
			}

		})
		return view
	}

	private inner class ViewHolder internal constructor(itemView: View) {
		var type_icon: ImageView
		var name: TextView
		var type: TextView
		var checkbox: MaterialCheckbox
		var size: TextView

		init {
			name = itemView.findViewById(R.id.fname)
			type = itemView.findViewById(R.id.ftype)
			size = itemView.findViewById(R.id.fsize)
			type_icon = itemView.findViewById(R.id.image_type)
			checkbox = itemView.findViewById(R.id.file_mark)
		}
	}

	fun setNotifyItemCheckedListener(notifyItemChecked: NotifyItemChecked) {
		this.notifyItemChecked = notifyItemChecked
	}

}