package com.eigengraph.egf2.guide.ui.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.guide.DataManager
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.util.fullName

class FollowItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
	val name = itemView.findViewById(R.id.follow_item_name) as TextView
	val email = itemView.findViewById(R.id.follow_item_email) as TextView
	val unfollow = itemView.findViewById(R.id.follow_item_unfollow) as Button

	fun bind(user: EGF2User, unfoll: Boolean) {
		name.text = user.name.fullName()
		email.text = user.email

		if (unfoll) {
			unfollow.text = "UNFOLLOW"
			this.unfollow.setOnClickListener {
				EGF2.deleteObjectFromEdge(DataManager.user?.id as String, EGF2User.EDGE_FOLLOWS, user).subscribe({}, {})
			}
		} else {
			unfollow.text = "FOLLOW"
			this.unfollow.setOnClickListener {
				EGF2.createEdge(DataManager.user?.id as String, EGF2User.EDGE_FOLLOWS, user).subscribe({}, {})
			}
		}
	}
}