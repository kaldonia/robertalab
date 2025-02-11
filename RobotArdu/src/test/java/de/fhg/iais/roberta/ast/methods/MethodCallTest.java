package de.fhg.iais.roberta.ast.methods;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.factory.ArduFactory;
import de.fhg.iais.roberta.util.test.Helper;

public class MethodCallTest {
    Helper h = new Helper();
    ArduFactory robotFactory = new ArduFactory(null);

    @Before
    public void setUp() throws Exception {
        this.h.setRobotFactory(this.robotFactory);
    }

    @Test
    public void methodCall1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=44, y=104], MethodReturn [Funktionsblock, VarDeclaration [NUMBER, x, EmptyExpr [defVal=NUMBER], true, false], "
                + "VarDeclaration [NUMBER, x2, EmptyExpr [defVal=NUMBER], true, false], "
                + "VarDeclaration [NUMBER, x3, EmptyExpr [defVal=NUMBER], false, false], \n"
                + "AktionStmt [LightAction [GREEN, ON]]MethodStmt [MethodIfReturn [Binary [EQ, NumConst [0], NumConst [0]], NUMBER, Var [x2]]], NUMBER, Var [x3]]], [Location [x=75, y=488], MethodCall [Funktionsblock, Var [x], Var [x2], Var [x3], NumConst [0], NumConst [1], NumConst [2], NUMBER]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/methods/method_call_1.xml"));
    }

    @Test
    public void methodCall2() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-28, y=85], MethodReturn [Funktionsblock, , , "
                + "STRING, StringConst []]], [Location [x=-16, y=181], MethodCall [Funktionsblock, , , STRING]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/methods/method_call_2.xml"));
    }

    @Test
    public void methodCall3() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=5, y=28], MethodVoid [Funktionsblock, "
                + "VarDeclaration [NUMBER, x, EmptyExpr [defVal=NUMBER], true, false], "
                + "VarDeclaration [NUMBER, x2, EmptyExpr [defVal=NUMBER], false, false], \n"
                + "AktionStmt [VolumeAction [SET, NumConst [50]]]]], [Location [x=6, y=189], MethodCall [Funktionsblock, Var [x], Var [x2], NumConst [0], NumConst [2], VOID]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/methods/method_call_3.xml"));
    }

    @Test
    public void methodCall4() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=5, y=28], MethodVoid [Funktionsblock, , \n"
                + "AktionStmt [VolumeAction [SET, NumConst [50]]]]], [Location [x=6, y=189], MethodCall [Funktionsblock, , , VOID]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/methods/method_call_4.xml"));
    }

    @Test
    public void reverseTransformation1() throws Exception {
        this.h.assertTransformationIsOk("/ast/methods/method_call_1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        this.h.assertTransformationIsOk("/ast/methods/method_call_2.xml");
    }

    @Test
    public void reverseTransformation3() throws Exception {
        this.h.assertTransformationIsOk("/ast/methods/method_call_3.xml");
    }

    @Test
    public void reverseTransformation4() throws Exception {
        this.h.assertTransformationIsOk("/ast/methods/method_call_4.xml");
    }

}
