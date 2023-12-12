import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DatabaseHelper2(private val mContext: Context) :
    SQLiteOpenHelper(mContext, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "recipes.db"
        private var DB_PATH = ""

        private const val DB_VERSION = 1
    }

    // Объявление переменной базы данных
    private var mDataBase: SQLiteDatabase? = null

    init {
        if (Build.VERSION.SDK_INT >= 17)
            DB_PATH = mContext.applicationInfo.dataDir + "/databases/"
        //DB_PATH =  " D:\\"
        else
            DB_PATH = "/data/data/" + mContext.packageName + "/databases/"

        copyDataBase()

        this.readableDatabase
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
        // Всегда закрываем текущую базу данных перед копированием
        this.close()

        try {
            copyDBFile()
        } catch (mIOException: IOException) {
            throw Error("ErrorCopyingDataBase")
        }
    }

    @Throws(IOException::class)
    private fun copyDBFile() {
        val mInput: InputStream = mContext.assets.open(DB_NAME)
        val mOutput: OutputStream = FileOutputStream(DB_PATH + DB_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        while (mInput.read(mBuffer).also { mLength = it } > 0)
            mOutput.write(mBuffer, 0, mLength)
        mOutput.flush()
        mOutput.close()
        mInput.close()
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

    //    когда база данных уже существует. В текущей реализации,
//    если база данных уже присутствует, происходит только чтение базы данных.
//
    override fun onCreate(db: SQLiteDatabase) {
        if (!tableExists(db, "recipes")) {
            // Таблицы нет, создаем её
            db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT NOT NULL,ingredients TEXT,author TEXT,category TEXT,cookingTime INTEGER);")
        }
    }

    private fun tableExists(db: SQLiteDatabase, tableName: String): Boolean {
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", arrayOf(tableName))
        val tableExists = cursor.moveToFirst()
        cursor.close()
        return tableExists
    }

    //onCreate(db: SQLiteDatabase) {}
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion)
            mNeedUpdate = true
    }

    private var mNeedUpdate = false
}
