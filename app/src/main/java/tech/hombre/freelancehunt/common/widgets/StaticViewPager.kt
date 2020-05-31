package tech.hombre.freelancehunt.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import androidx.viewpager.widget.PagerAdapter

import androidx.viewpager.widget.ViewPager


class StaticViewPager : ViewPager {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val childrenCount = childCount
        offscreenPageLimit = childrenCount - 1

        adapter = object : PagerAdapter() {
            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                return container.getChildAt(position)
            }

            override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
                return arg0 === arg1
            }

            override fun getCount(): Int {
                return childrenCount
            }

            override fun destroyItem(container: View, position: Int, `object`: Any) {}
        }
    }
}