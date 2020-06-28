package tech.hombre.freelancehunt.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ReverseFabScrollingViewBehavior(
    context: Context?,
    attrs: AttributeSet?
) :
    FloatingActionButton.Behavior() {
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed
        )

        if (dyConsumed > 0) {
            child.animate().translationY(0f).setInterpolator(LinearInterpolator()).start()
        } else if (dyConsumed < 0) {
            val layoutParams =
                child.layoutParams as CoordinatorLayout.LayoutParams
            val bottomMargin = layoutParams.bottomMargin
            child.animate().translationY(child.height + bottomMargin.toFloat())
                .setInterpolator(LinearInterpolator()).start()

        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
}