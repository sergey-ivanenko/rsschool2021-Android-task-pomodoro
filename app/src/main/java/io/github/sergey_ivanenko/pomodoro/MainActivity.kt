package io.github.sergey_ivanenko.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            pomodoroTimers.add(PomodoroTimer(nextId++, 0, false))
            pomodoroAdapter.submitList(pomodoroTimers.toList())
        }
    }

    override fun start(id: Int) {
        TODO("Not yet implemented")
    }

    override fun stop(id: Int, currentMs: Long) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }


}