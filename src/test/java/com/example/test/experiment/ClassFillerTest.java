package com.example.test.experiment;

import com.example.test.model.ExtendedTestModel;
import com.example.test.model.TestModel;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created on 14.07.2022.
 *
 * @author Denis Matytsin
 */
@ExtendWith(SoftAssertionsExtension.class)
class ClassFillerTest {

    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private final Calendar calendar = new GregorianCalendar(2001, Calendar.JANUARY, 10);

    private Date firstDate;
    private Date secondDate;
    private Date thirdDate;
    private Date tenthDate;
    private Date eleventhDate;

    @BeforeEach
    void setUp() throws ParseException {
        this.firstDate = format.parse("2001-01-01");
        this.secondDate = format.parse("2001-01-02");
        this.thirdDate = format.parse("2001-01-03");
        this.tenthDate = format.parse("2001-01-10");
        this.eleventhDate = format.parse("2001-01-11");
    }

    @Test
    void generateOne(SoftAssertions softly) {
        //Act
        TestModel model = ClassFiller.forClass(TestModel.class).generate();

        //Assert
        softly.assertThat(model.getCount()).isEqualTo(1);
        softly.assertThat(model.getName()).isEqualTo("text1");
        softly.assertThat(model.getDate()).isEqualTo(firstDate);
        softly.assertThat(model.getPercent()).isEqualTo(1.0 / 3.0);
        softly.assertThat(model.getFlag()).isEqualTo(true);
    }

    @Test
    void generateOneWithCustomParams(SoftAssertions softly) {
        //Act
        ExtendedTestModel model = ClassFiller.forClass(ExtendedTestModel.class)
                                             .withBigText("longName")
                                             .withStartIntValue(1001)
                                             .withStartDate(tenthDate)
                                             .generate();

        //Assert
        softly.assertThat(model.getCount()).isEqualTo(1001);
        softly.assertThat(model.getOtherCount()).isEqualTo(1002);
        softly.assertThat(model.getName()).isEqualTo("text1");
        softly.assertThat(model.getLongName()).isEqualTo("big text big text big text big text big text big text big text big text big text big text big text big text2");
        softly.assertThat(model.getDate()).isEqualTo(tenthDate);
        softly.assertThat(model.getOtherDate()).isEqualTo(eleventhDate);
        softly.assertThat(model.getPercent()).isEqualTo(1.0 / 3.0);
        softly.assertThat(model.getOtherPercent()).isEqualTo(1.0 / 3.0 * 2.0);
        softly.assertThat(model.getFlag()).isEqualTo(true);
        softly.assertThat(model.getOtherFlag()).isEqualTo(false);
    }

    @Test
    void generateOneWithOverloadCustomParams(SoftAssertions softly) {
        //Act
        ExtendedTestModel model = ClassFiller.forClass(ExtendedTestModel.class)
                                             .withStartDate(calendar)
                                             .generate();

        //Assert
        softly.assertThat(model.getCount()).isEqualTo(1);
        softly.assertThat(model.getOtherCount()).isEqualTo(2);
        softly.assertThat(model.getName()).isEqualTo("text1");
        softly.assertThat(model.getLongName()).isEqualTo("text2");
        softly.assertThat(model.getDate()).isEqualTo(tenthDate);
        softly.assertThat(model.getOtherDate()).isEqualTo(eleventhDate);
        softly.assertThat(model.getPercent()).isEqualTo(1.0 / 3.0);
        softly.assertThat(model.getOtherPercent()).isEqualTo(1.0 / 3.0 * 2.0);
        softly.assertThat(model.getFlag()).isEqualTo(true);
        softly.assertThat(model.getOtherFlag()).isEqualTo(false);
    }

    @Test
    void generateList(SoftAssertions softly) {
        //Act
        List<TestModel> models = ClassFiller.forClass(TestModel.class).generateList(2);

        //Assert
        TestModel model1 = models.get(0);
        softly.assertThat(model1.getCount()).isEqualTo(1);
        softly.assertThat(model1.getName()).isEqualTo("text1");
        softly.assertThat(model1.getDate()).isEqualTo(firstDate);
        softly.assertThat(model1.getFlag()).isEqualTo(true);

        TestModel model2 = models.get(1);
        softly.assertThat(model2.getCount()).isEqualTo(2);
        softly.assertThat(model2.getName()).isEqualTo("text2");
        softly.assertThat(model2.getDate()).isEqualTo(secondDate);
        softly.assertThat(model2.getFlag()).isEqualTo(false);
    }

    @Test
    void generateListDefault(SoftAssertions softly) {
        //Act
        List<ExtendedTestModel> models = ClassFiller.forClass(ExtendedTestModel.class).generateList();

        //Assert
        softly.assertThat(models.size()).isEqualTo(10);

        ExtendedTestModel model1 = models.get(0);
        softly.assertThat(model1.getCount()).isEqualTo(1);
        softly.assertThat(model1.getOtherCount()).isEqualTo(2);
        softly.assertThat(model1.getName()).isEqualTo("text1");
        softly.assertThat(model1.getLongName()).isEqualTo("text2");
        softly.assertThat(model1.getDate()).isEqualTo(firstDate);
        softly.assertThat(model1.getPercent()).isEqualTo(1.0 / 3.0);
        softly.assertThat(model1.getOtherPercent()).isEqualTo(1.0 / 3.0 * 2.0);
        softly.assertThat(model1.getFlag()).isEqualTo(true);
        softly.assertThat(model1.getOtherFlag()).isEqualTo(false);

        ExtendedTestModel model2 = models.get(1);
        softly.assertThat(model2.getCount()).isEqualTo(2);
        softly.assertThat(model2.getOtherCount()).isEqualTo(3);
        softly.assertThat(model2.getName()).isEqualTo("text2");
        softly.assertThat(model2.getLongName()).isEqualTo("text3");
        softly.assertThat(model2.getDate()).isEqualTo(secondDate);
        softly.assertThat(model2.getOtherDate()).isEqualTo(thirdDate);
        softly.assertThat(model2.getPercent()).isEqualTo(1.0 / 3.0 * 2.0);
        softly.assertThat(model2.getOtherPercent()).isEqualTo(1.0 / 3.0 * 3.0);
        softly.assertThat(model2.getFlag()).isEqualTo(false);
        softly.assertThat(model2.getOtherFlag()).isEqualTo(true);

        ExtendedTestModel model10 = models.get(9);
        softly.assertThat(model10.getCount()).isEqualTo(10);
        softly.assertThat(model10.getOtherCount()).isEqualTo(11);
        softly.assertThat(model10.getName()).isEqualTo("text10");
        softly.assertThat(model10.getLongName()).isEqualTo("text11");
        softly.assertThat(model10.getDate()).isEqualTo(tenthDate);
        softly.assertThat(model10.getOtherDate()).isEqualTo(eleventhDate);
        softly.assertThat(model10.getPercent()).isEqualTo(1.0 / 3.0 * 10.0);
        softly.assertThat(model10.getOtherPercent()).isEqualTo(1.0 / 3.0 * 11.0);
        softly.assertThat(model10.getFlag()).isEqualTo(false);
        softly.assertThat(model10.getOtherFlag()).isEqualTo(true);
    }
}