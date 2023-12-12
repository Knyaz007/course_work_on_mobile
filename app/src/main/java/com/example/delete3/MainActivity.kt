package com.example.delete3

import DatabaseHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var dishList: ArrayList<Dish>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DatabaseHelper.initialize(applicationContext)

            // val dbHelper = DatabaseHelper(applicationContext)
        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.listView)
        dishList = ArrayList()

        loadDishes()
        Log.d("DishAdapter", "Number of dishes loaded: ${dishList.size}")

        val adapter = DishAdapter(this, R.layout.dish_item, dishList)
        listView.adapter = adapter


        listView.setOnItemClickListener { parent, view, position, id ->
            // Получите данные о выбранном элементе
            val selectedDish = dishList[position]

            // Создайте Intent для открытия новой активности
            val intent = Intent(this, DetailsActivity::class.java)

            // Передайте данные о выбранном блюде в новую активность
            intent.putExtra("dishName", selectedDish.dishName)
            intent.putExtra("description", selectedDish.description)
            intent.putExtra("dishid", (selectedDish.dishId).toString())
            intent.putExtra("cookingTime", selectedDish.cookingTime)

            // Добавьте другие данные по необходимости

            // Запустите новую активность
            startActivity(intent)
        }
    }

    @SuppressLint("Range")
    private fun loadDishes() {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM Dishes3", null)

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val dishId = cursor.getLong(cursor.getColumnIndex("dish_id"))
                val dishName = cursor.getString(cursor.getColumnIndex("dish_name"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                val cookingTime = cursor.getInt(cursor.getColumnIndex("cookingTime"))
                val imageData = cursor.getBlob(cursor.getColumnIndex("image_data"))
                val step_description = cursor.getString(cursor.getColumnIndex("step_description"))
                val ingredients = cursor.getString(cursor.getColumnIndex("Ingredients"))

                val dish = Dish(dishId.toInt(), dishName, description, cookingTime, step_description,ingredients, imageData)
                dishList.add(dish)

                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()
    }
}
