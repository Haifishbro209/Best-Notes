package com.kaloyan.junustutorial

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kaloyan.junustutorial.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var lvTodoList: ListView
    private lateinit var button: FloatingActionButton
    private lateinit var notizen: ArrayList<String>
    private lateinit var itemAdapter: ArrayAdapter<String>
    private val funnyHints: ArrayList<String> = arrayListOf(
        "Brain dead? Save it.",
        "Dump your crap here.",
        "Oh look, another useless note.",
        "Memory failure? Type.",
        "Future you won’t read this.",
        "More bullshit? Fine.",
        "Typing won’t fix stupid.",
        "Save. Forget. Repeat.",
        "Another stroke of genius? Lol.",
        "Oh wow, another groundbreaking thought? Fucking type it.",
        "Just write it, genius. Maybe one day you’ll actually use it.",
        "Dump your half-baked ideas here. No one cares.",
        "This space is as empty as your attention span.",
        "Go on, write it. Pretend you’ll remember to check later.",
        "Another note? Sure. Like you don’t already ignore 90% of them.",
        "Brain malfunction? Save your last two neurons here.",
        "Congrats, you’ve reached the ‘shit I’ll forget’ section.",
        "Go ahead, waste more storage with nonsense.")


    private val sharedPreferences by lazy {
        getSharedPreferences("NotesApp", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lvTodoList = findViewById(R.id.lvTodoList)
        button = findViewById(R.id.AddButton)
        notizen = loadNotes()
        itemAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, notizen)
        lvTodoList.adapter = itemAdapter

        lvTodoList.setOnItemLongClickListener { _, _, pos, _ ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Proceed Deletion")
            builder.setMessage("This Idea will be deleted irreversibly")
            builder.setPositiveButton("Delete") { _, _ ->
                notizen.removeAt(pos)
                itemAdapter.notifyDataSetChanged()
                Toast.makeText(applicationContext, "It seems like the idea wasn't that brilliant.", Toast.LENGTH_SHORT).show()
                saveNotes()
            }
            builder.setNegativeButton("Keep Idea") { _, _ -> }
            builder.show()
            true
        }



        button.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add Note")
            builder.setMessage(funnyHints.random())
            val input = EditText(this)
            input.hint = "Add Idea"
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton("OK") { _, _ ->
                if (input.text.toString().isEmpty()){
                    Toast.makeText(applicationContext,"A blank note, how original.",Toast.LENGTH_SHORT).show()
                }else{
                    notizen.add(input.text.toString())
                    itemAdapter.notifyDataSetChanged()
                    saveNotes()
                }

            }
            builder.setNegativeButton("Return"){ _, _ ->
                Toast.makeText(applicationContext, "It seems like the idea wasn't that brilliant.",Toast.LENGTH_SHORT).show()
            }

            builder.show()
        }

    }
    private fun saveNotes() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("notes", notizen.toSet())
        editor.apply()
    }

    private fun loadNotes(): ArrayList<String> {
        val notesSet = sharedPreferences.getStringSet("notes", setOf())
        return ArrayList(notesSet)
    }
}
