package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class TemperatureSensorTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfTemperatureSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=187, y=38], " + "MainTask [], " + "DisplayTextAction [TEXT, SensorExpr [TemperatureSensor []]]]]]";

        String result = Helper.generateTransformerString("/sensor/get_temperature.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/sensor/get_temperature.xml");
    }

}
