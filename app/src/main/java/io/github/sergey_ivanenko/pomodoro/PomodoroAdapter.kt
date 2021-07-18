package io.github.sergey_ivanenko.pomodoro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import io.github.sergey_ivanenko.pomodoro.databinding.PomodoroItemBinding

class PomodoroAdapter(
    private val listener: PomodoroListener,
) : ListAdapter<PomodoroTimer, PomodoroViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PomodoroViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PomodoroItemBinding.inflate(layoutInflater, parent, false)

        return PomodoroViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PomodoroViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object {
        private val itemComparator = object : DiffUtil.ItemCallback<PomodoroTimer>() {

            override fun areItemsTheSame(oldItem: PomodoroTimer, newItem: PomodoroTimer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PomodoroTimer,
                newItem: PomodoroTimer,
            ): Boolean {
                return oldItem.currentMs == newItem.currentMs &&
                        oldItem.isStarted == newItem.isStarted
            }

            override fun getChangePayload(oldItem: PomodoroTimer, newItem: PomodoroTimer) = Any()
        }
    }
}