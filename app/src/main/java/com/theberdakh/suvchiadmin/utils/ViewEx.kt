package com.theberdakh.suvchiadmin.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.app.App
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/* View Ex*/
fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.GONE
}


/*Edit Text*/
fun EditText.isNotEmptyOrBlank() = this.getString().isNotEmptyOrBlank()
fun EditText.getStringIsEmptyOrBlank() = this.getString().isEmptyOrBlank()
fun EditText.getString() = this.text.toString()
fun EditText.shakeIfEmptyOrBlank(): Boolean {
   val isValid = if (this.getString().isEmptyOrBlank()) {
       this.shake()
        false
    } else {
        true
    }
    return isValid
}

fun EditText.shakeIf(validator: (String) -> Boolean): Boolean {
    val text = this.getString()
    val isValid: Boolean = if (validator.invoke(text) && text.isNotEmptyOrBlank()) {
        this.shake()
        false
    } else {
        true
    }

    return isValid
}

fun String.isEmptyOrBlank() = this.isEmpty() || this.isBlank()
fun String.isNotEmptyOrBlank() = this.isNotEmpty() && this.isNotBlank()

fun View.shake() {
    /*investigate all view animations */
    val shakeAnimation = AnimationUtils.loadAnimation(App.instance, R.anim.shake_animation)
    this.startAnimation(shakeAnimation)
}


/* TextView Ex */
fun TextView.setDrawableLeft(@DrawableRes drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

fun TextView.setDrawableTop(@DrawableRes drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0)
}

fun TextView.setDrawableRight(@DrawableRes drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
}

fun TextView.setDrawableBottom(@DrawableRes drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawable)
}

/*Pre-load views*/
fun showToast(text: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(App.instance, text, duration).show()
}

fun View.showSnackbar(text: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, text, duration).show()
}


/* AppCompatDelegate */
fun setOnlyLightMode() {
    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}

fun MaterialToolbar.setUpBackNavigation(fragmentManager: FragmentManager) {
    this.setNavigationOnClickListener {
        fragmentManager.popBackStack()
    }
}

fun Toolbar.setUpBackNavigation(fragmentManager: FragmentManager) {
    this.setNavigationOnClickListener {
        fragmentManager.popBackStack()
    }
}


fun NestedScrollView.setUpExtendOnScroll(fab: ExtendedFloatingActionButton) {
    this.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        if (scrollY > oldScrollY + 12 && fab.isExtended) {
            fab.shrink()
        }
        if (scrollY < oldScrollY - 12 && !fab.isExtended) {
            fab.extend()
        }
        if (scrollY == 0) {
            fab.extend()
        }
    })
}

/*Uri*/
private fun Uri.convertImageUriToPng(context: Context): File? {
    val contentResolver = context.contentResolver
    try {
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(this))
        bitmap?.let {
            val file = File(context.cacheDir, "temp_image.png")
            if (file.exists()) {
                file.delete()
                this.convertImageUriToPng(context)
            } else {
                val outputStream = FileOutputStream(file)
                it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                return file
            }
        }

    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}




