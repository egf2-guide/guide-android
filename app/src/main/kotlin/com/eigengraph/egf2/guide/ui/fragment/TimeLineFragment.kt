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
import com.eigengraph.egf2.guide.ui.anko.TimeLineLayout
import rx.functions.Action1

class TimeLineFragment : Fragment() {
	companion object {
		fun newInstance() = TimeLineFragment()
	}

	private var layout = TimeLineLayout()

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return layout.bind(this)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		if (DataManager.user != null) {
			getTimeline()
		} else {
			EGF2Bus.subscribeForObject(EGF2Bus.EVENT.OBJECT_CREATED, EGF2Model.ME, Action1 {
				getTimeline()
			})
		}
	}

	private fun getTimeline() {
		EGF2.getEdgeObjects<EGF2Post>(DataManager.user?.id as String, EGF2User.EDGE_TIMELINE, null, EGF2.DEF_COUNT, null, true)
				.subscribe({

				}, {

				})
	}

	override fun onDestroyView() {
		layout.unbind(this)
		super.onDestroyView()
	}
}