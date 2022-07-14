package com.example.test.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

/**
 * Created on 14.07.2022.
 *
 * @author Denis Matytsin
 */
@Value
@Builder
public class SecondTestModel {

    String name;
    String longName;
    Integer count;
    Integer otherCount;
    Date date;
    Date otherDate;
}
