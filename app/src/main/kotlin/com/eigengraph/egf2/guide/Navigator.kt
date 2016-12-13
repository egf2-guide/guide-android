package com.eigengraph.egf2.guide

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

inline fun FragmentManager.showFragment(fragment: Fragment) {
	this.beginTransaction()
			.replace(R.id.container, fragment, fragment.javaClass.simpleName)
			.commit()
}
