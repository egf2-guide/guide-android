package com.eigengraph.egf2.guide.ui.anko

import android.support.v4.app.Fragment
import android.view.View

interface IFragmentLayout {
	fun bind(fragment: Fragment): View
	fun unbind(fragment: Fragment)
}