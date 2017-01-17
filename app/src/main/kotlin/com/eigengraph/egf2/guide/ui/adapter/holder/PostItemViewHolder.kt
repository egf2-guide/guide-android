package com.eigengraph.egf2.guide.ui.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.guide.DataManager
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.models.EGF2AdminRole
import com.eigengraph.egf2.guide.models.EGF2File
import com.eigengraph.egf2.guide.models.EGF2Post
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.util.RecyclerClickListener

class PostItemViewHolder(itemView: View, listener: RecyclerClickListener, val isAdmin: Boolean) : RecyclerView.ViewHolder(itemView) {
	val imageView = itemView.findViewById(R.id.post_item_image) as ImageView
	val text = itemView.findViewById(R.id.post_item_text) as TextView
	val author = itemView.findViewById(R.id.post_item_creator) as TextView
	val confirm = itemView.findViewById(R.id.post_item_confirm) as Button
	val cancel = itemView.findViewById(R.id.post_item_cancel) as Button
	val adminLayout = itemView.findViewById(R.id.post_item_admin)

	init {
		itemView.setOnClickListener { listener.onElementClick(adapterPosition) }
	}

	fun bind(post: EGF2Post, image: EGF2File?, creator: EGF2User?) {
		text.text = post.desc

		author.text = ""

		creator?.let { author.text = creator.email }

		Glide.with(itemView.context)
				.load(image?.url)
				.fitCenter()
				.into(imageView)

		if (isAdmin) {
			adminLayout.visibility = View.VISIBLE
			confirm.setOnClickListener {
				EGF2.deleteObjectFromEdge(post.creator, EGF2User.EDGE_POSTS, post)
						.subscribe({}, {})
			}
			cancel.setOnClickListener {
				EGF2.deleteObjectFromEdge(DataManager.admin?.id as String, EGF2AdminRole.EDGE_OFFENDING_POSTS, post)
						.subscribe({}, {})
			}
		} else {
			adminLayout.visibility = View.GONE
		}
	}
}