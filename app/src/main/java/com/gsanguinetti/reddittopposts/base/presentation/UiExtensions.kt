package com.gsanguinetti.reddittopposts.base.presentation

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showError(errorResId: Int) {
    view?.let {
        Snackbar.make(it, errorResId, Snackbar.LENGTH_LONG)
            .show()
    }
}