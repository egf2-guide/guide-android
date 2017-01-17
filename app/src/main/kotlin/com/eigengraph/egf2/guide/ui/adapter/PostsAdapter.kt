package com.eigengraph.egf2.guide.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.eigengraph.egf2.guide.models.EGF2File
import com.eigengraph.egf2.guide.models.EGF2Post
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.ui.adapter.holder.PostItemViewHolder
import com.eigengraph.egf2.guide.ui.anko.PostItemUI
import com.eigengraph.egf2.guide.util.RecyclerClickListener
import java.util.*

class PostsAdapter : RecyclerView.Adapter<PostItemViewHolder> {
	override fun getItemCount() = list.size

	val list: ArrayList<EGF2Post>
	val images: HashMap<String, EGF2File>
	val creators: HashMap<String, EGF2User>

	private val listener: RecyclerClickListener
	private var isAdmin = false

	constructor(li: ArrayList<EGF2Post>, mapImage: HashMap<String, EGF2File>, mapCreator: HashMap<String, EGF2User>, listener: RecyclerClickListener, admin: Boolean = false) {
		list = li
		images = mapImage
		creators = mapCreator
		this.listener = listener
		isAdmin = admin
	}

	override fun onBindViewHolder(holder: PostItemViewHolder?, position: Int) {
		val post = list[position]
		holder?.bind(post, images[post.image], creators[post.creator])
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PostItemViewHolder {
		return PostItemViewHolder(PostItemUI().bind(parent!!), listener, isAdmin)
	}
}