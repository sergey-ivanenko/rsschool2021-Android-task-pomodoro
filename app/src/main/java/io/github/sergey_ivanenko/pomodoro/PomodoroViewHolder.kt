package io.github.sergey_ivanenko.pomodoro

import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import android.util.Log
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import io.github.sergey_ivanenko.pomodoro.databinding.PomodoroItemBinding
import kotlinx.coroutines.*

class PomodoroViewHolder(
    private val binding: PomodoroItemBinding,
    private val listener: PomodoroListener
) : RecyclerView.ViewHolder(binding.root) {

    private var timer: CountDownTimer? = null
    //private var current = 0L
    private var progressBarTime = 0L

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
                //binding.startTimer.text = "start"
                //binding.startTimer.text = itemView.resources.getString(R.string.start)
            } else {
                listener.start(pomodoroTimer.id)
                //binding.startTimer.text = "stop"
                //binding.startTimer.text = itemView.resources.getString(R.string.stop)
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

        job = CoroutineScope(Dispatchers.Default).launch {
            while (pomodoroTimer.currentMs >= 0) {
                //progressBarTime += INTERVAL
                //pomodoroTimer.currentMs -= INTERVAL
                binding.circleProgressBar.setCurrent(pomodoroTimer.startMs - pomodoroTimer.currentMs)
                delay(INTERVAL)
            }
        }

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
            //val interval = UNIT_TEN_MS
            //val interval = UNIT_ONE_SECOND

            override fun onTick(millisUntilFinished: Long) {
                //pomodoroTimer.currentMs += interval
                pomodoroTimer.currentMs = millisUntilFinished
                binding.pomodoroTimer.text = pomodoroTimer.currentMs.displayTime()
                //binding.circleProgressBar.setCurrent(pomodoroTimer.startMs - pomodoroTimer.currentMs)
            }

            override fun onFinish() {
                binding.pomodoroTimer.text = pomodoroTimer.startMs.displayTime()
                stopTimer(pomodoroTimer)
                binding.startTimer.isClickable = false
                binding.pomodoroItem.setCardBackgroundColor(itemView.context.getColor(R.color.dark_red_color))
                binding.circleProgressBar.isInvisible = true
                binding.circleProgressBar.setCurrent(0)

                /*binding.blinkingIndicator.isInvisible = true
                (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()*/
            }

        }
    }

    private fun Long.displayTime(): String {
        /*if (this <= 0L) {
            return START_TIME
        }*/

        val h = this / UNIT_ONE_SECOND / 3600
        val m = (this / UNIT_ONE_SECOND % 3600) / 60
        val s = (this / UNIT_ONE_SECOND) % 60
        Log.i("TICK", s.toString())

        return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10 > 0) {
            "$count"
        } else {
            "0$count"
        }
    }

    private companion object {
        //private const val START_TIME = "00:00:00"
        /*private const val UNIT_TEN_MS = 10L*/
        private const val UNIT_ONE_SECOND = 1000L
        private const val INTERVAL = /*1000L*/500L
        //private const val PERIOD = 1000L * 60L * 60L * 24L // Day
    }
}