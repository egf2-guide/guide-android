package com.eigengraph.egf2.guide.util

import android.support.design.widget.Snackbar
import android.view.View

fun View.snackbar(messageResId: Int) {
	Snackbar.make(this, messageResId, Snackbar.LENGTH_SHORT).show()
}

fun View.snackbarLong(messageResId: Int) {
	Snackbar.make(this, messageResId, Snackbar.LENGTH_LONG).show()
}

fun View.snackbar(message: String) {
	Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.snackbarLong(message: String) {
	Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}
