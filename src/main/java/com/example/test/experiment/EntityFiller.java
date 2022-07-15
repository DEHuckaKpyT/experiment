package com.example.test.experiment;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created on 14.07.2022.
 *
 * @author Denis Matytsin
 */
public class EntityFiller {

    private final Constructor<?> constructor;
    private final Field[] fields;

    private final int DEFAULT_FIRST_STEP = 0;
    private final int DEFAULT_STEP_VALUE = 1;
    private final int DEFAULT_LIST_ITEMS_COUNT = 10;
    private String DEFAULT_TEXT = "text";
    private final String DEFAULT_BIG_TEXT = "big text big text big text big text big text big text big text big text big text big text big text big text";
    private int START_INT_VALUE = 1;
    private final int START_STRING_VALUE = 1;
    private final double START_DOUBLE_VALUE = 1.0 / 3.0;
    private final Calendar START_DATE = new GregorianCalendar(2001, Calendar.JANUARY, 1);

    private String[] fieldsWithBigText;

    private <T> EntityFiller(Class<T> clazz) {
        this.constructor = clazz.getDeclaredConstructors()[0];
        this.constructor.setAccessible(true);
        this.fields = clazz.getDeclaredFields();
    }

    public static <T> EntityFiller forClass(Class<T> clazz) {
        return new EntityFiller(clazz);
    }

    public <T> T generate() {
        return generate(DEFAULT_FIRST_STEP);
    }

    public <T> T generate(int step) {
        Object[] params = new Object[fields.length];
        int currentStepIntValue = START_INT_VALUE + step;
        int currentStepStringValue = START_STRING_VALUE + step;
        int currentStepDoubleValue = step;
        Calendar currentStepDate = (Calendar) START_DATE.clone();
        currentStepDate.add(Calendar.DATE, step);

        for (int i = 0; i < fields.length; i++) {
            switch (fields[i].getGenericType().getTypeName()) {
                case "java.lang.Integer":
                    params[i] = currentStepIntValue;
                    currentStepIntValue += DEFAULT_STEP_VALUE;
                    break;
                case "java.lang.String":
                    params[i] = getStringValue(fields[i], currentStepStringValue);
                    currentStepStringValue += DEFAULT_STEP_VALUE;
                    break;
                case "java.util.Date":
                    params[i] = currentStepDate.getTime();
                    currentStepDate.add(Calendar.DATE, DEFAULT_STEP_VALUE);
                    break;
                case "java.lang.Double":
                    params[i] = START_DOUBLE_VALUE * currentStepDoubleValue;
                    currentStepDoubleValue++;
                    break;
                default:
                    params[i] = null;
                    break;
            }
        }

        try {
            return (T) constructor.newInstance(params);
        } catch (Exception ex) {
            throw new RuntimeException("Error while creating new instance");
        }
    }

    public <T> List<T> generateList(int count) {
        return new ArrayList<T>() {{
            for (int i = 0; i < count; i++) add(generate(i));
        }};
    }

    public <T> List<T> generateList() {
        return generateList(DEFAULT_LIST_ITEMS_COUNT);
    }

    public EntityFiller withBigText(String... fieldNames) {
        this.fieldsWithBigText = fieldNames;
        return this;
    }

    public EntityFiller withStartIntValue(int value) {
        this.START_INT_VALUE = value;
        return this;
    }

    private String getStringValue(Field field, int additionalStringValue) {
        if (ArrayUtils.contains(fieldsWithBigText, field.getName())) return DEFAULT_BIG_TEXT + additionalStringValue;

        return DEFAULT_TEXT + additionalStringValue;
    }
}
