package com.eigengraph.egf2.guide.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.guide.showFragment
import com.eigengraph.egf2.guide.ui.anko.LoginActivityLayout
import com.eigengraph.egf2.guide.ui.fragment.LoginFragment
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

	var container: FrameLayout? = null

	private val loginLayout = LoginActivityLayout()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (EGF2.isLoggedIn()) {
			finish()
			this.startActivity<MainActivity>()
		} else {
			setContentView(loginLayout.bind(this))
			supportFragmentManager.showFragment(LoginFragment.newInstance())
		}
	}

	override fun onDestroy() {
		loginLayout.unbind(this)
		super.onDestroy()
	}
}