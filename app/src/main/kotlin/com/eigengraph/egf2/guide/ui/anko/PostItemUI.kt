package com.eigengraph.egf2.guide.ui.anko

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.eigengraph.egf2.guide.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedTextView

class PostItemUI {
	fun bind(parent: ViewGroup): View =
			parent.context.UI {
				verticalLayout {
					val p = dip(8)
					lparams(width = matchParent, height = wrapContent)
					imageView {
						id = R.id.post_item_image
						scaleType = ImageView.ScaleType.FIT_CENTER
					}.lparams(width = matchParent, height = wrapContent)
					tintedTextView {
						id = R.id.post_item_creator
						singleLine = true
						ellipsize = TextUtils.TruncateAt.END
						topPadding = p
						leftPadding = p
						rightPadding = p
					}.lparams(width = matchParent, height = wrapContent)
					tintedTextView {
						id = R.id.post_item_text
						singleLine = true
						ellipsize = TextUtils.TruncateAt.END
						leftPadding = p
						rightPadding = p
					}.lparams(width = matchParent, height = wrapContent) {
						bottomMargin = dip(24)
					}
				}
			}.view
}