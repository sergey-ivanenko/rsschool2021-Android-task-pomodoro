package io.github.sergey_ivanenko.pomodoro

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PomodoroTimer(
    val id: Int,
    val startMs: Long,
    var currentMs: Long,
    var isStarted: Boolean
): Parcelable