package io.github.sergey_ivanenko.pomodoro

data class PomodoroTimer(
    val id: Int,
    var currentMs: Long,
    val isStarted: Boolean
)