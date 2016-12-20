package com.eigengraph.egf2.guide.ui.fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.framework.EGF2Bus
import com.eigengraph.egf2.guide.DataManager
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.ui.LoginActivity
import com.eigengraph.egf2.guide.ui.anko.AccountLayout
import com.eigengraph.egf2.guide.util.fullName
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.startActivity
import rx.Subscription
import rx.functions.Action1


class AccountFragment : Fragment() {
	companion object {
		fun newInstance() = AccountFragment()
	}

	var avatar: ImageView? = null
	var avatarBackground: LinearLayout? = null
	var name: TextView? = null
	var email: TextView? = null

	private var layout = AccountLayout()

	private var sub: Subscription? = null

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return layout.bind(this)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		setHasOptionsMenu(true)

		DataManager.user?.let {
			name?.text = it.name.fullName()
			email?.text = it.email
			sub = EGF2Bus.subscribeForObject(EGF2Bus.EVENT.OBJECT_UPDATED, it.id, Action1 {
				it.obj?.let {
					name?.text = (it as EGF2User).name.fullName()
					email?.text = (it as EGF2User).email
				}
			})
		}

		Glide.with(this)
				.load("https://xakep.ru/wp-content/uploads/2016/07/Android-Honeycomb-h.jpg")
				.asBitmap()
				.fitCenter()
				.into(object : SimpleTarget<Bitmap>() {
					override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
						val drawable = BitmapDrawable(resource)
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							avatarBackground?.background = drawable
						} else {
							avatarBackground?.setBackgroundDrawable(drawable)
						}
					}
				})

		Glide.with(this)
				.load("https://xakep.ru/wp-content/uploads/2015/10/android-hero.png")
				.asBitmap()
				.centerCrop()
				.into(object : BitmapImageViewTarget(avatar) {
					override fun setResource(resource: Bitmap) {
						val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource)
						circularBitmapDrawable.isCircular = true
						avatar?.setImageDrawable(circularBitmapDrawable)
					}
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
			}, {
				val pref = activity.defaultSharedPreferences
				pref.edit().remove("token").apply()
				activity.startActivity<LoginActivity>()
				activity.finishAffinity()
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

