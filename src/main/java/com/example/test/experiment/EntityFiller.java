package com.example.test.experiment;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
public class EntityFiller {

    private final Constructor<?> constructor;
    private final Field[] fields;
    private final Calendar calendar = Calendar.getInstance();
    private final Date date;

    private final int STEP_DEFAULT = 0;
    private final int LIST_ITEMS_COUNT_DEFAULT = 5;

    private String[] withBigText;

    public <T> EntityFiller(Class<T> clazz) throws ParseException {
        this.constructor = clazz.getDeclaredConstructors()[0];
        this.constructor.setAccessible(true);
        this.fields = clazz.getDeclaredFields();

        this.date = new SimpleDateFormat("yyyy-MM-dd").parse("2001-01-01");
    }

    public static <T> EntityFiller forClass(Class<T> clazz) throws ParseException {
        return new EntityFiller(clazz);
    }

    public <T> T generate() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return generate(STEP_DEFAULT);
    }

    public <T> T generate(int step) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object[] params = new Object[fields.length];

        int additionalIntValue = 1 + step;
        int additionalStringValue = 1 + step;
        calendar.setTime(date);
        calendar.add(Calendar.DATE, step);

        for (int i = 0; i < fields.length; i++) {
            switch (fields[i].getGenericType().getTypeName()) {
                case "java.lang.Integer":
                    params[i] = additionalIntValue;
                    additionalIntValue++;
                    break;
                case "java.lang.String":
                    params[i] = getStringValue(fields[i], additionalStringValue);
                    additionalStringValue++;
                    break;
                case "java.util.Date":
                    params[i] = calendar.getTime();
                    calendar.add(Calendar.DATE, 1);
                    break;
                default:
                    params[i] = null;
                    break;
            }
        }

        return (T) constructor.newInstance(params);
    }

    public <T> List<T> generateList(int count) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return new ArrayList<T>() {{
            for (int i = 0; i < count; i++) add(generate(i));
        }};
    }

    public <T> List<T> generateList() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return generateList(LIST_ITEMS_COUNT_DEFAULT);
    }

    public EntityFiller withBigText(String... fieldNames) {
        this.withBigText = fieldNames;

        return this;
    }

    private String getStringValue(Field field, int additionalStringValue) {
        if (ArrayUtils.contains(withBigText, field.getName())) return "this a big text" + additionalStringValue;

        return "text" + additionalStringValue;
    }
}
