package com.eigengraph.egf2.guide.ui.anko

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.ui.adapter.PostsAdapter
import com.eigengraph.egf2.guide.ui.fragment.PostsFragment
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class PostsLayout(val postsAdapter: PostsAdapter) : IFragmentLayout {
	override fun bind(fragment: Fragment): View =
			fragment.UI {
				(fragment as PostsFragment).swipe = swipeRefreshLayout {
					id = R.id.swipe
					(fragment as PostsFragment).recyclerView = recyclerView {
						id = R.id.list
						lparams(matchParent, matchParent)
						layoutManager = LinearLayoutManager(ctx)
						adapter = postsAdapter
					}
				}
			}.view

	override fun unbind(fragment: Fragment) {

	}
}