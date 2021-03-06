package uk.org.tombolo.field.transformation;

import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import uk.org.tombolo.AbstractTest;
import uk.org.tombolo.FieldBuilder;
import uk.org.tombolo.TestFactory;
import uk.org.tombolo.core.Attribute;
import uk.org.tombolo.core.Subject;
import uk.org.tombolo.recipe.FieldRecipe;
import uk.org.tombolo.field.value.LatestValueField;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FieldValueSumFieldTest extends AbstractTest {
    FieldValueSumField field;

    Subject subject;

    @Before
    public void setUp() throws Exception {
        FieldRecipe fs1 = FieldBuilder.latestValue("default_provider_label", "f1a_label").setLabel("f1").build();
        FieldRecipe fs2 = FieldBuilder.latestValue("default_provider_label", "f2a_label").setLabel("f2").build();

        subject = TestFactory.makeNamedSubject("E01002766");
        Attribute f1 = TestFactory.makeAttribute(TestFactory.DEFAULT_PROVIDER, "f1a_label");
        Attribute f2 = TestFactory.makeAttribute(TestFactory.DEFAULT_PROVIDER, "f2a_label");
        TestFactory.makeTimedValue(subject.getSubjectType(), "E01002766", f1, "2011-01-01T00:00:00", 10d);
        TestFactory.makeTimedValue(subject.getSubjectType(), "E01002766", f2, "2011-01-01T00:00:00", 40d);

        field = new FieldValueSumField("FVSF-label", "FVSF-name", Arrays.asList(fs1, fs2));
    }

    @Test
    public void initialize() throws Exception {
        field.initialize();
        assertEquals(2, field.sumFields.size());

        assertEquals(LatestValueField.class.getName(), field.sumFields.get(0).getClass().getName());
        assertEquals("f1", field.sumFields.get(0).getLabel());

        assertEquals(LatestValueField.class.getName(), field.sumFields.get(1).getClass().getName());
        assertEquals("f2", field.sumFields.get(1).getLabel());
    }

    @Test
    public void valueForSubject() throws Exception {
        String value = field.valueForSubject(subject, true);
        assertEquals(50d,Double.valueOf(value),1d);
    }

    @Test
    public void jsonValueForSubject() throws Exception {
        String jsonString = field.jsonValueForSubject(subject, null).toJSONString();
        JSONAssert.assertEquals("{" +
                "  FVSF-label: 50.0" +
                "}", jsonString, false);
    }

    @Test
    public void getLabel() throws Exception {
        assertEquals("FVSF-label", field.getLabel());
    }

}