package com.eigengraph.egf2.guide.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.framework.EGF2Bus
import com.eigengraph.egf2.guide.models.EGF2Comment
import com.eigengraph.egf2.guide.models.EGF2File
import com.eigengraph.egf2.guide.models.EGF2Post
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.ui.adapter.CommentsAdapter
import com.eigengraph.egf2.guide.ui.anko.PostActivityLayout
import com.eigengraph.egf2.guide.util.RecyclerClickListener
import com.eigengraph.egf2.guide.util.snackbar
import rx.Observable
import rx.functions.Action1
import java.util.*

class PostActivity : AppCompatActivity() {

	private val postLayout = PostActivityLayout()

	var imageView: ImageView? = null
	var text: TextView? = null
	var author: TextView? = null
	var list: RecyclerView? = null
	var send: ImageButton? = null
	var message: EditText? = null

	private var listComment = ArrayList<EGF2Comment>()
	private lateinit var adapter: CommentsAdapter
	private var mapCreator = HashMap<String, EGF2User>()

	private lateinit var post: EGF2Post

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(postLayout.bind(this))

		post = intent.getSerializableExtra("post") as EGF2Post
		val image: EGF2File = intent.getSerializableExtra("image") as EGF2File
		val creator: EGF2User = intent.getSerializableExtra("creator") as EGF2User

		Glide.with(imageView?.context)
				.load(image.url)
				.fitCenter()
				.into(imageView)

		author?.text = creator.email

		text?.text = post.desc

		adapter = CommentsAdapter(listComment, mapCreator, object : RecyclerClickListener {
			override fun onElementClick(position: Int) {
			}
		})

		list?.adapter = adapter

		getComments(false)

		EGF2Bus.subscribeForEdge(EGF2Bus.EVENT.EDGE_ADDED, post.id, EGF2Post.EDGE_COMMENTS, Action1 {
			list?.snackbar("EDGE_ADDED")
			getComments(false)
		})
	}

	private fun getComments(useCache: Boolean) {
		EGF2.getEdgeObjects(post.id, EGF2Post.EDGE_COMMENTS, null, EGF2.DEF_COUNT, arrayOf("creator"), useCache, EGF2Comment::class.java)
				.subscribe({
					if (EGF2.isFirstPage(it)) listComment.clear()

					listComment.addAll(it.results as ArrayList<EGF2Comment>)
					adapter.notifyDataSetChanged()

					val c = ArrayList<Observable<EGF2User>>()
					listComment.forEach {
						c.add(EGF2.getObjectByID(it.creator, null, true, EGF2User::class.java))
					}

					Observable.zip(c, { it }).subscribe({
						it.forEach {
							mapCreator.put((it as EGF2User).id, it)
						}
						adapter.notifyDataSetChanged()

					}, {})
				}, {
				})
	}

	override fun onDestroy() {
		postLayout.unbind(this)
		super.onDestroy()
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.itemId == android.R.id.home) finish()
		return super.onOptionsItemSelected(item)
	}

	fun sendMessage() {
		val comment = EGF2Comment()
		comment.object_type = EGF2Comment.OBJECT_TYPE
		comment.text = message?.text.toString()

		EGF2.createObjectOnEdge(post.id, EGF2Post.EDGE_COMMENTS, comment.create(), EGF2Comment::class.java)
				.subscribe({
					message?.setText("")
					list?.smoothScrollToPosition(0)
				}, {
					list?.snackbar(it.message.toString())
				})
	}
}