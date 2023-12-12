import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    // Declare the variable for the database
    private var mDataBase: SQLiteDatabase? = null

    init {

        copyDataBase()
        this.readableDatabase
    }

    companion object {
        private const val DB_NAME = "recipes.db"
        private const val DB_VERSION = 1
        private lateinit var mContext: Context
        private var DB_PATH = ""


        // Initialize mContext using the constructor
        fun initialize(context: Context) {
            mContext = context

            // Initialize DB_PATH here if needed
            if (Build.VERSION.SDK_INT >= 17)
                DB_PATH = mContext.applicationInfo.dataDir + "/databases/"
            else
                DB_PATH = "/data/data/" + mContext.packageName + "/databases/"
        }



    }



    @Throws(IOException::class)
    fun updateDataBase() {
        if (mNeedUpdate) {
            val dbFile = File(DB_PATH + DB_NAME)
            if (dbFile.exists())
                dbFile.delete()

            copyDataBase()

            mNeedUpdate = false
        }
    }

    private fun checkDataBase(): Boolean {
        val dbFile = File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    private fun copyDataBase() {
        try {
            copyDBFile()
        } catch (mIOException: IOException) {
            throw Error("Error copying database")
        }
    }

    @Throws(IOException::class)
    private fun copyDBFile() {
        try {
            val mInput: InputStream = mContext.assets.open(DB_NAME)
            val outputPath = DB_PATH + DB_NAME

            // Ensure the parent directory exists
            val dir = File(DB_PATH)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val mOutput: OutputStream = FileOutputStream(outputPath)

            val mBuffer = ByteArray(1024)
            var mLength: Int
            while (mInput.read(mBuffer).also { mLength = it } > 0) {
                mOutput.write(mBuffer, 0, mLength)
            }

            mOutput.flush()
            mOutput.close()
            mInput.close()
            Log.d("DatabaseHelper", "Database copied successfully to: $outputPath")
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error copying database", e)
            throw e
        }
    }


    @Throws(SQLException::class)
    fun openDataBase(): SQLiteDatabase {
        val mPath: String = DB_PATH + DB_NAME
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY)
        return mDataBase!!
    }

    @Synchronized
    override fun close() {
        if (mDataBase != null)
            mDataBase!!.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
       // db.execSQL("CREATE TABLE IF NOT EXISTS recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, ingredients TEXT, author TEXT, category TEXT, cookingTime INTEGER);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion)
            mNeedUpdate = true
    }

    private var mNeedUpdate = false
}
