package de.fhg.iais.roberta.syntax.codegen;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.EV3Configuration;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.MathConst.Const;
import de.fhg.iais.roberta.typecheck.BlocklyType;

public class JavaVisitorTest {
    private static final Configuration brickConfiguration = new EV3Configuration.Builder().build();
    private static final Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>();

    @Test
    public void getIndentaion() throws Exception {
        Ast2Ev3JavaVisitor visitor = new Ast2Ev3JavaVisitor("Test", brickConfiguration, usedSensors, 0);
        Assert.assertEquals(0, visitor.getIndentation());
    }

    // String code = AstToLejosJavaVisitor.generate("Test", brickConfiguration, transformer.getTree(), true);
    @Test
    public void getSb() throws Exception {
        Ast2Ev3JavaVisitor visitor = new Ast2Ev3JavaVisitor("Test", brickConfiguration, usedSensors, 0);
        Assert.assertEquals("", visitor.getSb().toString());
    }

    @Test
    public void visitMathConst() throws Exception {
        MathConst<Void> mathConst = MathConst.make(Const.E, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        Ast2Ev3JavaVisitor visitor = new Ast2Ev3JavaVisitor("Test", brickConfiguration, usedSensors, 0);
        mathConst.visit(visitor);
        Assert.assertEquals("BlocklyMethods.E", visitor.getSb().toString());
    }

    @Test
    public void visitEmptyExpr() throws Exception {
        EmptyExpr<Void> emptyExpr = EmptyExpr.make(BlocklyType.NUMBER_INT);
        Ast2JavaVisitor visitor = new Ast2Ev3JavaVisitor("Test", brickConfiguration, usedSensors, 0);
        emptyExpr.visit(visitor);
        Assert.assertEquals("0", visitor.getSb().toString());
    }
}
