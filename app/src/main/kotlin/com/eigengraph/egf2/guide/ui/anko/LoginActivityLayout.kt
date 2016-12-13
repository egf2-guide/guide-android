package com.eigengraph.egf2.guide.ui.anko

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.ui.LoginActivity
import org.jetbrains.anko.UI
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent

class LoginActivityLayout : IActivityLayout {
	override fun bind(activity: AppCompatActivity): View =
			activity.UI {
				(activity as LoginActivity).container = frameLayout {
					id = R.id.container
					lparams(width = matchParent, height = matchParent)
				}
			}.view

	override fun unbind(activity: AppCompatActivity) {
		(activity as LoginActivity).container = null
	}
}