package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.action.generic.ShowPictureAction;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.testutil.Helper;

public class ShowPictureActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-74, y=64], ShowPictureAction [EYESOPEN, NumConst [0], NumConst [0]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ShowPicture.xml"));
    }

    @Test
    public void getPicture() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowPicture.xml");
        ShowPictureAction<Void> spa = (ShowPictureAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("EYESOPEN", spa.getPicture().toString());
    }

    @Test
    public void getX() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowPicture.xml");
        ShowPictureAction<Void> spa = (ShowPictureAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [0]", spa.getX().toString());
    }

    @Test
    public void getY() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowPicture.xml");
        ShowPictureAction<Void> spa = (ShowPictureAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [0]", spa.getY().toString());
    }

    @Test
    public void missing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-9, y=3], ShowPictureAction [FLOWERS, EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], ShowPictureAction [OLDGLASSES, NumConst [0], EmptyExpr [defVal=NUMBER_INT]], ShowPictureAction [TACHO, EmptyExpr [defVal=NUMBER_INT], NumConst [0]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ShowPictureMissing.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_ShowPicture.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_ShowPicture1.xml");
    }

    @Test
    public void reverseTransformatinMissing() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_ShowPictureMissing.xml");
    }
}
