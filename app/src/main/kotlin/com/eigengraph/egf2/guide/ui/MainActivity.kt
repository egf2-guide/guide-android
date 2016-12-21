package com.eigengraph.egf2.guide.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.FrameLayout
import com.dd.realmbrowser.RealmBrowser
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.framework.EGF2Bus
import com.eigengraph.egf2.framework.models.EGF2Model
import com.eigengraph.egf2.guide.DataManager
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.showFragment
import com.eigengraph.egf2.guide.ui.anko.MainActivityLayout
import com.eigengraph.egf2.guide.ui.fragment.AccountFragment
import com.eigengraph.egf2.guide.ui.fragment.PostsFragment
import com.eigengraph.egf2.guide.ui.fragment.TimeLineFragment
import com.eigengraph.egf2.guide.util.snackbar
import com.eigengraph.egf2.guide.util.snackbarLong
import io.realm.RealmConfiguration
import org.jetbrains.anko.contentView

class MainActivity : AppCompatActivity() {

	var container: FrameLayout? = null
	var fab: FloatingActionButton? = null

	private val mainLayout = MainActivityLayout()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(mainLayout.bind(this))

		if (DataManager.user == null) {
			EGF2.getSelfUser(null, true, EGF2User::class.java).subscribe({
				user ->
				DataManager.user = user
				EGF2Bus.post(EGF2Bus.EVENT.OBJECT_LOADED, EGF2Model.ME, user)
				if (!user.verified) {
					verify()
				}
			}, {
				contentView?.snackbar(it.message.toString())
			})
		}

		if (savedInstanceState == null) {
			fab?.hide()
			supportFragmentManager.showFragment(TimeLineFragment.newInstance())
		}

		val conf: RealmConfiguration = RealmConfiguration.Builder()
				.schemaVersion(1)
				.build()

		RealmBrowser.getInstance().addRealmConf(conf)

		RealmBrowser.showRealmFilesNotification(this)
	}

	private fun verify() {
		contentView?.snackbarLong("User not verified")
	}

	override fun onDestroy() {
		mainLayout.unbind(this)
		super.onDestroy()
	}

	var currItem = R.id.action_timeline

	fun navigationListener(item: MenuItem): Boolean {
		if (currItem != item.itemId) {
			currItem = item.itemId
			when (item.itemId) {
				R.id.action_timeline -> {
					fab?.hide()
					supportFragmentManager.showFragment(TimeLineFragment.newInstance())
				}
				R.id.action_posts -> {
					fab?.show()
					supportFragmentManager.showFragment(PostsFragment.newInstance())
				}
				R.id.action_account -> {
					fab?.hide()
					supportFragmentManager.showFragment(AccountFragment.newInstance())
				}
			}
		}
		return true
	}

}
