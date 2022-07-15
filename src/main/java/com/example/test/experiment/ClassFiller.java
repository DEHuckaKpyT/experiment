package com.example.test.experiment;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created on 14.07.2022.
 *
 * @author Denis Matytsin
 */
public class ClassFiller<T> {

    private final Constructor<T> constructor;
    private final Field[] fields;

    //region статические параметры
    private final static int DEFAULT_FIRST_STEP = 0;
    private final static int DEFAULT_STEP_VALUE = 1;
    private final static int DEFAULT_LIST_ITEMS_COUNT = 10;
    private final static String DEFAULT_TEXT = "text";
    private final static String DEFAULT_BIG_TEXT = "big text big text big text big text big text big text big text big text big text big text big text big text";
    private final static double DEFAULT_STEP_DOUBLE_VALUE = 1.0 / 3.0;
    private final static int START_ADDITIONAL_DOUBLE_STEP = 1;
    private final static int START_STRING_VALUE = 1;
    private final static boolean START_BOOLEAN_VALUE = true;
    //endregion

    //region настраиваемые параметры
    private int START_INT_VALUE = 1;
    private Calendar START_DATE = new GregorianCalendar(2001, Calendar.JANUARY, 1);
    private String[] FIELDS_WITH_BIG_TEXT = new String[]{};
    //endregion

    private ClassFiller(Class<T> clazz) {
        this.constructor = (Constructor<T>) clazz.getDeclaredConstructors()[0];
        this.constructor.setAccessible(true);
        this.fields = clazz.getDeclaredFields();
    }

    /**
     * @param clazz класс, который необходимо заполнить
     */
    public static <T> ClassFiller<T> forClass(Class<T> clazz) {
        return new ClassFiller<>(clazz);
    }

    /**
     * Получить экземпляр с заполненными полями
     *
     * @return экземпляр с заполненными полями
     */
    public T generate() {
        return generate(DEFAULT_FIRST_STEP);
    }

    /**
     * Получить экземпляр с заполненными полями
     *
     * @param step шаг, который необходимо добавить для текущего экземпляра
     *
     * @return экземпляр с заполненными полями
     */
    public T generate(int step) {
        Object[] params = new Object[fields.length];
        int currentStepIntValue = START_INT_VALUE + step;
        int currentStepStringValue = START_STRING_VALUE + step;
        int currentStepDoubleValue = START_ADDITIONAL_DOUBLE_STEP + step;
        boolean currentStepBooleanValue = START_BOOLEAN_VALUE ^ (step % 2 == 1);
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
                    params[i] = DEFAULT_STEP_DOUBLE_VALUE * currentStepDoubleValue;
                    currentStepDoubleValue++;
                    break;
                case "java.lang.Boolean":
                    params[i] = currentStepBooleanValue;
                    currentStepBooleanValue = !currentStepBooleanValue;
                    break;
                default:
                    params[i] = null;
                    break;
            }
        }

        try {
            return constructor.newInstance(params);
        } catch (Exception ex) {
            throw new RuntimeException("Error while creating new instance");
        }
    }

    /**
     * Получить список экземпляров с заполненными полями
     *
     * @param count количество экземпляров
     *
     * @return список экземпляров с заполненными полями
     */
    public List<T> generateList(int count) {
        return new ArrayList<T>() {{
            for (int i = 0; i < count; i++) add(generate(i));
        }};
    }

    /**
     * Получить список экземпляров с заполненными полями
     *
     * @return список экземпляров с заполненными полями
     */
    public List<T> generateList() {
        return generateList(DEFAULT_LIST_ITEMS_COUNT);
    }

    /**
     * Присвоить определённым полям длинный текст
     *
     * @param fieldNames наименования полей, у которых должен быть длинный текст
     */
    public ClassFiller<T> withBigText(String... fieldNames) {
        this.FIELDS_WITH_BIG_TEXT = fieldNames;
        return this;
    }

    /**
     * Задать начальное значение для числовых полей
     *
     * @param value начальное значение
     */
    public ClassFiller<T> withStartIntValue(int value) {
        this.START_INT_VALUE = value;
        return this;
    }

    /**
     * Задать начальное значение для даты
     *
     * @param date начальное значение
     */
    public ClassFiller<T> withStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.START_DATE = calendar;
        return this;
    }

    /**
     * Задать начальное значение для даты
     *
     * @param calendar начальное значение
     */
    public ClassFiller<T> withStartDate(Calendar calendar) {
        this.START_DATE = calendar;
        return this;
    }

    private String getStringValue(Field field, int currentStepStringValue) {
        if (ArrayUtils.contains(FIELDS_WITH_BIG_TEXT, field.getName())) {
            return DEFAULT_BIG_TEXT + currentStepStringValue;
        }

        return DEFAULT_TEXT + currentStepStringValue;
    }
}
