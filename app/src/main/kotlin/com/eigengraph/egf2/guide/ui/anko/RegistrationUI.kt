package com.eigengraph.egf2.guide.ui.anko

import android.text.InputType
import android.view.View
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.ui.fragment.LoginFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedEditText
import org.jetbrains.anko.design.textInputLayout

class RegistrationUI {
	fun bind(parent: LoginFragment): View =
			parent.context.UI {
				scrollView {
					verticalLayout {
						padding = dip(16)

						textInputLayout {
							parent.fname = tintedEditText {
								hint = resources.getString(R.string.hint_first_name)
								singleLine = true
							}
						}.lparams(width = matchParent, height = wrapContent)

						textInputLayout {
							parent.lname = tintedEditText {
								hint = resources.getString(R.string.hint_last_name)
								singleLine = true
							}
						}.lparams(width = matchParent, height = wrapContent)

						textInputLayout {
							parent.email = tintedEditText {
								hint = resources.getString(R.string.hint_email)
								singleLine = true
							}
						}.lparams(width = matchParent, height = wrapContent)

						textInputLayout {
							parent.dob = tintedEditText {
								hint = resources.getString(R.string.hint_dob)
								singleLine = true
							}
						}.lparams(width = matchParent, height = wrapContent)

						textInputLayout {
							parent.pass = tintedEditText {
								hint = resources.getString(R.string.hint_pass)
								singleLine = true
								inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
							}
						}.lparams(width = matchParent, height = wrapContent)

						textInputLayout {
							parent.pass2 = tintedEditText {
								hint = resources.getString(R.string.hint_pass2)
								singleLine = true
								inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
							}
						}.lparams(width = matchParent, height = wrapContent)
					}
				}
			}.view
}