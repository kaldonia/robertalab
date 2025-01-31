package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class CompassSensorTest {
    Helper h = new Helper();

    @Test
    public void sensorSetGyro() throws Exception {
        String a = "BlockAST [project=[[Location [x=137, y=263], CompassSensor []]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_Compass.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_Compass.xml");
    }

}
