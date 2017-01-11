package com.eigengraph.egf2.guide.ui

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.framework.EGF2Bus
import com.eigengraph.egf2.guide.DataManager
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.models.EGF2Comment
import com.eigengraph.egf2.guide.models.EGF2File
import com.eigengraph.egf2.guide.models.EGF2Post
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.ui.adapter.CommentsAdapter
import com.eigengraph.egf2.guide.ui.anko.CommentUI
import com.eigengraph.egf2.guide.ui.anko.PostActivityLayout
import com.eigengraph.egf2.guide.util.CommentClickListener
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
	var swipe: SwipeRefreshLayout? = null

	private var listComment = ArrayList<EGF2Comment>()
	private lateinit var adapter: CommentsAdapter
	private var mapCreator = HashMap<String, EGF2User>()

	private lateinit var post: EGF2Post

	private var isEndPage = false
	private var isLoading = false
	private var after: EGF2Comment? = null
	private var isOffended = false

	var comment: EditText? = null

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

		adapter = CommentsAdapter(listComment, mapCreator, (post.creator == DataManager.user?.id), object : CommentClickListener {
			override fun onEditClick(position: Int) {
				val comm = listComment[position]
				AlertDialog.Builder(this@PostActivity)
						.setTitle("Edit Comment")
						.setView(CommentUI().bind(this@PostActivity))
						.setPositiveButton("SAVE", DialogInterface.OnClickListener { dialogInterface, i ->
							comm.text = comment?.text.toString()
							EGF2.updateObject(comm.id, comm.update(), EGF2Comment::class.java)
									.subscribe({
										adapter.notifyDataSetChanged()
									}, {
										list?.snackbar(it.message.toString())
									})
						})
						.setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialogInterface, i -> })
						.show()

				comment?.setText(comm.text)
			}

			override fun onDeleteClick(position: Int) {
				AlertDialog.Builder(this@PostActivity)
						.setTitle("Delete Comment?")
						.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
							EGF2.deleteObjectFromEdge(post.id, EGF2Post.EDGE_COMMENTS, listComment[position])
									.subscribe({

									}, {
										list?.snackbar(it.message.toString())
									})
						})
						.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i -> })
						.show()
			}

			override fun onElementClick(position: Int) {

			}
		})

		list?.adapter = adapter

		getComments(true)

		EGF2Bus.subscribeForEdge(EGF2Bus.EVENT.EDGE_ADDED, post.id, EGF2Post.EDGE_COMMENTS, Action1 {
			list?.snackbar("EDGE_ADDED")
			getComments(true)
		})

		EGF2Bus.subscribeForEdge(EGF2Bus.EVENT.EDGE_REMOVED, post.id, EGF2Post.EDGE_COMMENTS, Action1 {
			event ->
			list?.snackbar("EDGE_REMOVED")
			val iter = listComment.iterator()
			while (iter.hasNext()) {
				val user = iter.next()
				if (user.id == event.obj?.getId())
					iter.remove()
			}
			adapter.notifyDataSetChanged()
		})

		swipe?.setOnRefreshListener {
			isEndPage = false
			after = null
			getComments(false)
		}

		if (post.creator != DataManager.user?.id) {
			EGF2.getEdgeObject(post.id, EGF2Post.EDGE_OFFENDED, DataManager.user?.id as String, null, false, EGF2User::class.java)
					.subscribe({
						isOffended = false
						supportInvalidateOptionsMenu()
					}, {
						isOffended = true
						supportInvalidateOptionsMenu()
					})
		}
	}

	private fun getComments(useCache: Boolean) {
		swipe?.isRefreshing = true
		isLoading = true
		EGF2.getEdgeObjects(post.id, EGF2Post.EDGE_COMMENTS, after, EGF2.DEF_COUNT, arrayOf("creator"), useCache, EGF2Comment::class.java)
				.subscribe({
					if (EGF2.isFirstPage(it)) listComment.clear()

					if (it.last == null) isEndPage = true

					listComment.addAll(it.results as ArrayList<EGF2Comment>)
					adapter.notifyDataSetChanged()

					after = if (listComment.isNotEmpty()) listComment[listComment.size - 1] else null

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
					if (swipe?.isRefreshing as Boolean) swipe?.isRefreshing = false
					isLoading = false
				}, {
					if (swipe?.isRefreshing as Boolean) swipe?.isRefreshing = false
					isLoading = false
					list?.snackbar(it.message.toString())
				})
	}

	override fun onDestroy() {
		postLayout.unbind(this)
		super.onDestroy()
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater?.inflate(R.menu.post, menu)
		if (post.creator != DataManager.user?.id) {
			menu?.findItem(R.id.action_offended)?.isEnabled = false
		} else {
			menu?.findItem(R.id.action_offended)?.isVisible = false
		}
		return true//super.onCreateOptionsMenu(menu)
	}

	override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
		menu?.findItem(R.id.action_offended)?.isEnabled = isOffended
		return super.onPrepareOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.itemId == android.R.id.home) finish()
		if (item?.itemId == R.id.action_offended) offended()
		return super.onOptionsItemSelected(item)
	}

	private fun offended() {
		EGF2.createEdge(post.id, EGF2Post.EDGE_OFFENDED, DataManager.user as EGF2User)
				.subscribe({
					isOffended = false
					supportInvalidateOptionsMenu()
				}, {
					isOffended = false
					supportInvalidateOptionsMenu()
					list?.snackbar(it.message.toString())
				})
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

	val scrollListener = object : RecyclerView.OnScrollListener() {
		override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
			super.onScrollStateChanged(recyclerView, newState)

			val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
			val visibleItemsCount = layoutManager.childCount
			val totalItemsCount = layoutManager.itemCount
			val firstVisibleItemPos = layoutManager.findFirstVisibleItemPosition()

			if (visibleItemsCount + firstVisibleItemPos >= totalItemsCount && !isEndPage && !isLoading) {
				getComments(true)
			}
		}
	}
}