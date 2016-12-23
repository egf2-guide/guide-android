package com.eigengraph.egf2.guide.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.framework.EGF2Bus
import com.eigengraph.egf2.guide.DataManager
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.ui.LoginActivity
import com.eigengraph.egf2.guide.ui.adapter.FollowsAdapter
import com.eigengraph.egf2.guide.ui.anko.AccountLayout
import com.eigengraph.egf2.guide.util.fullName
import com.eigengraph.egf2.guide.util.snackbar
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.startActivity
import rx.Subscription
import rx.functions.Action1
import java.util.*


class AccountFragment : Fragment() {
	companion object {
		fun newInstance() = AccountFragment()
	}

	var avatar: ImageView? = null
	var avatarBackground: LinearLayout? = null
	var name: TextView? = null
	var email: TextView? = null
	var recyclerView: RecyclerView? = null
	var swipe: SwipeRefreshLayout? = null
    var verify: Button? = null

	private var layout = AccountLayout()

	private var sub: Subscription? = null

	private var list = ArrayList<EGF2User>()

	private var adapter = FollowsAdapter(list)

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return layout.bind(this)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		setHasOptionsMenu(true)

		recyclerView?.adapter = adapter

		DataManager.user?.let {
			name?.text = it.name.fullName()
			email?.text = it.email
			sub = EGF2Bus.subscribeForObject(EGF2Bus.EVENT.OBJECT_UPDATED, it.id, Action1 {
				it.obj?.let {
					name?.text = (it as EGF2User).name.fullName()
					email?.text = (it as EGF2User).email
				}
			})

            if (!it.verified) {
                verify?.visibility = View.VISIBLE
                verify?.setOnClickListener {
                    EGF2.resendEmailVerification()
                            .subscribe({
                                view?.snackbar("Please check the email, We have sent you an Verification Email")
                            }, {
                                view?.snackbar(it.message.toString())
                            })
                }
            }

			getFollows(true)

			swipe?.setOnRefreshListener {
				getFollows(false)
			}

			EGF2Bus.subscribeForEdge(EGF2Bus.EVENT.EDGE_REMOVED, DataManager.user?.id as String, EGF2User.EDGE_FOLLOWS, Action1 {
				event ->

				val iter = list.iterator()
				while (iter.hasNext()) {
					val user = iter.next()
					if (user.id == event.obj?.getId())
						iter.remove()
				}
				adapter.notifyDataSetChanged()
			})
		}

	}

	private fun getFollows(useCache: Boolean) {
		swipe?.isRefreshing = true
		EGF2.getEdgeObjects(DataManager.user?.id as String, EGF2User.EDGE_FOLLOWS, null, EGF2.DEF_COUNT, null, useCache, EGF2User::class.java)
				.subscribe({
					if (EGF2.isFirstPage(it)) list.clear()
					list.addAll(it.results as ArrayList<EGF2User>)
					adapter.notifyDataSetChanged()
					if (swipe?.isRefreshing as Boolean) swipe?.isRefreshing = false
				}, {
					if (swipe?.isRefreshing as Boolean) swipe?.isRefreshing = false
				})
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.account, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.itemId == R.id.action_logout) {
			EGF2.logout().subscribe({
				val pref = activity.defaultSharedPreferences
				pref.edit().remove("token").apply()
				activity.startActivity<LoginActivity>()
				activity.finishAffinity()
				DataManager.user = null
			}, {
				val pref = activity.defaultSharedPreferences
				pref.edit().remove("token").apply()
				activity.startActivity<LoginActivity>()
				activity.finishAffinity()
				DataManager.user = null
			})
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onDestroyView() {
		layout.unbind(this)
		sub?.unsubscribe()
		super.onDestroyView()
	}
}

