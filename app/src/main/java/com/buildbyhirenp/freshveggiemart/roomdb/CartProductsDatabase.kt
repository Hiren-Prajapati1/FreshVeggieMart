package com.buildbyhirenp.freshveggiemart.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.buildbyhirenp.freshveggiemart.utility.StringListTypeConverter

@Database(entities = [CartProducts::class, FavouriteProducts::class], version = 2, exportSchema = false)
@TypeConverters(StringListTypeConverter::class)  // Ensure TypeConverters are applied
abstract class CartProductsDatabase : RoomDatabase() {

    abstract fun cartsProductDao(): CartProductsDao

    companion object {
        @Volatile
        private var INSTANT: CartProductsDatabase? = null

        // Migration from version 1 to version 2, adding the FavouriteProducts table
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `FavouriteProducts` (
                        `productID` TEXT NOT NULL PRIMARY KEY,
                        `productTitle` TEXT,
                        `productQuantity` TEXT,
                        `productUnit` TEXT,
                        `productPrice` TEXT,
                        `productStock` INTEGER,
                        `productCategory` TEXT,
                        `productType` TEXT,
                        `productDiscription` TEXT,
                        `adminUid` TEXT,
                        `productImageUris` TEXT
                    )
                """.trimIndent())
            }
        }

        fun getDatabaseInstant(context: Context): CartProductsDatabase {
            val tempInstant = INSTANT
            if (tempInstant != null) return tempInstant

            synchronized(this) {
                val roomDB = Room.databaseBuilder(
                    context.applicationContext,
                    CartProductsDatabase::class.java,
                    "CartProducts"
                )
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANT = roomDB
                return roomDB
            }
        }
    }
}
