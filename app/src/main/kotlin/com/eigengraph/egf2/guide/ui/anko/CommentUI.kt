package com.eigengraph.egf2.guide.ui.anko

import android.view.View
import com.eigengraph.egf2.guide.ui.PostActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedEditText
import org.jetbrains.anko.design.textInputLayout

class CommentUI {
	fun bind(parent: PostActivity): View =
			parent.UI {
				scrollView {
					verticalLayout {
						padding = dip(16)
						textInputLayout {
							parent.comment = tintedEditText {
								hint = "Message"
								singleLine = true
							}
						}.lparams(width = matchParent, height = wrapContent)
					}
				}
			}.view
}