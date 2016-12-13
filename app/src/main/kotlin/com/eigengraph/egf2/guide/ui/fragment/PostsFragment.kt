package com.eigengraph.egf2.guide.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.framework.EGF2Bus
import com.eigengraph.egf2.framework.models.EGF2Model
import com.eigengraph.egf2.guide.DataManager
import com.eigengraph.egf2.guide.models.EGF2Post
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.ui.MainActivity
import com.eigengraph.egf2.guide.ui.NewPostActivity
import com.eigengraph.egf2.guide.ui.anko.PostsLayout
import org.jetbrains.anko.startActivity
import rx.Subscription
import rx.functions.Action1

class PostsFragment : Fragment() {
	companion object {
		fun newInstance() = PostsFragment()
	}

	private var layout = PostsLayout()

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return layout.bind(this)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		if (DataManager.user != null) {
			getPosts(DataManager.user as EGF2User)
		} else {
			EGF2Bus.subscribeForObject(EGF2Bus.EVENT.OBJECT_CREATED, EGF2Model.ME, Action1 {
				getPosts(it?.obj as EGF2User)
			})
		}

		(activity as MainActivity).fab?.setOnClickListener {
			activity.startActivity<NewPostActivity>()
		}
	}

	private fun getPosts(it: EGF2User): Subscription? {
		return EGF2.getEdgeObjects<EGF2Post>(it.id, EGF2User.EDGE_POSTS, null, EGF2.DEF_COUNT, null, true)
				.subscribe({

				}, {

				})
	}

	override fun onDestroyView() {
		layout.unbind(this)
		super.onDestroyView()
	}
}