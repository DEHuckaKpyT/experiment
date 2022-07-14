package com.example.test.experiment;

import com.example.test.model.SecondTestModel;
import com.example.test.model.TestModel;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created on 14.07.2022.
 *
 * @author Denis Matytsin
 */
@ExtendWith(SoftAssertionsExtension.class)
class EntityFillerTest {

    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private Date date;
    private Date otherDate;

    @BeforeEach
    void setUp() throws ParseException {
        this.date = format.parse("2001-01-01");
        this.otherDate = format.parse("2001-01-02");
    }

    @Test
    void generateOne(SoftAssertions softly) throws ParseException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //Act
        TestModel model = EntityFiller.forClass(TestModel.class).generate();

        //Assert
        softly.assertThat(model.getCount()).isEqualTo(1);
        softly.assertThat(model.getName()).isEqualTo("text1");
        softly.assertThat(model.getDate()).isEqualTo(date);
    }

    @Test
    void generateOneWithBigText(SoftAssertions softly) throws ParseException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //Act
        SecondTestModel model = EntityFiller.forClass(SecondTestModel.class)
                                            .withBigText("longName")
                                            .generate();

        //Assert
        softly.assertThat(model.getCount()).isEqualTo(1);
        softly.assertThat(model.getOtherCount()).isEqualTo(2);
        softly.assertThat(model.getName()).isEqualTo("text1");
        softly.assertThat(model.getLongName()).isEqualTo("this a big text2");
        softly.assertThat(model.getDate()).isEqualTo(date);
        softly.assertThat(model.getOtherDate()).isEqualTo(otherDate);
    }

    @Test
    void generateList(SoftAssertions softly) throws ParseException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //Act
        List<TestModel> models = EntityFiller.forClass(TestModel.class).generateList(2);

        //Assert
        TestModel model1 = models.get(0);
        softly.assertThat(model1.getCount()).isEqualTo(1);
        softly.assertThat(model1.getName()).isEqualTo("text1");
        softly.assertThat(model1.getDate()).isEqualTo(date);

        TestModel model2 = models.get(1);
        softly.assertThat(model2.getCount()).isEqualTo(2);
        softly.assertThat(model2.getName()).isEqualTo("text2");
        softly.assertThat(model2.getDate()).isEqualTo(otherDate);
    }
}