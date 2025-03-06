package com.indie.apps.pennypal.util

import android.app.Activity
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager

fun launchInAppReview(
    activity: Activity,
    reviewManager: ReviewManager,
    onSuccess: () -> Unit = {},
    onFailure: (Exception?) -> Unit = {}
) {

    val request = reviewManager.requestReviewFlow()
    request.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val reviewInfo: ReviewInfo = task.result
            val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
            flow.addOnCompleteListener {
                onSuccess()
            }
        } else {
            onFailure(task.exception)
        }
    }
}