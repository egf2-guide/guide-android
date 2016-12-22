package com.eigengraph.egf2.guide.ui.anko

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.eigengraph.egf2.guide.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedTextView

class FollowItemUI {
	fun bind(parent: ViewGroup): View =
			parent.context.UI {
				linearLayout {
					lparams(width = matchParent, height = wrapContent)
					orientation = LinearLayout.HORIZONTAL
					gravity = Gravity.CENTER
					padding = dip(8)
					verticalLayout {
						tintedTextView {
							id = R.id.follow_item_name
							textSize = 16f
							setTypeface(Typeface.SERIF, Typeface.BOLD)
						}.lparams(width = matchParent, height = wrapContent)
						tintedTextView {
							id = R.id.follow_item_email
							textSize = 12f
						}.lparams(width = matchParent, height = wrapContent)
					}.lparams(width = matchParent, height = wrapContent) {
						weight = 1f
					}
					button {
						id = R.id.follow_item_unfollow
						text = "UNFOLLOW"
					}.lparams(height = dip(36)) { leftMargin = dip(16) }
				}
			}.view
}