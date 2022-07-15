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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created on 14.07.2022.
 *
 * @author Denis Matytsin
 */
@ExtendWith(SoftAssertionsExtension.class)
class EntityFillerTest {

    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private Date firstDate;
    private Date secondDate;
    private Date thirdDate;
    private Date fourthDate;

    @BeforeEach
    void setUp() throws ParseException {
        this.firstDate = format.parse("2001-01-01");
        this.secondDate = format.parse("2001-01-02");
        this.thirdDate = format.parse("2001-01-03");
        this.fourthDate = format.parse("2001-01-04");
    }

    @Test
    void generateOne(SoftAssertions softly) {
        //Act
        TestModel model = EntityFiller.forClass(TestModel.class).generate();

        //Assert
        softly.assertThat(model.getCount()).isEqualTo(1);
        softly.assertThat(model.getName()).isEqualTo("text1");
        softly.assertThat(model.getDate()).isEqualTo(firstDate);
        softly.assertThat(model.getPercent()).isEqualTo(0.0);
    }

    @Test
    void generateOneWithBigText(SoftAssertions softly) {
        //Act
        ExtendedTestModel model = EntityFiller.forClass(ExtendedTestModel.class)
                                              .withBigText("longName")
                                              .generate();

        //Assert
        softly.assertThat(model.getCount()).isEqualTo(1);
        softly.assertThat(model.getOtherCount()).isEqualTo(2);
        softly.assertThat(model.getName()).isEqualTo("text1");
        softly.assertThat(model.getLongName()).isEqualTo("big text big text big text big text big text big text big text big text big text big text big text big text2");
        softly.assertThat(model.getDate()).isEqualTo(firstDate);
        softly.assertThat(model.getOtherDate()).isEqualTo(secondDate);
        softly.assertThat(model.getPercent()).isEqualTo(0.0);
        softly.assertThat(model.getOtherPercent()).isEqualTo(1.0 / 3.0);
    }

    @Test
    void generateList(SoftAssertions softly) {
        //Act
        List<TestModel> models = EntityFiller.forClass(TestModel.class).generateList(2);

        //Assert
        TestModel model1 = models.get(0);
        softly.assertThat(model1.getCount()).isEqualTo(1);
        softly.assertThat(model1.getName()).isEqualTo("text1");
        softly.assertThat(model1.getDate()).isEqualTo(firstDate);

        TestModel model2 = models.get(1);
        softly.assertThat(model2.getCount()).isEqualTo(2);
        softly.assertThat(model2.getName()).isEqualTo("text2");
        softly.assertThat(model2.getDate()).isEqualTo(secondDate);
    }

    @Test
    void generateListDefault(SoftAssertions softly){
        //Act
        List<ExtendedTestModel> models = EntityFiller.forClass(ExtendedTestModel.class).generateList();

        //Assert
        softly.assertThat(models.size()).isEqualTo(10);

        ExtendedTestModel model1 = models.get(0);
        softly.assertThat(model1.getCount()).isEqualTo(1);
        softly.assertThat(model1.getOtherCount()).isEqualTo(2);
        softly.assertThat(model1.getName()).isEqualTo("text1");
        softly.assertThat(model1.getLongName()).isEqualTo("text2");
        softly.assertThat(model1.getDate()).isEqualTo(firstDate);
        softly.assertThat(model1.getPercent()).isEqualTo(0.0);
        softly.assertThat(model1.getOtherPercent()).isEqualTo(1.0 / 3.0);

        ExtendedTestModel model2 = models.get(1);
        softly.assertThat(model2.getCount()).isEqualTo(2);
        softly.assertThat(model2.getOtherCount()).isEqualTo(3);
        softly.assertThat(model2.getName()).isEqualTo("text2");
        softly.assertThat(model2.getLongName()).isEqualTo("text3");
        softly.assertThat(model2.getDate()).isEqualTo(secondDate);
        softly.assertThat(model2.getOtherDate()).isEqualTo(thirdDate);
        softly.assertThat(model2.getPercent()).isEqualTo(1.0 / 3.0);
        softly.assertThat(model2.getOtherPercent()).isEqualTo(2.0 / 3.0);

        ExtendedTestModel model3 = models.get(2);
        softly.assertThat(model3.getCount()).isEqualTo(3);
        softly.assertThat(model3.getOtherCount()).isEqualTo(4);
        softly.assertThat(model3.getName()).isEqualTo("text3");
        softly.assertThat(model3.getLongName()).isEqualTo("text4");
        softly.assertThat(model3.getDate()).isEqualTo(thirdDate);
        softly.assertThat(model3.getOtherDate()).isEqualTo(fourthDate);
        softly.assertThat(model3.getPercent()).isEqualTo(2.0 / 3.0);
        softly.assertThat(model3.getOtherPercent()).isEqualTo(1.0);
    }

    @Test
    void generateOtherClass(SoftAssertions softly){
        //Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ExtendedTestModel model = EntityFiller.forClass(TestModel.class).generate();
        });

        softly.assertThat(exception.getMessage()).isEqualTo("com.example.test.model.TestModel cannot be cast to com.example.test.model.ExtendedTestModel");
    }
}