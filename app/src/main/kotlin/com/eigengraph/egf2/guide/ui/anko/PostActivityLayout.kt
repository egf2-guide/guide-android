package com.eigengraph.egf2.guide.ui.anko

import android.graphics.Color
import android.os.Build
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.ui.PostActivity
import com.eigengraph.egf2.guide.util.actionBarSize
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedEditText
import org.jetbrains.anko.appcompat.v7.tintedTextView
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.collapsingToolbarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class PostActivityLayout : IActivityLayout {
	override fun bind(activity: AppCompatActivity): View = activity.UI {
		var collapsingToolbar: CollapsingToolbarLayout? = null
		var toolbar: Toolbar? = null
		val p8 = dip(8)
		val p48 = dip(48)
		coordinatorLayout {
			fitsSystemWindows = true

			appBarLayout {
				fitsSystemWindows = true

				collapsingToolbar = collapsingToolbarLayout {
					fitsSystemWindows = true
					expandedTitleMarginStart = p48
					expandedTitleMarginEnd = p48
					setContentScrimResource(R.color.colorPrimary)

					(activity as PostActivity).imageView = imageView {
						fitsSystemWindows = true

						id = R.id.post_item_image
						scaleType = ImageView.ScaleType.CENTER_CROP
						layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
						layoutParams = CollapsingToolbarLayout.LayoutParams(layoutParams as FrameLayout.LayoutParams)
						(layoutParams as CollapsingToolbarLayout.LayoutParams).collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
					}
					toolbar = toolbar(R.style.AppTheme_AppBarOverlay) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) elevation = 4f
						activity.setSupportActionBar(this)
						activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
						activity.supportActionBar?.title = ""
						layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionBarSize())
						layoutParams = CollapsingToolbarLayout.LayoutParams(layoutParams as FrameLayout.LayoutParams)
						(layoutParams as CollapsingToolbarLayout.LayoutParams).collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
					}
				}.lparams(matchParent, matchParent)
			}.lparams(matchParent, dip(250))


			verticalLayout {
				verticalLayout {
					backgroundColor = Color.WHITE
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) elevation = 4f
					(activity as PostActivity).author = tintedTextView {
						id = R.id.post_item_creator
						topPadding = p8
						leftPadding = p8
						rightPadding = p8
						textSize = 12f
					}.lparams(width = matchParent, height = wrapContent)
					linearLayout {
						orientation = LinearLayout.HORIZONTAL
						(activity as PostActivity).text = tintedEditText {
							id = R.id.post_item_text
							leftPadding = p8
							rightPadding = p8
							bottomPadding = p8
							textSize = 16f
							inputType = InputType.TYPE_NULL
							//inputType = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE or InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE
							imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
							singleLine = false
							background = null
						}.lparams(width = matchParent, height = wrapContent) { weight = 1f }
						(activity as PostActivity).btnEdit = imageButton {
							imageResource = R.drawable.border_color
							visibility = View.GONE
						}
					}.lparams(width = matchParent, height = wrapContent)
				}

				(activity as PostActivity).swipe = swipeRefreshLayout {
					(activity as PostActivity).list = recyclerView {
						clipToPadding = true
						layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
						bottomPadding = p48
						addOnScrollListener((activity as PostActivity).scrollListener)
					}.lparams(matchParent, wrapContent) {
						//weight = 1f
					}
				}

			}.lparams(width = matchParent, height = matchParent) {
				behavior = AppBarLayout.ScrollingViewBehavior()
			}

			linearLayout {
				orientation = LinearLayout.HORIZONTAL
				backgroundColor = Color.WHITE
				gravity = Gravity.CENTER
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) elevation = 4f
				(activity as PostActivity).message = tintedEditText {
					hint = "Message"
					leftPadding = p8
					rightPadding = p8
				}.lparams(matchParent, wrapContent) {
					weight = 1f
				}
				(activity as PostActivity).send = imageButton {
					imageResource = R.drawable.send
					onClick { (activity as PostActivity).sendMessage() }
				}.lparams(p48, p48)
			}.lparams(width = matchParent, height = wrapContent) {
				gravity = Gravity.BOTTOM
			}
		}

		//(collapsingToolbar?.layoutParams as AppBarLayout.LayoutParams).scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
		(collapsingToolbar?.layoutParams as AppBarLayout.LayoutParams).scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP

	}.view

	override fun unbind(activity: AppCompatActivity) {

	}
}