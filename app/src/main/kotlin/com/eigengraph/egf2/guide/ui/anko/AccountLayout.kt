package com.eigengraph.egf2.guide.ui.anko

import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.ui.fragment.AccountFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedTextView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class AccountLayout : IFragmentLayout {
	override fun bind(fragment: Fragment): View =
			fragment.UI {
				verticalLayout {
					(fragment as AccountFragment).avatarBackground = verticalLayout {
						bottomPadding = dip(16)
						backgroundColor = resources.getColor(R.color.colorPrimary)
						gravity = Gravity.CENTER
						(fragment as AccountFragment).avatar = imageView {
							imageResource = R.drawable.account
						}.lparams(width = dip(72), height = dip(72)) {
							gravity = Gravity.CENTER
							margin = dip(16)
						}
						(fragment as AccountFragment).name = tintedTextView {
							text = ""
							gravity = Gravity.CENTER
							textColor = Color.WHITE
							textSize = 18f
							setShadowLayer(5.0f, 2.0f, 2.0f, Color.BLACK)
						}.lparams(matchParent, wrapContent)
						(fragment as AccountFragment).email = tintedTextView {
							text = ""
							gravity = Gravity.CENTER
							textColor = Color.WHITE
							textSize = 14f
							setShadowLayer(5.0f, 2.0f, 2.0f, Color.BLACK)
						}.lparams(matchParent, wrapContent)
						(fragment as AccountFragment).verify = button {
							text = "Resend verify email"
							visibility = View.GONE
						}.lparams(wrapContent, dip(36))
					}.lparams(matchParent, wrapContent)

					(fragment as AccountFragment).swipe = swipeRefreshLayout {
						(fragment as AccountFragment).recyclerView = recyclerView {
							layoutManager = LinearLayoutManager(ctx)
							itemAnimator = DefaultItemAnimator()
						}.lparams(matchParent, wrapContent)
					}
				}
			}.view

	override fun unbind(fragment: Fragment) {
		(fragment as AccountFragment).avatarBackground = null
		(fragment as AccountFragment).avatar = null
		(fragment as AccountFragment).name = null
		(fragment as AccountFragment).email = null
	}
}