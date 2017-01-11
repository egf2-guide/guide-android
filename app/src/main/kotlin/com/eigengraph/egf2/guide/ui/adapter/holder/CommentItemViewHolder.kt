package com.eigengraph.egf2.guide.ui.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.eigengraph.egf2.guide.DataManager
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.models.EGF2Comment
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.util.CommentClickListener

class CommentItemViewHolder(itemView: View, val isDeleteComment: Boolean, listener: CommentClickListener) : RecyclerView.ViewHolder(itemView) {
	val text = itemView.findViewById(R.id.post_item_text) as TextView
	val author = itemView.findViewById(R.id.post_item_creator) as TextView
	val edit = itemView.findViewById(R.id.comment_item_edit) as Button
	val delete = itemView.findViewById(R.id.comment_item_delete) as Button

	init {
		itemView.setOnClickListener { listener.onElementClick(adapterPosition) }
		edit.setOnClickListener { listener.onEditClick(adapterPosition) }
		delete.setOnClickListener { listener.onDeleteClick(adapterPosition) }
	}

	fun bind(comment: EGF2Comment, creator: EGF2User?) {
		text.text = comment.text

		author.text = ""

		creator?.let { author.text = creator.email }

		if (isDeleteComment) delete.visibility = View.VISIBLE
		else delete.visibility = View.GONE

		if (comment.creator == DataManager.user?.id) edit.visibility = View.VISIBLE
		else edit.visibility = View.GONE
	}
}