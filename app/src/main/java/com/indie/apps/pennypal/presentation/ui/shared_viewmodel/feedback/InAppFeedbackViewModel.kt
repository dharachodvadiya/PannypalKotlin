package com.indie.apps.pennypal.presentation.ui.shared_viewmodel.feedback

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.review.ReviewManager
import com.indie.apps.pennypal.repository.InAppFeedbackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class InAppFeedbackViewModel @Inject constructor(
    private val reviewManager: ReviewManager,
    private val inAppFeedbackRepository: InAppFeedbackRepository,
) : ViewModel() {

    private var reviewJob: Job? = null

    fun triggerReview(context: Context, isDelay: Boolean = false) {
        reviewJob?.cancel() // Cancel any existing job
        reviewJob = viewModelScope.launch {
            if (inAppFeedbackRepository.isShowDialog() && context is Activity) {
                if (isDelay) {
                    delay(3000) // Wait 3 seconds
                } else {
                    delay(1000)
                }

                launchInAppReview(context)

            }
        }
    }

    private suspend fun launchInAppReview(activity: Activity) {
        try {
            val reviewInfo = reviewManager.requestReviewFlow().await()
            reviewManager.launchReviewFlow(activity, reviewInfo).await()
        } catch (_: Exception) {

        }
    }
}

