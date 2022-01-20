package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClick(position: Int) {
                //1. Remove Item from list
                listOfTasks.removeAt(position)
                //2. Notify Adapter something changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        //1. Let's detect when user clicks add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            //Code in here is going to be executed when the user clicks on a button
//            Log.i("Bryan", "User clicked on button")
//        }

        loadItems()

        //Look up Recycler View on layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Set up button and input field, so the user can enter a task

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        findViewById<Button>(R.id.button).setOnClickListener{
            //1. Grab text that user had inputted into @id/addTaskfield
            val userInputtedTask = inputTextField.text.toString()

            //2. Add the strong into list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            // Notify Data Adapter
            adapter.notifyItemChanged(listOfTasks.size - 1)

            //3. Reset Text Field
            inputTextField.setText("")

            saveItems()
        }

    }

    //Save the data that the user has inputted
    //Save data by writing and reading from a file

    //Created a method to get the files we need
    fun getDataFile() : File {

        //Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data,txt")
    }
    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks =
                org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    //Save items by writing them into our data
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}