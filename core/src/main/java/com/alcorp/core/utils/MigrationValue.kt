package com.alcorp.core.utils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigrationValue : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Membuat tabel baru "profile_new" dengan skema yang diharapkan
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS profile_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT, " +
                    "gender TEXT, " +
                    "date_birth TEXT, " +
                    "phone TEXT, " +
                    "address TEXT, " +
                    "photo TEXT)"
        )

        // Memindahkan nilai dari tabel lama ke tabel baru
        database.execSQL(
            "INSERT INTO profile_new (name, gender, date_birth, phone, address, photo) " +
                    "SELECT name, '', '', phone, address, photo FROM profile"
        )

        // Menghapus tabel lama
        database.execSQL("DROP TABLE IF EXISTS profile")

        // Mengubah nama tabel baru menjadi nama yang diinginkan
        database.execSQL("ALTER TABLE profile_new RENAME TO profile")
    }
}