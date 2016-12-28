package com.eigengraph.egf2.guide.ui.anko

import android.view.View
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.ui.fragment.LoginFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedEditText
import org.jetbrains.anko.design.textInputLayout

class ForgotUI {
	fun bind(parent: LoginFragment): View =
			parent.context.UI {
				scrollView {
					verticalLayout {
						padding = dip(16)

						textInputLayout {
							parent.email = tintedEditText {
								hint = resources.getString(R.string.hint_email)
								singleLine = true
							}
						}.lparams(width = matchParent, height = wrapContent)
					}
				}
			}.view
}