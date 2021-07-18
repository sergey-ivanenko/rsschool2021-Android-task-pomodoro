package io.github.sergey_ivanenko.pomodoro

import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import io.github.sergey_ivanenko.pomodoro.databinding.PomodoroItemBinding

class PomodoroViewHolder(
    private val binding: PomodoroItemBinding,
    private val listener: PomodoroListener
) : RecyclerView.ViewHolder(binding.root) {

    private var timer: CountDownTimer? = null

    fun bind(pomodoroTimer: PomodoroTimer) {
        binding.pomodoroTimer.text = pomodoroTimer.currentMs.displayTime()

        if (pomodoroTimer.isStarted) {
            startTimer(pomodoroTimer)
        } else {
            stopTimer(pomodoroTimer)
        }

        initButtonsListeners(pomodoroTimer)
    }

    private fun initButtonsListeners(pomodoroTimer: PomodoroTimer) {
        binding.startTimer.setOnClickListener {
            if (pomodoroTimer.isStarted) {
                listener.stop(pomodoroTimer.id, pomodoroTimer.currentMs)
            } else {
                listener.start(pomodoroTimer.id)
            }
        }

        binding.deleteButton.setOnClickListener { listener.delete(pomodoroTimer.id) }
    }

    private fun startTimer(pomodoroTimer: PomodoroTimer) {
        timer?.cancel()
        timer = getCountDownTimer(pomodoroTimer)
        timer?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer(pomodoroTimer: PomodoroTimer) {
        timer?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(pomodoroTimer: PomodoroTimer): CountDownTimer {
        return object : CountDownTimer(PERIOD, UNIT_TEN_MS) {
            val interval = UNIT_TEN_MS

            override fun onTick(millisUntilFinished: Long) {
                pomodoroTimer.currentMs += interval
                binding.pomodoroTimer.text = pomodoroTimer.currentMs.displayTime()
            }

            override fun onFinish() {
                binding.pomodoroTimer.text = pomodoroTimer.currentMs.displayTime()
            }

        }
    }

    private fun Long.displayTime(): String {
        if (this <= 0L) {
            return START_TIME
        }

        val h = this / 1000 / 3600
        val m = this / 1000 % 3600 / 60
        val s = this / 1000 % 60

        return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }

    private companion object {
        private const val START_TIME = "00:00:00"
        private const val UNIT_TEN_MS = 10L
        private const val PERIOD = 1000L * 60L * 60L * 24L // Day
    }
}