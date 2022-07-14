package com.example.test.experiment;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created on 14.07.2022.
 *
 * @author Denis Matytsin
 */
public class TemplateFillerHelper {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Calendar calendar = Calendar.getInstance();

    public static <T> List<T> getValues(Class<T> clazz, int count) throws IllegalAccessException, InstantiationException, ParseException {
        List<T> instances = new ArrayList<T>();

        for (int i = 0; i < count; i++) instances.add(getValue(clazz, i));

        return instances;
    }

    public static <T> T getValue(Class<T> clazz, int rowNumber) throws InstantiationException, IllegalAccessException, ParseException {
        T instance = clazz.newInstance();

        int additionalIntValue = 1001 + rowNumber;
        int additionalStringValue = 1 + rowNumber;
        Date date = dateFormat.parse("2022-01-01");
        calendar.setTime(date);
        calendar.add(Calendar.DATE, rowNumber);

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            switch (field.getGenericType().getTypeName()) {
                case "java.lang.Integer":
                    field.set(instance, additionalIntValue);
                    additionalIntValue++;
                    break;
                case "java.lang.String":
                    field.set(instance, "text" + additionalStringValue);
                    additionalStringValue++;
                    break;
                case "java.util.Date":
                    field.set(instance, calendar.getTime());
                    calendar.add(Calendar.DATE, 1);
                    break;
                default:
                    field.set(instance, null);
                    break;
            }
        }

        return instance;
    }

    public static <T> T getValue(Class<T> clazz) throws InstantiationException, IllegalAccessException, ParseException {
        return getValue(clazz, 0);
    }
}
