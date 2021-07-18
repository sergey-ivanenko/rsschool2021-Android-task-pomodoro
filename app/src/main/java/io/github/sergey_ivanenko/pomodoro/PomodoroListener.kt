package io.github.sergey_ivanenko.pomodoro

interface PomodoroListener {
    fun start(id: Int)
    fun stop(id: Int, currentMs: Long)
    fun delete(id: Int)
}