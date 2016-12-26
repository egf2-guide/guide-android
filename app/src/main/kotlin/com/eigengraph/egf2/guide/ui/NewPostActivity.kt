package com.eigengraph.egf2.guide.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.eigengraph.egf2.framework.EGF2
import com.eigengraph.egf2.guide.DataManager
import com.eigengraph.egf2.guide.models.EGF2File
import com.eigengraph.egf2.guide.models.EGF2Post
import com.eigengraph.egf2.guide.models.EGF2User
import com.eigengraph.egf2.guide.ui.anko.NewPostActivityLayout
import com.eigengraph.egf2.guide.util.parseError
import com.eigengraph.egf2.guide.util.snackbar
import java.io.File
import java.util.*

class NewPostActivity : AppCompatActivity() {

	var container: LinearLayout? = null
	var fab: FloatingActionButton? = null
	var image: ImageView? = null
	var text: EditText? = null
	var progress: ProgressBar? = null

	private val newPostLayout = NewPostActivityLayout()

	private val PERMISSIONS_REQUEST_STORAGE = 99
	private val RESULT_LOAD_IMAGE = 999

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(newPostLayout.bind(this))

		image?.setOnClickListener {
			if (ContextCompat.checkSelfPermission(NewPostActivity@ this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
					!= PackageManager.PERMISSION_GRANTED) {
				if (ActivityCompat.shouldShowRequestPermissionRationale(NewPostActivity@ this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					Snackbar.make(container as View, "Storage permission is needed to get Image from gallery",
							Snackbar.LENGTH_INDEFINITE)
							.setAction("Ok", {
								ActivityCompat.requestPermissions(NewPostActivity@ this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
										PERMISSIONS_REQUEST_STORAGE)
							})
							.show()
				} else {
					ActivityCompat.requestPermissions(NewPostActivity@ this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
							PERMISSIONS_REQUEST_STORAGE)
				}
			} else {
				startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE)
			}
		}

		fab?.setOnClickListener {
			if (mCurrentPhotoPath != null && text?.text?.isNotEmpty() as Boolean) {
				progress?.visibility = View.VISIBLE
				EGF2.uploadImage(mCurrentPhotoPath as String, mCurrentPhotoMime as String, Date().time.toString(), EGF2File.ENUM_KINDS.IMAGE.name.toLowerCase(), EGF2File::class.java)
						.flatMap {
							val post = EGF2Post()
							post.object_type = EGF2Post.OBJECT_TYPE
							post.image = it.id
							post.desc = text?.text.toString()
							EGF2.createObjectOnEdge(DataManager.user?.id as String, EGF2User.EDGE_POSTS, post.create(), EGF2Post::class.java)
						}
						.subscribe({
							progress?.visibility = View.GONE
							finish()
						}, {
							container?.snackbar(parseError(it.message.toString()))
							progress?.visibility = View.GONE
						})
			}
		}
	}

	private var mCurrentPhotoPath: String? = null
	private var mCurrentPhotoMime: String? = null

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			val selectedImage: Uri = data.data
			val filePathColumn = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE)
			val cursor: Cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null)
			cursor.moveToFirst()
			val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
			val columnIndexMime: Int = cursor.getColumnIndex(filePathColumn[1])
			mCurrentPhotoPath = cursor.getString(columnIndex)
			mCurrentPhotoMime = cursor.getString(columnIndexMime)
			cursor.close()

			Glide.with(NewPostActivity@ this).load(File(mCurrentPhotoPath))
					.fitCenter()
					.into(image)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		if (requestCode == PERMISSIONS_REQUEST_STORAGE) {
			if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				container?.snackbar("Storage Permission has been granted.")
				startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE)
			} else {
				container?.snackbar("Permissions were not granted.")
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		}
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.itemId == android.R.id.home) finish()
		return super.onOptionsItemSelected(item)
	}

	override fun onDestroy() {
		newPostLayout.unbind(this)
		super.onDestroy()
	}
}