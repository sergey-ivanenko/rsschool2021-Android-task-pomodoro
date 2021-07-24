package io.github.sergey_ivanenko.pomodoro

const val INVALID = "INVALID"
const val COMMAND_START = "COMMAND_START"
const val COMMAND_STOP = "COMMAND_STOP"
const val COMMAND_ID = "COMMAND_ID"
const val STARTED_TIMER_TIME_MS = "STARTED_TIMER_TIME"
const val UNIT_ONE_SECOND = 1000L

fun Long.displayTime(): String {

    val h = this / UNIT_ONE_SECOND / 3600
    val m = this / UNIT_ONE_SECOND % 3600 / 60
    val s = this / UNIT_ONE_SECOND % 60

    return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"
}

private fun displaySlot(count: Long): String {
    return if (count / 10L > 0) {
        "$count"
    } else {
        "0$count"
    }
}
