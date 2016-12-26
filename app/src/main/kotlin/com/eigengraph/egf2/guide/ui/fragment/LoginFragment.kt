package com.eigengraph.egf2.guide.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.framework.LoginModel
import com.eigengraph.egf2.framework.RegisterModel
import com.eigengraph.egf2.guide.ui.MainActivity
import com.eigengraph.egf2.guide.ui.anko.LoginFragmentLayout
import com.eigengraph.egf2.guide.ui.anko.RegistrationUI
import com.eigengraph.egf2.guide.util.parseError
import com.eigengraph.egf2.guide.util.snackbar
import com.jakewharton.rxbinding.widget.RxTextView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.startActivity
import org.joda.time.DateTime
import rx.Observable
import java.util.*


class LoginFragment : Fragment() {
	companion object {
		fun newInstance() = LoginFragment()
	}

	private var layout = LoginFragmentLayout()

	private var dobDate = DateTime.now()

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return layout.bind(this)
	}

	internal fun login(email: EditText?, pass: EditText?) {
		EGF2.login(LoginModel(email?.text.toString(), pass?.text.toString()))
				.subscribe({
					val pref = activity.defaultSharedPreferences
					pref.edit().putString("token", it).apply()
					activity.startActivity<MainActivity>()
					activity.finishAffinity()
				}, {
					view?.snackbar(parseError(it.message.toString()))
				})
	}

	override fun onDestroyView() {
		layout.unbind(this)
		super.onDestroyView()
	}

	var fname: EditText? = null
	var lname: EditText? = null
	var email: EditText? = null
	var dob: EditText? = null
	var pass: EditText? = null
	var pass2: EditText? = null

	fun register() {

		val dialog = AlertDialog.Builder(activity)
				.setTitle("Register")
				.setView(RegistrationUI().bind(this))
				.setPositiveButton("OK", { dialogInterface, i -> })
				.setNegativeButton("CANCEL", { dialogInterface, i -> })
				.create()
		dialog.show()

		dob?.setOnFocusChangeListener { view, b ->
			if (b) showBDateDialog()
		}

		dob?.setOnClickListener {
			showBDateDialog()
		}

		val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
		btn.setOnClickListener {
			EGF2.register(
					RegisterModel(fname?.text.toString(),
							lname?.text.toString(),
							email?.text.toString(),
							dobDate.toString(),
							pass?.text.toString()))
					.subscribe({
						dialog.dismiss()
						val pref = activity.defaultSharedPreferences
						pref.edit().putString("token", it).apply()
						activity.startActivity<MainActivity>()
						activity.finishAffinity()
					}, {
						view?.snackbar(parseError(it.message.toString()))
					})
		}

		Observable.combineLatest(
				RxTextView.textChanges(pass as EditText),
				RxTextView.textChanges(pass2 as EditText),
				{ pass, pass2 ->
					dob?.text.toString().isNotEmpty() && pass.isNotEmpty() && pass.toString() == pass2.toString()
				})
				.subscribe {
					btn.isEnabled = it
				}
	}

	private fun showBDateDialog() {
		val now = Calendar.getInstance()
		val dpd = DatePickerDialog.newInstance(
				{
					view, year, monthOfYear, dayOfMonth ->
					dob?.setText("${(monthOfYear + 1).toString()}/${dayOfMonth.toString()}/${year.toString()}")
					dobDate = DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0)
				},
				now.get(Calendar.YEAR),
				now.get(Calendar.MONTH),
				now.get(Calendar.DAY_OF_MONTH)
		)
		dpd.isThemeDark = false
		dpd.vibrate(false)
		dpd.dismissOnPause(true)
		dpd.showYearPickerFirst(true)
		dpd.setTitle("Birthday")
		dpd.show(activity.fragmentManager, "Datepickerdialog")
	}

	fun forgot() {

	}
}