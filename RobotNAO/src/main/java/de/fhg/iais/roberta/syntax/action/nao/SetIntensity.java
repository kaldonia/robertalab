package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Frame;
import de.fhg.iais.roberta.mode.action.nao.Led;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class represents the <b>naoActions_setIntensity</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the intensity of the NON-RGB-LEDs of NAO.<br/>
 * <br/>
 * The client must provide the {@link led}.
 */
public final class SetIntensity<V> extends Action<V> {

    private final Led led;
    private final Expr<V> Intensity;

    private SetIntensity(Led led, Expr<V> Intensity, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SET_INTENSITY"), properties, comment);
        this.led = led;
        Assert.notNull(Intensity);
        this.Intensity = Intensity;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SetIntensity}. This instance is read only and can not be modified.
     *
     * @param frame {@link Frame} the coordinates relate to,
     * @param speed {@link speed} the movement will be executed at,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SetIntensity}
     */
    private static <V> SetIntensity<V> make(Led led, Expr<V> Intensity, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SetIntensity<V>(led, Intensity, properties, comment);
    }

    public Led getLed() {
        return this.led;
    }

    public Expr<V> getIntensity() {
        return this.Intensity;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitSetIntensity(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        List<Value> values = helper.extractValues(block, (short) 1);

        Phrase<V> Intensity = helper.extractValue(values, new ExprParam(BlocklyConstants.INTENSITY, BlocklyType.NUMBER_INT));

        String leds = helper.extractField(fields, BlocklyConstants.LED);

        return SetIntensity.make(Led.get(leds), helper.convertPhraseToExpr(Intensity), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.LED, this.led.toString());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.INTENSITY, this.Intensity);

        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "SetLeds [" + this.led + ", " + this.Intensity + ", " + "]";
    }
}
