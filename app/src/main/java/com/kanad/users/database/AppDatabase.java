package com.kanad.users.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.kanad.users.database.dao.UserDao;
import com.kanad.users.database.entity.User;


@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserAppDao();
}
