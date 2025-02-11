package de.fhg.iais.roberta.syntax.expr;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.Sig;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents all binary operations from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Op, Expr, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link Op} contains all allowed binary operations.
 */
public final class Binary<V> extends Expr<V> {
    private final Op op;
    private final Expr<V> left;
    private final Expr<V> right;
    private final String operationRange;

    private Binary(Op op, Expr<V> left, Expr<V> right, String operationRange, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BINARY"), properties, comment);
        Assert.isTrue(op != null && left != null && right != null && left.isReadOnly() && right.isReadOnly());
        this.op = op;
        this.left = left;
        this.right = right;
        this.operationRange = operationRange;
        this.setReadOnly();
    }

    /**
     * Creates instance of {@link Binary}. This instance is read only and can not be modified.
     *
     * @param op operator; must be <b>not</b> null,
     * @param left expression on the left hand side; must be <b>not</b> null and <b>read only</b>,
     * @param right expression on the right hand side; must be <b>not</b> null and <b>read only</b>,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return Binary expression
     */

    public static <V> Binary<V> make(Op op, Expr<V> left, Expr<V> right, String operationRange, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Binary<>(op, left, right, operationRange, properties, comment);
    }

    /**
     * @return the operation in the binary expression. See enum {@link Op} for all possible operations
     */
    public Op getOp() {
        return this.op;
    }

    /**
     * @return the expression on the left hand side. Returns subclass of {@link Expr}
     */
    public Expr<V> getLeft() {
        return this.left;
    }

    /**
     * @return the expression on the right hand side. Returns subclass of {@link Expr}
     */
    public Expr<V> getRight() {
        return this.right;
    }

    /**
     * @return the operationRange
     */
    public String getOperationRange() {
        return this.operationRange;
    }

    @Override
    public int getPrecedence() {
        return this.op.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.op.getAssoc();
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.CAPTURED_TYPE;
    }

    @Override
    public String toString() {
        return "Binary [" + this.op + ", " + this.left + ", " + this.right + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitBinary(this);
    }

