package com.eigengraph.egf2.guide.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.framework.EGF2Bus
import com.eigengraph.egf2.framework.models.EGF2Model
import com.eigengraph.egf2.guide.DataManager
import com.eigengraph.egf2.guide.models.EGF2File
import com.eigengraph.egf2.guide.models.EGF2Post
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.ui.MainActivity
import com.eigengraph.egf2.guide.ui.NewPostActivity
import com.eigengraph.egf2.guide.ui.PostActivity
import com.eigengraph.egf2.guide.ui.adapter.PostsAdapter
import com.eigengraph.egf2.guide.ui.anko.PostsLayout
import com.eigengraph.egf2.guide.util.RecyclerClickListener
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import rx.Observable
import rx.Subscription
import rx.functions.Action1
import java.util.*

class PostsFragment : Fragment() {
	companion object {
		fun newInstance() = PostsFragment()
	}

	private lateinit var layout: PostsLayout
	private lateinit var adapter: PostsAdapter

	private var sub: Subscription? = null

	private var list = ArrayList<EGF2Post>()
	private var mapImage = HashMap<String, EGF2File>()
	private var mapCreator = HashMap<String, EGF2User>()

	var swipe: SwipeRefreshLayout? = null
	var recyclerView: RecyclerView? = null

	private var isEndPage = false
	private var isLoading = false
	private var after: EGF2Post? = null

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Log.d(PostsFragment::class.java.simpleName, "onCreateView")
		adapter = PostsAdapter(list, mapImage, mapCreator, object : RecyclerClickListener {
			override fun onElementClick(position: Int) {
				val post = list[position]
				val image: EGF2File = mapImage[post.image] as EGF2File
				val creator: EGF2User = mapCreator[post.creator] as EGF2User
				startActivity<PostActivity>(
						Pair("post", post),
						Pair("image", image),
						Pair("creator", creator))
			}
		})
		layout = PostsLayout(adapter)
		return layout.bind(this)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		recyclerView?.addOnScrollListener(scrollListener)
		Log.d(PostsFragment::class.java.simpleName, "onActivityCreated")
		if (DataManager.user != null) {
			subscribe()
			getPosts(DataManager.user as EGF2User)
		} else {
			EGF2Bus.subscribeForObject(EGF2Bus.EVENT.OBJECT_LOADED, EGF2Model.ME, Action1 {
				subscribe()
				getPosts(it?.obj as EGF2User)
			})
		}

		swipe?.setOnRefreshListener {
			isEndPage = false
			after = null
			getPosts(DataManager.user as EGF2User, false)
		}

		(activity as MainActivity).fab?.setOnClickListener {
			activity.startActivity<NewPostActivity>()
		}
	}

	private fun getPosts(it: EGF2User, useCache: Boolean = true) {
		isLoading = true
		swipe?.isRefreshing = true
		EGF2.getEdgeObjects(it.id, EGF2User.EDGE_POSTS, after, EGF2.DEF_COUNT, arrayOf("image", "creator"), useCache, EGF2Post::class.java)
				.subscribe({
					if (EGF2.isFirstPage(it)) list.clear()
					if (it.last == null) isEndPage = true
					list.addAll(it.results as ArrayList<EGF2Post>)
					adapter.notifyDataSetChanged()
					after = if (list.isNotEmpty()) list[list.size - 1] else null
					val l = ArrayList<Observable<EGF2File>>()
					val c = ArrayList<Observable<EGF2User>>()
					list.forEach {
						l.add(EGF2.getObjectByID(it.image, null, true, EGF2File::class.java))
						c.add(EGF2.getObjectByID(it.creator, null, true, EGF2User::class.java))
					}

					Observable.zip(l, { it }).subscribe({
						it.forEach {
							mapImage.put((it as EGF2File).id, it)
						}
						adapter.notifyDataSetChanged()
					}, {})

					Observable.zip(c, { it }).subscribe({
						it.forEach {
							mapCreator.put((it as EGF2User).id, it)
						}
						adapter.notifyDataSetChanged()
					}, {})

					if (swipe?.isRefreshing as Boolean) swipe?.isRefreshing = false
					isLoading = false
					Log.d(PostsFragment::class.java.simpleName, "getEdgeObjects onNext")
				}, {
					if (swipe?.isRefreshing as Boolean) swipe?.isRefreshing = false
					isLoading = false
					Log.d(PostsFragment::class.java.simpleName, "getEdgeObjects onError")
				})
	}

	private fun subscribe() {
		EGF2Bus.subscribeForObject(EGF2Bus.EVENT.OBJECT_UPDATED, null, Action1 {
			event ->
			Log.d(PostsFragment::class.java.simpleName, "subscribeForObject")
			list.forEach {
				if (it.id == event.obj?.getId()) {
					it.desc = (event.obj as EGF2Post)?.desc
				}
			}
			adapter.notifyDataSetChanged()
		})

		sub = EGF2Bus.subscribeForEdge(EGF2Bus.EVENT.EDGE_ADDED, DataManager.user?.id as String, EGF2User.EDGE_POSTS, Action1 {
			Log.d(PostsFragment::class.java.simpleName, "subscribeForEdge")

			val post = it.obj as EGF2Post

			list.add(0, post)
			adapter.notifyDataSetChanged()

			EGF2.getObjectByID(post.image, null, true, EGF2File::class.java)
					.subscribe({
						mapImage.put((it as EGF2File).id, it)
						adapter.notifyDataSetChanged()
					}, {})

			EGF2.getObjectByID(post.creator, null, true, EGF2User::class.java)
					.subscribe({
						mapCreator.put((it as EGF2User).id, it)
						adapter.notifyDataSetChanged()
					}, {})

			recyclerView?.smoothScrollToPosition(0)
		})
	}

	override fun onDestroyView() {
		Log.d(PostsFragment::class.java.simpleName, "onDestroyView")
		sub?.unsubscribe()
		layout.unbind(this)
		super.onDestroyView()
	}

	val scrollListener = object : RecyclerView.OnScrollListener() {
		override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
			super.onScrollStateChanged(recyclerView, newState)

			val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
			val visibleItemsCount = layoutManager.childCount
			val totalItemsCount = layoutManager.itemCount
			val firstVisibleItemPos = layoutManager.findFirstVisibleItemPosition()

			if (visibleItemsCount + firstVisibleItemPos >= totalItemsCount && !isEndPage && !isLoading) {
				getPosts(DataManager.user as EGF2User, true)
			}
		}
	}
}