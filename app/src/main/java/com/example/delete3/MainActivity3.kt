package com.example.delete3

import DatabaseHelper
import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity3 : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var dishList: ArrayList<Dish>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.listView)
        dishList = ArrayList()

        loadDishes()

        val adapter = DishAdapter(this, R.layout.dish_item, dishList)
        listView.adapter = adapter
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
