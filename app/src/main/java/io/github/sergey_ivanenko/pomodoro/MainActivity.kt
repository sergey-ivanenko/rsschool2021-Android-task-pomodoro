package io.github.sergey_ivanenko.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.sergey_ivanenko.pomodoro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), PomodoroListener {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)

    private val pomodoroTimers = mutableListOf<PomodoroTimer>()
    private val pomodoroAdapter = PomodoroAdapter(this)
    private var nextId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pomodoroAdapter
        }

        binding.addTimerButton.setOnClickListener {
            val inputTime = binding.setTime.text.toString()
            if (inputTime.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val millisInput = inputTime.toLong() * 60000
            if (millisInput == 0L) {
                Toast.makeText(this, getString(R.string.toast_equals_zero), Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            pomodoroTimers.add(PomodoroTimer(nextId++, startMs = millisInput, currentMs = millisInput, false))
            pomodoroAdapter.submitList(pomodoroTimers.toList())
        }
    }

    override fun start(id: Int) {
        val stopPomodoroTimers = mutableListOf<PomodoroTimer>()

        pomodoroTimers.forEach {
            stopPomodoroTimers.add(PomodoroTimer(it.id, it.startMs, it.currentMs, false))
        }
        pomodoroAdapter.submitList(stopPomodoroTimers)
        pomodoroTimers.clear()
        pomodoroTimers.addAll(stopPomodoroTimers)

        changeTimer(id, null, true)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeTimer(id, currentMs, false)
    }

    override fun delete(id: Int) {
        pomodoroTimers.remove(pomodoroTimers.find { it.id == id })
        pomodoroAdapter.submitList(pomodoroTimers.toList())
    }

    private fun changeTimer(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<PomodoroTimer>()
        pomodoroTimers.forEach {
            if (it.id == id) {
                newTimers.add(PomodoroTimer(it.id, it.startMs, currentMs ?: it.currentMs, isStarted))
            } else {
                newTimers.add(it)
            }
        }
        pomodoroAdapter.submitList(newTimers)
        pomodoroTimers.clear()
        pomodoroTimers.addAll(newTimers)
    }
}