package co.cartrack.za.utils

import android.view.View
import android.widget.ProgressBar

class Constants {
    companion object{
        const val BASE_URL = "http://www.omdbapi.com/"
        const val DEBUG = "debug"

        fun hideProgressBar(progressBar: ProgressBar ){
            progressBar.visibility = View.INVISIBLE
        }

        fun showProgressBar(progressBar: ProgressBar) {
            progressBar.visibility = View.VISIBLE
        }
    }
}