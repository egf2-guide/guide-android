package com.eigengraph.egf2.guide.ui.anko

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.ui.adapter.PostsAdapter
import com.eigengraph.egf2.guide.ui.fragment.TimeLineFragment
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.wrapContent

class TimeLineLayout(val postsAdapter: PostsAdapter) : IFragmentLayout {
	override fun bind(fragment: Fragment): View =
			fragment.UI {
				(fragment as TimeLineFragment).swipe = swipeRefreshLayout {
					id = R.id.swipe
					recyclerView {
						id = R.id.list
						lparams(matchParent, wrapContent)
						layoutManager = LinearLayoutManager(ctx)
						adapter = postsAdapter
						addOnScrollListener((fragment as TimeLineFragment).scrollListener)
					}
				}
			}.view

	override fun unbind(fragment: Fragment) {

	}
}