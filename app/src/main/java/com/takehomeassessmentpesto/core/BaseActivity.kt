package com.takehomeassessmentpesto.core

import android.content.Context
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.takehomeassessmentpesto.utils.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    private var customProgressDialog: CustomProgressDialog? = null

    val deviceId: String by lazy { Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID) }

    /**
     * show progress dialog
     * @param context context of activity
     */
    protected fun showProgress(context: Context) {
        try {
            if (customProgressDialog != null && customProgressDialog!!.isShowing) {
                return
            }
            customProgressDialog = CustomProgressDialog(context)
            customProgressDialog?.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * hide progress dialog
     */
    protected fun hideProgress() {
        try {
            if (customProgressDialog != null && customProgressDialog!!.isShowing) {
                customProgressDialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * redirect to login
     */
    protected fun redirectToLogin(context: Context) {
        try {
            /*FirebaseAuth.getInstance().signOut()
//            pref.clearAllPref(context, Constants.PREF_FILE)// do not clear scm token will clear and it will set "" in after login
            NotificationManagerCompat.from(context).cancelAll() // clear all notification on logout
            startActivity(Intent(context, LoginActivity::class.java))
            finishAffinity()*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}