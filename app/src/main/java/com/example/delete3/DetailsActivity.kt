package com.example.delete3

import DatabaseHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var dishList: ArrayList<Dish>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        dbHelper = DatabaseHelper(this)
        dishList = ArrayList()

        val intent = intent
        val dishName = intent.getStringExtra("dishName")
        val description = intent.getStringExtra("description")

        val textViewTitle: TextView = findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = findViewById(R.id.textViewDescription)

        textViewTitle.text = dishName
        textViewDescription.text = description

        // Отобразите другие данные по необходимости

        // Вызов метода загрузки блюд
        loadDishes()
    }

    @SuppressLint("Range")
    private fun loadDishes() {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM Dishes", null)

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val dishId = cursor.getLong(cursor.getColumnIndex("dish_id"))
                val dishName = cursor.getString(cursor.getColumnIndex("dish_name"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                val cookingTime = cursor.getInt(cursor.getColumnIndex("cookingTime"))
                val imageData = cursor.getBlob(cursor.getColumnIndex("image_data"))

                val dish = Dish(dishId.toInt(), dishName, description, cookingTime, imageData)
                dishList.add(dish)

                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()
    }
}
