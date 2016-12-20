package com.eigengraph.egf2.guide.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
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
import com.eigengraph.egf2.guide.ui.adapter.PostsAdapter
import com.eigengraph.egf2.guide.ui.anko.PostsLayout
import org.jetbrains.anko.startActivity
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

	var swipe: SwipeRefreshLayout? = null

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Log.d(PostsFragment::class.java.simpleName, "onCreateView")
		adapter = PostsAdapter(list, mapImage, mapCreator)
		layout = PostsLayout(adapter)
		return layout.bind(this)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		Log.d(PostsFragment::class.java.simpleName, "onActivityCreated")
		if (DataManager.user != null) {
			getPosts(DataManager.user as EGF2User)
		} else {
			EGF2Bus.subscribeForObject(EGF2Bus.EVENT.OBJECT_LOADED, EGF2Model.ME, Action1 {
				getPosts(it?.obj as EGF2User)
			})
		}

		swipe?.setOnRefreshListener {
			getPosts(DataManager.user as EGF2User, false)
		}

		(activity as MainActivity).fab?.setOnClickListener {
			activity.startActivity<NewPostActivity>()
		}
	}

	private var mapImage = HashMap<String, EGF2File>()
	private var mapCreator = HashMap<String, EGF2User>()

	private fun getPosts(it: EGF2User, useCache: Boolean = true) {

		sub = EGF2Bus.subscribeForEdge(EGF2Bus.EVENT.EDGE_ADDED, it.id, EGF2User.EDGE_POSTS, Action1 {
			Log.d(PostsFragment::class.java.simpleName, "subscribeForEdge")
		})

		EGF2.getEdgeObjects(it.id, EGF2User.EDGE_POSTS, null, EGF2.DEF_COUNT, arrayOf("image", "creator"), useCache, EGF2Model::class.java)
				.subscribe({
					if (EGF2.isFirstPage(it)) list.clear()

					list.addAll(it.results as ArrayList<EGF2Post>)
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

					if (swipe?.isRefreshing as Boolean) swipe?.isRefreshing = false

					Log.d(PostsFragment::class.java.simpleName, "getEdgeObjects onNext")
				}, {
					Log.d(PostsFragment::class.java.simpleName, "getEdgeObjects onError")
				})
	}

	override fun onDestroyView() {
		Log.d(PostsFragment::class.java.simpleName, "onDestroyView")
		sub?.unsubscribe()
		layout.unbind(this)
		super.onDestroyView()
	}
}