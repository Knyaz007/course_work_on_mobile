package com.example.delete3

import DatabaseHelper
import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class MainActivity2 : AppCompatActivity() {
    // Объявим переменные компонентов
    var button: Button? = null
    var textView: TextView? = null
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var mDb: SQLiteDatabase

    // Переменная для работы с БД

//    fun onAddRecipeClick(view: View) {
//        // Обновим базу данных
//        mDBHelper.updateDataBase()
//        // После обновления откроем базу данных
//        mDb = mDBHelper.writableDatabase
//        // Дополнительный код для добавления рецепта, если необходимо
//        updateList()
//    }
    fun onAddRecipeClick(view: View) {
        // Не обновляем базу данных здесь
        // mDBHelper.updateDataBase()

        // Открываем базу данных
        mDb = mDBHelper.writableDatabase

        // Дополнительный код для добавления рецепта, если необходимо
        insertRecipe("Spaghetti Bolognese", "Pasta, Tomatoes, Meat", "John Doe", "Italian", 30)

        // Обновляем список после добавления рецепта
        updateList()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDBHelper = DatabaseHelper(this)

        // Получим базу данных
        mDb = mDBHelper.writableDatabase

        // Найдем компоненты в XML разметке
        button = findViewById(R.id.button)
        textView = findViewById(R.id.textView)

        // Пропишем обработчик клика кнопки
        button?.setOnClickListener(View.OnClickListener { // Пример добавления нового рецепта
            insertRecipe("Spaghetti Bolognese", "Pasta, Tomatoes, Meat", "John Doe", "Italian", 30)
            updateList()
        })
        updateList()
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        mDBHelper = DatabaseHelper(this)
//        try {
//            mDBHelper?.updateDataBase()
//        } catch (mIOException: IOException) {
//            throw Error("UnableToUpdateDatabase")
//        }


//        // Найдем компоненты в XML разметке
//        button = findViewById(R.id.button)
//        textView = findViewById(R.id.textView)
//
//        // Пропишем обработчик клика кнопки
//        button?.setOnClickListener(View.OnClickListener { // Пример добавления нового рецепта
//            insertRecipe("Spaghetti Bolognese", "Pasta, Tomatoes, Meat", "John Doe", "Italian", 30)
//            updateList()
//        })
//        updateList()
//    }

    // Метод для вставки нового рецепта
    private fun insertRecipe(
        title: String,
        ingredients: String,
        author: String,
        category: String,
        cookingTime: Int
    ) {
        val values = ContentValues()
        values.put("title", title)
        values.put("ingredients", ingredients)
        values.put("author", author)
        values.put("category", category)
        values.put("cookingTime", cookingTime)
        val newRowId = mDb?.insert("recipes", null, values)
    }

    // Метод для обновления списка


    @SuppressLint("Range")
    private fun updateList() {

        // Список рецептов
        val recipes = ArrayList<HashMap<String, Any?>>()

        // Список параметров конкретного рецепта
        var recipe: HashMap<String, Any?>

        // Отправляем запрос в БД
        val cursor = mDb?.rawQuery("SELECT * FROM recipes", null)

        if (cursor == null) {
            Log.e("Database", "Cursor is null")
        } else {
            if (cursor.moveToFirst()) {
                // есть результаты запроса
                do {
                    // обрабатываем данные текущей строки
                    val title = cursor.getString(cursor.getColumnIndex("title"))
                    val ingredients = cursor.getString(cursor.getColumnIndex("ingredients"))
                    val author = cursor.getString(cursor.getColumnIndex("author"))
                    val category = cursor.getString(cursor.getColumnIndex("category"))
                    val cookingTime = cursor.getInt(cursor.getColumnIndex("cookingTime"))

                    // Выводим данные в лог
                    Log.d(
                        "Database",
                        "Title: $title, Ingredients: $ingredients, Author: $author, Category: $category, Cooking Time: $cookingTime"
                    )

                    // Заполняем рецепт
                    recipe = HashMap()
                    recipe["title"] = title
                    recipe["ingredients"] = ingredients
                    recipe["author"] = author
                    recipe["category"] = category
                    recipe["cookingTime"] = cookingTime

                    // Закидываем рецепт в список рецептов
                    recipes.add(recipe)
                } while (cursor.moveToNext())
            } else {
                Log.e("Database", "No rows found")
            }
            cursor.close()
        }

        // Какие параметры рецепта мы будем отображать в соответствующих
        // элементах из разметки adapter_item.xml
        val from = arrayOf("title", "ingredients", "author", "category", "cookingTime")
        val to = intArrayOf(
            R.id.textView,
            R.id.textView2,
            R.id.textView3,
            R.id.textView4,
            R.id.textView5
        )

        // Создаем адаптер
        val adapter = SimpleAdapter(this, recipes, R.layout.adapter_item, from, to)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter
        Log.d("Database", "Number of recipes: ${recipes.size}")
    }


}
