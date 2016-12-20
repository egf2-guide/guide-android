package com.eigengraph.egf2.guide.ui.anko

import android.support.v4.app.Fragment
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.ui.fragment.LoginFragment
import com.jakewharton.rxbinding.widget.RxTextView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedButton
import org.jetbrains.anko.appcompat.v7.tintedEditText
import org.jetbrains.anko.appcompat.v7.tintedTextView
import org.jetbrains.anko.design.textInputLayout
import org.jetbrains.anko.support.v4.UI
import rx.Observable

class LoginFragmentLayout : IFragmentLayout {
	override fun bind(fragment: Fragment): View =
			fragment.UI {
				verticalLayout {
					padding = dip(16)
					layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
					tintedTextView {
						textSize = 24f
						text = resources.getString(R.string.welcome)
						gravity = Gravity.CENTER
					}
					tintedTextView {
						textSize = 16f
						text = resources.getString(R.string.welcome2)
						gravity = Gravity.CENTER
						bottomPadding = dip(24)
					}
					var email: EditText? = null
					textInputLayout {
						email = tintedEditText {
							hint = resources.getString(R.string.hint_email)
							singleLine = true
						}
					}.lparams(width = matchParent, height = wrapContent)
					var pass: EditText? = null
					textInputLayout {
						pass = tintedEditText {
							hint = resources.getString(R.string.hint_pass)
							singleLine = true
							inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
						}
					}.lparams(width = matchParent, height = wrapContent)
					val btn = tintedButton {
						text = resources.getString(R.string.btn_login)
						onClick {
							(fragment as LoginFragment).login(email, pass)
						}
					}
					tintedTextView {
						topPadding = dip(24)
						text = resources.getString(R.string.login_register)
						gravity = Gravity.CENTER
						onClick {
							(fragment as LoginFragment).register()
						}
					}
					tintedTextView {
						topPadding = dip(4)
						text = resources.getString(R.string.login_forgot)
						gravity = Gravity.CENTER
						onClick {
							(fragment as LoginFragment).forgot()
						}
					}
					Observable.combineLatest(
							RxTextView.textChanges(email!!),
							RxTextView.textChanges(pass!!),
							{ login, pass ->
								login.isNotEmpty() && pass.isNotEmpty()
							})
							.subscribe { btn.isEnabled = it }
				}
			}.view

	override fun unbind(fragment: Fragment) {

	}

}