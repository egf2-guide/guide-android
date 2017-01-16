package com.eigengraph.egf2.guide.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.ui.adapter.holder.FollowItemViewHolder
import com.eigengraph.egf2.guide.ui.anko.FollowItemUI

class FollowsAdapter : RecyclerView.Adapter<FollowItemViewHolder> {

	val list: List<EGF2User>
	private var unfollow = true

	constructor(li: List<EGF2User>) {
		list = li
	}

	override fun getItemCount() = list.size

	override fun onBindViewHolder(holder: FollowItemViewHolder?, position: Int) {
		val user = list[position]
		holder?.bind(user, unfollow)
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FollowItemViewHolder {
		return FollowItemViewHolder(FollowItemUI().bind(parent!!))
	}

	fun unfollow(b: Boolean) {
		unfollow = b
	}
}