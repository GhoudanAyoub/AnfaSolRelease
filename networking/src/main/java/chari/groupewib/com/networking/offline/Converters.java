package ghoudan.anfaSolution.com.networking.offline;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ghoudan.anfaSolution.com.app_models.CustomerAnfa;
import ghoudan.anfaSolution.com.app_models.SupplierAnfa;

public class Converters {

    @TypeConverter
    public static Date toDate(Long dateLong) {
        return dateLong == null ? null : new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static List<String> fromStringToList(String value) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static String fromListToString(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public String fromLanguagesListToString(List<CustomerAnfa> languages) {
        return new Gson().toJson(languages);
    }

    @TypeConverter
    public List<CustomerAnfa> FromStringToLanguagesList(String json) {
        if (json == null) return (null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomerAnfa>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String fromMoneysListToString(List<SupplierAnfa> moneys) {
        return new Gson().toJson(moneys);
    }

    @TypeConverter
    public List<SupplierAnfa> FromStringToMoneysList(String json) {
        if (json == null) return (null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<SupplierAnfa>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
