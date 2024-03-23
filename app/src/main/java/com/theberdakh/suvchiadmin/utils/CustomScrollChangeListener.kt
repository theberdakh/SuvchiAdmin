package com.theberdakh.suvchiadmin.utils

import android.util.Log
import androidx.recyclerview.widget.RecyclerView

object CustomScrollChangeListener : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        when (newState) {
            RecyclerView.SCROLL_STATE_IDLE -> Log.d("Recycler", "idle")
            RecyclerView.SCROLL_STATE_DRAGGING -> Log.d("Recycler", "dragging")
            RecyclerView.SCROLL_STATE_SETTLING -> Log.d("Recycler", "settling")
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy > 0) {
            showToast("Scrolled Downwards");
        } else if (dy < 0) {
            showToast("Scrolled Upwards");
        } else {
            showToast("No Vertical Scrolled");
        }
        super.onScrolled(recyclerView, dx, dy)
    }
}