package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.firebase

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.installations.FirebaseInstallations

object FirebaseUtils {

    private const val TAG_FIREBASE = "firebase_tag"

    /**
     *  Syntax:
     *      try {}
     *      catch(ex: Exception){
     *          ex.recordException("MainActivity > OnCreate > getList")
     *      }
     */

    fun Throwable.recordException(logTag: String) {
        try {
            FirebaseCrashlytics.getInstance().log(logTag)
            FirebaseCrashlytics.getInstance().recordException(this)
            Log.e(logTag, "recordException: ${this.message.toString()}")
        } catch (e: Exception) {
            Log.e(logTag, "recordException: ${this.message.toString()}")
        }
    }



    fun getDeviceToken() {
        // Add this 'id' in firebase AB testing console as a testing device
        FirebaseInstallations.getInstance().getToken(false)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    Log.d(TAG_FIREBASE, "Installation auth token: " + task.result.token)
                } else {
                    Log.e(TAG_FIREBASE, "Unable to get Installation auth token")
                }
            }
    }
}