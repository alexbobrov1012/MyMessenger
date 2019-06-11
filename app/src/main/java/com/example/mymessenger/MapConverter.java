package com.example.mymessenger;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MapConverter {
    @TypeConverter
    public String fromList(Map<String, List<String>> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    @TypeConverter
    public Map<String, List<String>> toList(String data) {
        Type mapType = new TypeToken<Map<String, List<String>>>() {
        }.getType();
        return new Gson().fromJson(data, mapType);
    }
}
