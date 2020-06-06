package tech.hombre.freelancehunt.ui.main.view.fragments

import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.common.extensions.switch
import tech.hombre.freelancehunt.ui.base.BaseFragment
import tech.hombre.freelancehunt.ui.main.presentation.MainPublicViewModel


class MainFragment : BaseFragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val sharedViewModelMain: MainPublicViewModel by sharedViewModel()

    override fun getLayout() = R.layout.fragment_main

    override fun viewReady() {
        subscribeToData()
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.selectedItemId = R.id.menu_feed
    }

    private fun subscribeToData() {
        sharedViewModelMain.feedBadgeCounter.subscribe(this) {
            val menuItemId = bottomNavigationView.menu.getItem(0).itemId
            val badgeDrawable = bottomNavigationView.getOrCreateBadge(menuItemId)
            if (it > 0) {
                badgeDrawable.number = it
                badgeDrawable.isVisible = true
            } else {
                badgeDrawable.isVisible = false
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_feed -> {
                childFragmentManager.switch(
                    R.id.fragmentContainer,
                    FeedFragment.newInstance(),
                    FeedFragment.TAG
                )
                true
            }
            R.id.menu_projects -> {
                childFragmentManager.switch(
                    R.id.fragmentContainer,
                    ProjectsFragment.newInstance(),
                    ProjectsFragment.TAG
                )
                true
            }
            R.id.menu_contests -> {
                childFragmentManager.switch(
                    R.id.fragmentContainer,
                    ContestsFragment.newInstance(),
                    ContestsFragment.TAG
                )
                true
            }
            else -> false
        }
    }

    companion object {
        val TAG = MainFragment::class.java.simpleName

        fun newInstance() = MainFragment()
    }
}

