package com.eigengraph.egf2.guide.ui.anko

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.eigengraph.egf2.guide.R
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.wrapContent

class PostsLayout : IFragmentLayout {
	override fun bind(fragment: Fragment): View =
			fragment.UI {
				recyclerView {
					id = R.id.list
					lparams(matchParent, wrapContent)
					layoutManager = LinearLayoutManager(ctx)
				}
			}.view

	override fun unbind(fragment: Fragment) {

	}
}