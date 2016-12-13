package com.eigengraph.egf2.guide.util

import android.support.design.widget.BottomNavigationView
import android.util.TypedValue
import android.view.View
import android.view.ViewManager
import com.eigengraph.egf2.guide.R
import org.jetbrains.anko.custom.ankoView

fun View.actionBarSize(): Int {
	val tv = TypedValue()
	if (context.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
		return TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
	}
	return 0
}

inline fun ViewManager.bottomNavigationView(theme: Int = 0) = bottomNavigationView(theme) {}
inline fun ViewManager.bottomNavigationView(theme: Int = 0, init: BottomNavigationView.() -> Unit) = ankoView(::BottomNavigationView, theme, init)