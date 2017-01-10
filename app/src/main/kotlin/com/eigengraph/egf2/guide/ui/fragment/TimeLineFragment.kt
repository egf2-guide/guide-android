package com.eigengraph.egf2.guide.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.framework.EGF2Bus
import com.eigengraph.egf2.framework.models.EGF2Model
import com.eigengraph.egf2.guide.DataManager
import com.eigengraph.egf2.guide.R
import com.eigengraph.egf2.guide.models.EGF2File
import com.eigengraph.egf2.guide.models.EGF2Post
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.ui.PostActivity
import com.eigengraph.egf2.guide.ui.adapter.PostsAdapter
import com.eigengraph.egf2.guide.ui.anko.TimeLineLayout
import com.eigengraph.egf2.guide.util.RecyclerClickListener
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent
import org.jetbrains.anko.support.v4.startActivity
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import java.util.*
import java.util.concurrent.TimeUnit


class TimeLineFragment : Fragment() {
	companion object {
		fun newInstance() = TimeLineFragment()
	}

	private lateinit var layout: TimeLineLayout
	private lateinit var adapter: PostsAdapter

	private var list = ArrayList<EGF2Post>()
	private var mapImage = HashMap<String, EGF2File>()
	private var mapCreator = HashMap<String, EGF2User>()

	var swipe: SwipeRefreshLayout? = null

	private var sub: Subscription? = null

	private var isEndPage = false
	private var isLoading = false
	private var isSearchCollapse = true
	private var after: EGF2Post? = null

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
		layout = TimeLineLayout(adapter)
		return layout.bind(this)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		setHasOptionsMenu(true)

		if (DataManager.user != null) {
			getTimeline(true)
		} else {
			EGF2Bus.subscribeForObject(EGF2Bus.EVENT.OBJECT_LOADED, EGF2Model.ME, Action1 {
				getTimeline(true)
			})
		}

		swipe?.setOnRefreshListener {
			isEndPage = false
			after = null
			getTimeline(false)
		}
	}

	private fun getTimeline(useCache: Boolean) {
		swipe?.isRefreshing = true
		isLoading = true
		EGF2.getEdgeObjects(DataManager.user?.id as String, EGF2User.EDGE_TIMELINE, after, EGF2.DEF_COUNT, null, useCache, EGF2Post::class.java)
				.subscribe({
					if (EGF2.isFirstPage(it)) clearSearchList()
					if (it.last == null) isEndPage = true
					fillTimeLine(it.count, it.results)
					after = if (list.isNotEmpty()) list[list.size - 1] else null
					if (swipe?.isRefreshing as Boolean) swipe?.isRefreshing = false
					isLoading = false
				}, {
					if (swipe?.isRefreshing as Boolean) swipe?.isRefreshing = false
					isLoading = false
				})
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.search, menu)

		val item = menu?.findItem(R.id.action_search)
		val searchView = MenuItemCompat.getActionView(item) as SearchView

		RxSearchView.queryTextChangeEvents(searchView)
				.debounce {
					if (it.queryText().isNullOrEmpty()) {
						Observable.empty<SearchViewQueryTextEvent>()
					} else {
						Observable.empty<SearchViewQueryTextEvent>().delay(450, TimeUnit.MILLISECONDS)
					}
				}
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({
					Log.d("SEARCH", it.toString())
					if (it.queryText().isEmpty()) {
						if (!isSearchCollapse) {
							clearSearchList()
						}
					} else {
						search(it.queryText().toString())
					}
				}, {
					Log.d("SEARCH", it.toString())
				})

		MenuItemCompat.setOnActionExpandListener(item,
				object : MenuItemCompat.OnActionExpandListener {
					override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
						Log.d("SEARCH", "onMenuItemActionCollapse")
						isSearchCollapse = true
						swipe?.isEnabled = true
						clearSearchList()
						getTimeline(true)
						return true
					}

					override fun onMenuItemActionExpand(item: MenuItem): Boolean {
						Log.d("SEARCH", "onMenuItemActionExpand")
						isSearchCollapse = false
						swipe?.isEnabled = false
						clearSearchList()
						return true
					}
				})
	}

	private fun clearSearchList() {
		list.clear()
		adapter.notifyDataSetChanged()
		swipe?.isRefreshing = false
	}

	private fun search(text: String) {
		swipe?.isRefreshing = true
		isEndPage = true
		clearSearchList()
		EGF2.search(text, "", EGF2.DEF_COUNT, "post", "desc", "", "", "", "image,creator", EGF2Post::class.java)
				.subscribe({
					fillTimeLine(it.count, it.results)
					swipe?.isRefreshing = false
				}, {
					swipe?.isRefreshing = false
				})
	}

	private fun fillTimeLine(count: Int, results: List<EGF2Post>) {
		list.addAll(results as ArrayList<EGF2Post>)
		adapter.notifyDataSetChanged()

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
	}

	override fun onDestroyView() {
		layout.unbind(this)
		sub?.unsubscribe()
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
				if (isSearchCollapse)
					getTimeline(true)
			}
		}
	}
}