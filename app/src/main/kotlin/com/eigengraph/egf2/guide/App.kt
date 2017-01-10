package com.eigengraph.egf2.guide

import android.app.Application
import com.eigengraph.egf2.framework.EGF2
import org.jetbrains.anko.defaultSharedPreferences

class App : Application() {
	override fun onCreate() {
		super.onCreate()

		val pref = defaultSharedPreferences
		val token = pref.getString("token", "")

		EGF2.Builder(applicationContext)
				.config(EGF2Config())
				.gson(EGF2GsonFactory())
				.types(EGF2MapTypesFactory())
				.version(1)
				.token(token)
				.debug(true)
				.build()
	}
}