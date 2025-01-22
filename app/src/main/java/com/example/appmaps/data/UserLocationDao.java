package com.example.appmaps.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserLocationDao {
    @Insert
    void insert(UserLocation location);

    @Query("SELECT * FROM user_locations WHERE userId = :userId")
    List<UserLocation> getLocationsForUser(int userId);
}