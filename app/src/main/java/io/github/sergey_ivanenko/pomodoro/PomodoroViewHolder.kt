package io.github.sergey_ivanenko.pomodoro

import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import androidx.core.view.isInvisible
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.RecyclerView
import io.github.sergey_ivanenko.pomodoro.databinding.PomodoroItemBinding
import kotlinx.coroutines.*

class PomodoroViewHolder(
    private val binding: PomodoroItemBinding,
    private val listener: PomodoroListener
) : RecyclerView.ViewHolder(binding.root), LifecycleObserver {

    private var timer: CountDownTimer? = null
    private var job: Job? = null

    fun bind(pomodoroTimer: PomodoroTimer) {
        binding.pomodoroTimer.text = pomodoroTimer.currentMs.displayTime()
        binding.circleProgressBar.setPeriod(pomodoroTimer.startMs)
        binding.pomodoroItem.setCardBackgroundColor(itemView.context.getColor(R.color.white))

        if (pomodoroTimer.isStarted) {
            startTimer(pomodoroTimer)
            binding.startTimer.text = itemView.resources.getString(R.string.stop)
        } else {
            stopTimer(pomodoroTimer)
            binding.startTimer.text = itemView.resources.getString(R.string.start)
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

        binding.deleteButton.setOnClickListener {
            stopTimer(pomodoroTimer)
            binding.pomodoroItem.setCardBackgroundColor(itemView.context.getColor(R.color.white))
            listener.delete(pomodoroTimer.id)
        }
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
        job?.cancel()
        pomodoroTimer.isStarted = false
        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(pomodoroTimer: PomodoroTimer): CountDownTimer {
        return object : CountDownTimer(pomodoroTimer.currentMs, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                pomodoroTimer.currentMs = millisUntilFinished
                binding.pomodoroTimer.text = pomodoroTimer.currentMs.displayTime()
                binding.circleProgressBar.setCurrent(pomodoroTimer.startMs - pomodoroTimer.currentMs)
            }

            override fun onFinish() {
                binding.pomodoroTimer.text = pomodoroTimer.startMs.displayTime()
                stopTimer(pomodoroTimer)
                binding.startTimer.isClickable = false
                binding.pomodoroItem.setCardBackgroundColor(itemView.context.getColor(R.color.dark_red_color))
                binding.circleProgressBar.isInvisible = true
                binding.circleProgressBar.setCurrent(0)
            }
        }
    }

    private companion object {
        private const val INTERVAL = 500L
    }
}