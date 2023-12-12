package com.example.delete3

import DatabaseHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class DetailsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var dishList: ArrayList<Dish>
    private var dishId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        dbHelper = DatabaseHelper(this)
        dishList = ArrayList()

        val intent = intent
        val dishName = intent.getStringExtra("dishName")
        val description = intent.getStringExtra("description")
        val dishId2 = intent.getStringExtra("dishid")
        if (dishId2 != null) {
            dishId = dishId2.toInt()
        }

        val textViewTitle: TextView = findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = findViewById(R.id.textViewDescription)
        val textViewCookingTime: TextView = findViewById(R.id.textViewCookingTime)
        val textViewStepDescription: TextView = findViewById(R.id.textViewStepDescription)
        val imageViewDish: ImageView = findViewById(R.id.imageViewDish)

        textViewTitle.text = dishName
        textViewDescription.text = description

        // Вызов метода загрузки блюда
        loadDish()

        // Отобразите другие данные по необходимости
    }

    @SuppressLint("Range")
    private fun loadDish() {
        val db = dbHelper.readableDatabase
        val id = arrayOf(dishId.toString())
        val cursor: Cursor = db.rawQuery("SELECT * FROM Dishes2 WHERE dish_id = ?", id)

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val dishId = cursor.getLong(cursor.getColumnIndex("dish_id"))
                val dishName = cursor.getString(cursor.getColumnIndex("dish_name"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                val step_description = cursor.getString(cursor.getColumnIndex("step_description"))
                val cookingTime = cursor.getInt(cursor.getColumnIndex("cookingTime"))
                val imageData = cursor.getBlob(cursor.getColumnIndex("image_data"))

                val dish = Dish(dishId.toInt(), dishName, description, cookingTime, step_description, imageData)
                dishList.add(dish)

                // Отобразите данные в представлениях
                val textViewCookingTime: TextView = findViewById(R.id.textViewCookingTime)
                val textViewStepDescription: TextView = findViewById(R.id.textViewStepDescription)
                val imageViewDish: ImageView = findViewById(R.id.imageViewDish)

                textViewCookingTime.text = "Время готовки: $cookingTime минут"
                textViewStepDescription.text = step_description

                // Отобразите изображение, если оно есть
                if (imageData != null && imageData.isNotEmpty()) {
                    val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                    imageViewDish.setImageBitmap(bitmap)
                }

                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()
    }
}
