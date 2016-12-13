package com.eigengraph.egf2.guide.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.framework.LoginModel
import com.eigengraph.egf2.guide.ui.MainActivity
import com.eigengraph.egf2.guide.ui.anko.LoginFragmentLayout
import com.eigengraph.egf2.guide.util.snackbar
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.startActivity

class LoginFragment : Fragment() {
	companion object {
		fun newInstance() = LoginFragment()
	}

	private var layout = LoginFragmentLayout()

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return layout.bind(this)
	}

	internal fun login(email: EditText?, pass: EditText?) {
		EGF2.login(LoginModel(email?.text.toString(), pass?.text.toString()))
				.subscribe({
					val pref = activity.defaultSharedPreferences
					pref.edit().putString("token", it).apply()
					activity.startActivity<MainActivity>()
				}, {
					view?.snackbar(it.message.toString())
				})
	}

	override fun onDestroyView() {
		layout.unbind(this)
		super.onDestroyView()
	}
}