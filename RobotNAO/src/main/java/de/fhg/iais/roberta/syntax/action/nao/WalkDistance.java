package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.WalkDirection;
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
 * This class represents the <b>naoActions_walk</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for making the robot walk for a distance.<br/>
 * <br/>
 * The client must provide the {@link walkDirection} and {@link distanceToWalk} (direction and distance to walk).
 */
public final class WalkDistance<V> extends Action<V> {

    private final WalkDirection walkDirection;
    private final Expr<V> distanceToWalk;

    private WalkDistance(WalkDirection walkDirection, Expr<V> distanceToWalk, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("WALK_DISTANCE"), properties, comment);
        Assert.notNull(walkDirection, "Missing direction in WalkDistance block!");
        this.walkDirection = walkDirection;
        this.distanceToWalk = distanceToWalk;
        setReadOnly();
    }

    /**
     * Creates instance of {@link WalkDistance}. This instance is read only and can not be modified.
     *
     * @param direction {@link walkDirection} the robot will walk,
     * @param distance {@link distanceToWalk} the robot will walk for,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link WalkDistance}
     */
    private static <V> WalkDistance<V> make(WalkDirection walkDirection, Expr<V> distanceToWalk, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new WalkDistance<V>(walkDirection, distanceToWalk, properties, comment);
    }

    public WalkDirection getWalkDirection() {
        return this.walkDirection;
    }

    public Expr<V> getDistanceToWalk() {
        return this.distanceToWalk;
    }

    @Override
    public String toString() {
        return "WalkDistance [" + this.walkDirection + ", " + this.distanceToWalk + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitWalkDistance(this);
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

        String walkDirection = helper.extractField(fields, BlocklyConstants.DIRECTION);
        Phrase<V> walkDistance = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));

        return WalkDistance.make(
            WalkDirection.get(walkDirection),
            helper.convertPhraseToExpr(walkDistance),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.walkDirection.toString());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.POWER, this.distanceToWalk);

        return jaxbDestination;
    }
}
