package com.eigengraph.egf2.guide.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.eigengraph.egf2.guide.models.EGF2Comment
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.ui.adapter.holder.CommentItemViewHolder
import com.eigengraph.egf2.guide.ui.anko.CommentItemUI
import com.eigengraph.egf2.guide.util.RecyclerClickListener
import java.util.*

class CommentsAdapter : RecyclerView.Adapter<CommentItemViewHolder> {
	override fun getItemCount() = list.size

	val list: ArrayList<EGF2Comment>
	val creators: HashMap<String, EGF2User>

	private val listener: RecyclerClickListener

	constructor(li: ArrayList<EGF2Comment>, mapCreator: HashMap<String, EGF2User>, listener: RecyclerClickListener) {
		list = li
		creators = mapCreator
		this.listener = listener
	}

	override fun onBindViewHolder(holder: CommentItemViewHolder?, position: Int) {
		val comment = list[position]
		holder?.bind(comment, creators[comment.creator])
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommentItemViewHolder {
		return CommentItemViewHolder(CommentItemUI().bind(parent!!), listener)
	}
}