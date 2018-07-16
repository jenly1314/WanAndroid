package com.king.wanandroid.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.king.wanandroid.bean.SearchHistory;

import java.util.List;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Dao
public interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchHistory searchHistory);

    @Delete
    void delete(SearchHistory searchHistory);

    @Query("DELETE FROM SearchHistory")
    void deleteAll();

    @Query("SELECT * FROM SearchHistory")
    LiveData<List<SearchHistory>> getAllHistory();

    @Query("SELECT * FROM SearchHistory ORDER BY time DESC LIMIT :count")
    LiveData<List<SearchHistory>> getHistory(int count);

}
