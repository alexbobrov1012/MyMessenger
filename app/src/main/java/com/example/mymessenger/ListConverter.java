package com.example.mymessenger;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListConverter {
    @TypeConverter
    public String fromList(List<String> list) {
        return list.toString();
    }

    @TypeConverter
    public List<String> toList(String data) {
        data.replaceAll("\\[|\\]", "");
        return Arrays.asList(data.split(","));
    }
}
