package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.Helper;

@Ignore
public class ClearDisplayActionTest {
    Helper h = new Helper();

    @Test
    public void clearDisplay() throws Exception {
        String a = "publicvoidrun(){clearscreen();}";

        this.h.assertCodeIsOk(a, "/ast/actions/action_ClearDisplay.xml", false);
    }
}