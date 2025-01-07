package com.example.pr24_var3_mironov_ilya

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar


class BlankFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private lateinit var addButton : Button
    private lateinit var view : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_task_list, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        addButton = view.findViewById(R.id.buttonAdd)


        adapter = TaskAdapter(tasks, { task -> showEditDialog(task) }, { task -> deleteTask(task) })
        recyclerView.adapter = adapter

        addButton.setOnClickListener()
        {
            val sharedPreferences = requireContext().getSharedPreferences("tasks", Context.MODE_PRIVATE)
            var counterTasks = sharedPreferences.getInt("counterTasks", 1)
            counterTasks += 1
            sharedPreferences.edit().putInt("counterTasks", counterTasks).apply()
            loadTasks()

            val snackbar = Snackbar.make(view, "Задача добавлена", Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        loadTasks()

        return view
    }

    private fun deleteTask(task: Task)
    {
        AlertDialog.Builder(requireContext())
            .setTitle("Точно удаляем?")
            .setPositiveButton("Удаляем") { _, _ ->
                val sharedPreferences = requireContext().getSharedPreferences("tasks", Context.MODE_PRIVATE)
                sharedPreferences.edit().remove("task_${task.id}_title").apply()
                sharedPreferences.edit().remove("task_${task.id}_description").apply()

                var counterTasks = sharedPreferences.getInt("counterTasks", 1)

                for (i in task.id..counterTasks) {
                    val title = sharedPreferences.getString("task_${i + 1}_title", "Новая задача")
                    val description = sharedPreferences.getString("task_${i + 1}_description", "Типо описание")
                    sharedPreferences.edit().putString("task_${i}_title", title).apply()
                    sharedPreferences.edit().putString("task_${i}_description", description).apply()
                }

                counterTasks -= 1
                sharedPreferences.edit().putInt("counterTasks", counterTasks).apply()
                loadTasks()
                Snackbar.make(view, "Mission completed", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("Отбой", null)
            .show()
    }

    private fun loadTasks() {
        tasks.clear()
        val sharedPreferences = requireContext().getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val counterTasks = sharedPreferences.getInt("counterTasks", 1)
        for (i in 1..counterTasks) {
            val title = sharedPreferences.getString("task_${i}_title", "Новая задача")
            val description = sharedPreferences.getString("task_${i}_description", "Типо описание")
            if (title != null && description != null) {
                tasks.add(Task(i, title, description))
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun showEditDialog(task: Task) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_task, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.edit_task_title)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.edit_task_description)

        titleEditText.setText(task.title)
        descriptionEditText.setText(task.description)

        AlertDialog.Builder(requireContext())
            .setTitle("Редачим")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newTitle = titleEditText.text.toString()
                val newDescription = descriptionEditText.text.toString()
                updateTask(task, newTitle, newDescription)
            }
            .setNegativeButton("Отбой", null)
            .show()
    }

    private fun updateTask(task: Task, newTitle: String, newDescription: String) {
        task.title = newTitle
        task.description = newDescription

        val sharedPreferences = requireContext().getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("task_${task.id}_title", task.title)
        editor.putString("task_${task.id}_description", task.description)
        editor.apply()

        val position = tasks.indexOf(task)
        adapter.notifyItemChanged(position)
    }
}