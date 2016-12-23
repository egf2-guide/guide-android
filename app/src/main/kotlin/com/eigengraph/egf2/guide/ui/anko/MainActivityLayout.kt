package com.eigengraph.egf2.guide.ui.anko

import android.os.Build
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.ui.MainActivity
import com.eigengraph.egf2.guide.util.actionBarSize
import com.eigengraph.egf2.guide.util.bottomNavigationView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton

class MainActivityLayout : IActivityLayout {
	override fun unbind(activity: AppCompatActivity) {
		(activity as MainActivity).container = null
		(activity as MainActivity).fab = null
	}

	override fun bind(activity: AppCompatActivity) = activity.UI {
		coordinatorLayout {
			fitsSystemWindows = true

			appBarLayout {
				(activity as MainActivity).appBar = toolbar(R.style.AppTheme_AppBarOverlay) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) elevation = 4f
					activity.setSupportActionBar(this)
					activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
				}.lparams(width = matchParent, height = actionBarSize())

			}.lparams(width = matchParent) {
			}

			verticalLayout {
				(activity as MainActivity).coordinatorLayout = coordinatorLayout {
					(activity as MainActivity).container = frameLayout {
						id = R.id.container
					}.lparams(width = matchParent, height = matchParent) {
						topMargin = actionBarSize()
						//bottomMargin = actionBarSize()
						behavior = AppBarLayout.ScrollingViewBehavior()
					}

					(activity as MainActivity).fab = floatingActionButton {
						imageResource = R.drawable.plus
					}.lparams(dip(56), dip(56)) {
						gravity = Gravity.BOTTOM or Gravity.RIGHT
						bottomMargin = dip(16)
						rightMargin = dip(16)
					}
				}.lparams(height = dip(0)) {
					weight = 1f
				}
				bottomNavigationView {
					itemBackgroundResource = R.color.colorPrimary
					itemTextColor = resources.getColorStateList(R.color.main_bottom_item)
					itemIconTintList = resources.getColorStateList(R.color.main_bottom_item)
					inflateMenu(R.menu.main_bottom)

					setOnNavigationItemSelectedListener { (activity as MainActivity).navigationListener(it) }
				}.lparams(width = matchParent, height = actionBarSize()) {
					gravity = Gravity.BOTTOM
				}
			}
		}
		((activity as MainActivity).appBar?.layoutParams as AppBarLayout.LayoutParams).scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
	}.view
}