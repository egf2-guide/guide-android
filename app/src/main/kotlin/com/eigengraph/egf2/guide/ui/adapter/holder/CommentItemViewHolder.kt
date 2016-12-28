package com.eigengraph.egf2.guide.ui.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.models.EGF2Comment
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.util.RecyclerClickListener

class CommentItemViewHolder(itemView: View, listener: RecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
	val text = itemView.findViewById(R.id.post_item_text) as TextView
	val author = itemView.findViewById(R.id.post_item_creator) as TextView

	init {
		itemView.setOnClickListener { listener.onElementClick(adapterPosition) }
	}

	fun bind(comment: EGF2Comment, creator: EGF2User?) {
		text.text = comment.text

		author.text = ""

		creator?.let { author.text = creator.email }
	}
}