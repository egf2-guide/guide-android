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


		//EGF2.register(RegisterModel("Alex", "Derendyaev", "alexxxdev@yandex.ru", DateTime.now().toString(), "qazwsx123")).subscribe({}, {})

/*		var id:String = ""
		EGF2.login(LoginModel("alexxxdev@yandex.ru", "qazwsx123"))
				.flatMap{
					EGF2.getSelfUser(null, false, EGF2User::class.java)
				}
				.flatMap {
					id = it.id
					EGF2.getEdgeObjects<EGF2Post>(it.id, EGF2User.EDGE_ROLES, null, EGF2.DEF_COUNT, null, true)
				}
				.flatMap {
					EGF2.getEdgeObjects<EGF2Post>(id, EGF2User.EDGE_POSTS, null, EGF2.DEF_COUNT, null, true)
				}
				.flatMap {
					EGF2.getEdgeObjects<EGF2Post>(id, EGF2User.EDGE_TIMELINE, null, EGF2.DEF_COUNT, null, true)
				}
				.subscribe({

				}, {

				})*/

		/*EGF2.login(LoginModel("", "")).subscribe({}, {})

		EGF2.verifyEmail("").subscribe({}, {})
		EGF2.logout().subscribe({}, {})
		EGF2.forgotPassword("email").subscribe({}, {})
		EGF2.resetPassword(ResetPasswordModel("", "")).subscribe({}, {})
		EGF2.changePassword(ChangePasswordModel("", "")).subscribe({}, {})
		EGF2.resendEmailVerification().subscribe({}, {})

		EGF2.getSelfUser(clazz = EGF2User::class.java).subscribe({}, {})

		EGF2.getObjectByID("id_object", "", true, EGF2User::class.java)
				.subscribe({}, {})

		EGF2.getObjectByID("id_object", clazz = EGF2Comment::class.java)
				.subscribe({}, {})

		EGF2.getEdgeObjects<EGF2User>("id_object", "edge", EGF2User(), 10).subscribe({}, {})

		EGF2.getEdgeObject("id_src", "edge", "id_dst", clazz = EGF2Comment::class.java).subscribe({}, {})

		EGF2.createObject(EGF2User().create(), EGF2Comment::class.java).subscribe({}, {})
		EGF2.createObjectOnEdge("id_object", "edge", "", EGF2Comment::class.java).subscribe({}, {})
		EGF2.createEdge("id_src", "edge", EGF2File()).subscribe({}, {})

		EGF2.updateObject("id_object", EGF2User().update(), EGF2Comment::class.java).subscribe({}, {})

		EGF2.deleteObject("id_object").subscribe({}, {})

		EGF2.deleteObjectFromEdge("id_src", "edge", EGF2File()).subscribe({}, {})

		EGF2.search<EGF2File>("q", "object", "fields", "filters", "sort", "expand").subscribe({}, {})

		EGF2.uploadFile("file", "mime", "title", EGF2File::class.java).subscribe({}, {})
		EGF2.uploadImage("file", "mime", "title", "kind", EGF2File::class.java).subscribe({}, {})*/
	}

}