    /**
     * Operators for the binary expression.
     */
    public static enum Op {
        ADD( 100, Assoc.LEFT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "+" ),
        MINUS( 100, Assoc.LEFT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "-" ),
        MULTIPLY( 200, Assoc.LEFT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "*" ),
        DIVIDE( 200, Assoc.LEFT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "/" ),
        MOD( 200, Assoc.NONE, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "%" ),
        EQ( 80, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE), "==" ),
        NEQ( 80, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE), "!=", "<>" ),
        LT( 90, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER, BlocklyType.NUMBER), "<" ),
        LTE( 90, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER, BlocklyType.NUMBER), "<=" ),
        GT( 90, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER, BlocklyType.NUMBER), ">" ),
        GTE( 90, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER, BlocklyType.NUMBER), ">=" ),
        AND( 70, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.BOOLEAN, BlocklyType.BOOLEAN), "&&" ),
        OR( 60, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.BOOLEAN, BlocklyType.BOOLEAN), "||" ),
        MATH_CHANGE( 80, Assoc.NONE, Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE), "+=" ),
        TEXT_APPEND( 1, Assoc.LEFT, Sig.of(BlocklyType.STRING, BlocklyType.STRING, BlocklyType.STRING), "+=", "TEXTAPPEND" ),
        IN( 1, Assoc.LEFT, Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE), ":" ),
        ASSIGNMENT( 1, Assoc.RIGHT, Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE), "=" ),
        ADD_ASSIGNMENT( 1, Assoc.RIGHT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "+=" ),
        MINUS_ASSIGNMENT( 1, Assoc.RIGHT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "-=" ),
        MULTIPLY_ASSIGNMENT( 1, Assoc.RIGHT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "*=" ),
        DIVIDE_ASSIGNMENT( 1, Assoc.RIGHT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "/=" ),
        MOD_ASSIGNMENT( 1, Assoc.RIGHT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "%=" );

        private final String[] values;
        private final int precedence;
        private final Assoc assoc;
        private final Sig sig;

        private Op(int precedence, Assoc assoc, Sig sig, String... values) {
            this.precedence = precedence;
            this.assoc = assoc;
            this.values = values;
            this.sig = sig;
        }

        /**
         * @return mathematical symbol of the operation.
         */
        public String getOpSymbol() {
            if ( this.values.length == 0 ) {
                return this.toString();
            } else {
                return this.values[0];
            }
        }

        /**
         * @return precedence of the operator.
         */
        public int getPrecedence() {
            return this.precedence;
        }

        /**
         * @return association of the operator
         */
        public Assoc getAssoc() {
            return this.assoc;
        }

        /**
         * get the signature. The caller has to check for <code>null</code>!
         *
         * @return the signature; if not found, return <code>null</code>
         */
        public Sig getSignature() {
            return this.sig;
        }

        /**
         * get operator from {@link Op} from string parameter. It is possible for one operator to have multiple string mappings.
         * Throws exception if the operator does not exists.
         *
         * @param name of the operator
         * @return operator from the enum {@link Op}
         */
        public static Op get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid binary operator symbol: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Op op : Op.values() ) {
                if ( op.toString().equals(sUpper) ) {
                    return op;
                }
                for ( String value : op.values ) {
                    if ( sUpper.equals(value) ) {
                        return op;
                    }
                }
            }
            throw new DbcException("Invalid binary operator symbol: " + s);
        }
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {

        List<Value> values;
        Phrase<V> leftt;
        Phrase<V> rightt;
        switch ( block.getType() ) {
            case BlocklyConstants.TEXT_APPEND:
                values = helper.extractValues(block, (short) 2);
                leftt = helper.extractValue(values, new ExprParam(BlocklyConstants.VAR, BlocklyType.STRING));
                rightt = helper.extractValue(values, new ExprParam(BlocklyConstants.TEXT, BlocklyType.STRING));
                return ExprStmt.make(
                    Binary.make(
                        Binary.Op.TEXT_APPEND,
                        helper.convertPhraseToExpr(leftt),
                        helper.convertPhraseToExpr(rightt),
                        "",
                        helper.extractBlockProperties(block),
                        helper.extractComment(block)));
            case BlocklyConstants.ROB_MATH_CHANGE:
            case BlocklyConstants.MATH_CHANGE:
                values = helper.extractValues(block, (short) 2);
                leftt = helper.extractValue(values, new ExprParam(BlocklyConstants.VAR, BlocklyType.STRING));
                rightt = helper.extractValue(values, new ExprParam(BlocklyConstants.DELTA, BlocklyType.NUMBER_INT));
                return ExprStmt.make(
                    Binary.make(
                        Binary.Op.MATH_CHANGE,
                        helper.convertPhraseToExpr(leftt),
                        helper.convertPhraseToExpr(rightt),
                        "",
                        helper.extractBlockProperties(block),
                        helper.extractComment(block)));

            case BlocklyConstants.MATH_MODULO:
                return helper.blockToBinaryExpr(
                    block,
                    new ExprParam(BlocklyConstants.DIVIDEND, BlocklyType.NUMBER_INT),
                    new ExprParam(BlocklyConstants.DIVISOR, BlocklyType.NUMBER_INT),
                    BlocklyConstants.MOD);

            case BlocklyConstants.MATH_ARITHMETIC:
                String opp = helper.extractOperation(block, BlocklyConstants.OP_);
                if ( opp.equals(BlocklyConstants.POWER) ) {
                    ArrayList<ExprParam> exprParams = new ArrayList<>();
                    exprParams.add(new ExprParam(BlocklyConstants.A, BlocklyType.NUMBER_INT));
                    exprParams.add(new ExprParam(BlocklyConstants.B, BlocklyType.NUMBER_INT));
                    List<Expr<V>> params = helper.extractExprParameters(block, exprParams);
                    return MathPowerFunct.make(FunctionNames.POWER, params, helper.extractBlockProperties(block), helper.extractComment(block));
                }
            default:
                return helper.blockToBinaryExpr(
                    block,
                    new ExprParam(BlocklyConstants.A, BlocklyType.NUMBER_INT),
                    new ExprParam(BlocklyConstants.B, BlocklyType.NUMBER_INT),
                    BlocklyConstants.OP_);

        }
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        if ( !this.operationRange.equals("") ) {
            Mutation mutation = new Mutation();
            mutation.setOperatorRange(this.operationRange);
            jaxbDestination.setMutation(mutation);
        }
        switch ( getOp() ) {

            case MATH_CHANGE:
                JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VAR, getLeft());
                JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.DELTA, getRight());
                return jaxbDestination;
            case TEXT_APPEND:
                JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VAR, getLeft());
                JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.TEXT, getRight());
                return jaxbDestination;

            case MOD:
                JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.DIVIDEND, getLeft());
                JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.DIVISOR, getRight());
                return jaxbDestination;

            default:
                JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.OP_, getOp().name());
                JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.A, getLeft());
                JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.B, getRight());
                return jaxbDestination;
        }
    }
}
