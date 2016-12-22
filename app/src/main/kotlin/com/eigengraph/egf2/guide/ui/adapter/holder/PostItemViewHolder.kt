package com.eigengraph.egf2.guide.ui.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.models.EGF2File
import com.eigengraph.egf2.guide.models.EGF2Post
import com.eigengraph.egf2.guide.models.EGF2User

class PostItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
	val imageView = itemView.findViewById(R.id.post_item_image) as ImageView
	val text = itemView.findViewById(R.id.post_item_text) as TextView
	val author = itemView.findViewById(R.id.post_item_creator) as TextView

	fun bind(post: EGF2Post, image: EGF2File?, creator: EGF2User?) {
		text.text = post.desc

		author.text = ""

		creator?.let { author.text = creator.email }

		Glide.with(itemView.context)
				.load(image?.url)
				.fitCenter()
				.into(imageView)
	}
}