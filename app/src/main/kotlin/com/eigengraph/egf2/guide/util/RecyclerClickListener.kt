package com.eigengraph.egf2.guide.util

interface RecyclerClickListener {
	fun onElementClick(position: Int)
}

interface CommentClickListener : RecyclerClickListener {
	fun onEditClick(position: Int)
	fun onDeleteClick(position: Int)
}