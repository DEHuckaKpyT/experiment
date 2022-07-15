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
public class TestModel {

    String name;
    Integer count;
    Date date;
    Double percent;
    Boolean flag;
}
