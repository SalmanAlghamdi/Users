package com.kanad.users.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kanad.users.database.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    List<User> getAllUserData();

    @Query("SELECT * FROM User WHERE userId =:id")
    User getUserData(String id);

    @Delete
    int deleteUserDetails(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertUserDetail(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertAllUser(List<User> users);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM User WHERE userId =:id LIMIT 1")
    public User findUser(int id);

    @Query("SELECT COUNT(*) from User")
    int countUsersData();

    @Insert
    void insertAll(User... users);
}