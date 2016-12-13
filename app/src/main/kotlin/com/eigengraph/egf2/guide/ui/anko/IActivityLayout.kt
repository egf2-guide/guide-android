package com.eigengraph.egf2.guide.ui.anko

import android.support.v7.app.AppCompatActivity
import android.view.View

interface IActivityLayout {
	fun bind(activity: AppCompatActivity): View
	fun unbind(activity: AppCompatActivity)
}