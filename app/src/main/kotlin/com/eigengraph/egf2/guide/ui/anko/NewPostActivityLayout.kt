package com.eigengraph.egf2.guide.ui.anko

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.ui.NewPostActivity
import com.eigengraph.egf2.guide.util.actionBarSize
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedEditText
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.textInputLayout

class NewPostActivityLayout : IActivityLayout {
	override fun bind(activity: AppCompatActivity) = activity.UI {
		coordinatorLayout {
			fitsSystemWindows = true

			appBarLayout {
				toolbar(R.style.AppTheme_AppBarOverlay) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) elevation = 4f
					activity.setSupportActionBar(this)
					activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
					activity.supportActionBar?.title = "Add New Post"
				}.lparams(width = matchParent, height = actionBarSize())
			}.lparams(width = matchParent)

			scrollView {
				(activity as NewPostActivity).container = verticalLayout {
					id = R.id.container

					(activity as NewPostActivity).image = imageView {
						imageResource = R.drawable.camera_enhance
						scaleType = ImageView.ScaleType.FIT_CENTER
					}.lparams(width = matchParent, height = dip(250))

					textInputLayout {
						(activity as NewPostActivity).text = tintedEditText {
							hint = "Add text"
						}
					}.lparams(width = matchParent, height = wrapContent) {
						margin = dip(16)
					}
				}.lparams(width = matchParent, height = matchParent)
			}.lparams(width = matchParent, height = matchParent) {
				topMargin = actionBarSize()
			}

			(activity as NewPostActivity).fab = floatingActionButton {
				imageResource = R.drawable.check
			}.lparams(dip(56), dip(56)) {
				gravity = Gravity.BOTTOM or Gravity.RIGHT
				bottomMargin = dip(16)
				rightMargin = dip(16)
			}

			(activity as NewPostActivity).progress = progressBar {
				visibility = View.GONE
			}.lparams {
				gravity = Gravity.CENTER
			}
		}
	}.view

	override fun unbind(activity: AppCompatActivity) {
		(activity as NewPostActivity).container = null
		(activity as NewPostActivity).fab = null
		(activity as NewPostActivity).image = null
		(activity as NewPostActivity).text = null
	}
}