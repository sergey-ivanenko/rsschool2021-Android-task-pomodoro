package io.github.sergey_ivanenko.pomodoro

data class PomodoroTimer(
    val id: Int,
    val startMs: Long,
    var currentMs: Long,
    var isStarted: Boolean
)