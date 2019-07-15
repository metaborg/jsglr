package org.spoofax.jsglr2.parsetable;

import static org.spoofax.terms.Term.*;

import java.util.Iterator;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parsetable.symbols.ConcreteSyntaxContext;
import org.spoofax.jsglr2.parsetable.symbols.ISortSymbol;
import org.spoofax.jsglr2.parsetable.symbols.ISymbol;
import org.spoofax.jsglr2.parsetable.symbols.SymbolReader;
import org.spoofax.terms.TermVisitor;

public class ProductionReader {

    final CharacterClassReader characterClassReader;

    public ProductionReader(CharacterClassReader characterClassReader) {
        this.characterClassReader = characterClassReader;
    }

    public IProduction read(IStrategoTerm productionWithIdTerm) throws ParseTableReadException {
        int productionId = intAt(productionWithIdTerm, 1);

        IStrategoAppl productionTerm = termAt(productionWithIdTerm, 0);
        IStrategoAppl lhsTerm = termAt(productionTerm, 1);
        IStrategoList rhsTerm = termAt(productionTerm, 0);
        IStrategoAppl attributesTerm = termAt(productionTerm, 2);

        SymbolReader symbolReader = new SymbolReader(characterClassReader);

        ISymbol lhs = symbolReader.read(lhsTerm);
        ISymbol[] rhs = new ISymbol[rhsTerm.size()];
        for(int i = 0; i < rhsTerm.size(); i++)
            rhs[i] = symbolReader.read((IStrategoAppl) rhsTerm.getSubterm(i));

        boolean isStringLiteral = getIsStringLiteral(rhsTerm);
        boolean isNumberLiteral = getIsNumberLiteral(rhsTerm);

        boolean isLexicalRhs = getIsLexicalRhs(rhsTerm);
        boolean isLayout = lhs.concreteSyntaxContext() == ConcreteSyntaxContext.Layout;
        boolean isLiteral = lhs.concreteSyntaxContext() == ConcreteSyntaxContext.Literal;
        boolean isLexical = lhs.concreteSyntaxContext() == ConcreteSyntaxContext.Lexical;

        boolean skippableLayout = isLayout && !getIsLayoutParent(lhsTerm, rhsTerm);
        boolean skippableLexical = !(lhs instanceof ISortSymbol) && (isLexical || (isLexicalRhs && !isLiteral));

        boolean isSkippableInParseForest = skippableLayout || skippableLexical;

        ProductionAttributes attributes = readProductionAttributes(attributesTerm);

        return new Production(productionId, lhs, rhs, isStringLiteral, isNumberLiteral, isLexicalRhs,
            isSkippableInParseForest, attributes);
    }

    private boolean getIsLayoutParent(IStrategoTerm lhs, IStrategoTerm rhs) {
        String descriptor = lhs.toString() + " -> " + rhs.toString();

        return "cf".equals(((IStrategoAppl) lhs).getConstructor().getName())
            && !"cf(layout) -> [cf(layout),cf(layout)]".equals(descriptor)
            && !"cf(opt(layout)) -> []".equals(descriptor);
    }

    private boolean getIsLexicalRhs(IStrategoList rhs) {
        if(rhs.getSubtermCount() > 0) {
            boolean lexRhs = true;

            for(IStrategoTerm rhsPart : rhs.getAllSubterms()) {
                String rhsPartConstructor = ((IStrategoAppl) rhsPart).getConstructor().getName();

                lexRhs &= "char-class".equals(rhsPartConstructor);
            }

            return lexRhs;
        } else
            return false;
    }

    private boolean getIsStringLiteral(IStrategoTerm rhs) {
        return topdownHasSpaces(rhs);
    }

    private boolean getIsNumberLiteral(IStrategoTerm rhs) {
        IStrategoTerm range = getFirstRange(rhs);

        return range != null && intAt(range, 0) == '0' && intAt(range, 1) == '9';
    }

    private boolean topdownHasSpaces(IStrategoTerm term) {
        Iterator<IStrategoTerm> iterator = TermVisitor.tryGetListIterator(term);

        for(int i = 0, max = term.getSubtermCount(); i < max; i++) {
            IStrategoTerm child = iterator == null ? term.getSubterm(i) : iterator.next();

            if(isRangeAppl(child)) {
                int start = intAt(child, 0);
                int end = intAt(child, 1);

                if(start <= ' ' && ' ' <= end)
                    return true;
            } else {
                if(topdownHasSpaces(child))
                    return true;
            }
        }

        return false;
    }

    private boolean isRangeAppl(IStrategoTerm child) {
        return isTermAppl(child) && ((IStrategoAppl) child).getName().equals("range");
    }

    private IStrategoTerm getFirstRange(IStrategoTerm term) {
        for(int i = 0; i < term.getSubtermCount(); i++) {
            IStrategoTerm child = termAt(term, i);

            if(isRangeAppl(child))
                return child;
            else {
                child = getFirstRange(child);

                if(child != null)
                    return child;
            }
        }

        return null;
    }

    private ProductionAttributes readProductionAttributes(IStrategoAppl attributesTerm) throws ParseTableReadException {
        if(attributesTerm.getName().equals("attrs")) {
            ProductionType type = ProductionType.NO_TYPE;

            IStrategoTerm constructor = null;

            boolean isRecover = false;
            boolean isBracket = false;
            boolean isCompletion = false;
            boolean isPlaceholderInsertion = false;
            boolean isLiteralCompletion = false;
            boolean isIgnoreLayout = false;
            boolean isNewlineEnforced = false;
            boolean isLongestMatch = false;
            boolean isCaseInsensitive = false;
            boolean isIndentPaddingLexical = false;
            boolean isFlatten = false;

            IStrategoList attributesTermsList = (IStrategoList) attributesTerm.getSubterm(0);

            for(IStrategoTerm attributeTerm : attributesTermsList) {
                IStrategoNamed attributeTermNamed = (IStrategoNamed) attributeTerm;

                String attributeName = attributeTermNamed.getName();

                switch(attributeName) {
                    case "reject":
                        type = ProductionType.REJECT;
                        break;
                    case "prefer":
                        type = ProductionType.PREFER;
                        break;
                    case "avoid":
                        type = ProductionType.AVOID;
                        break;
                    case "bracket":
                        // Irrelevant during parsing/imploding thus we ignore it here.
                        break;
                    case "assoc":
                        // This attribute is used to indicate left/right/assoc associativity. Since this is irrelevant
                        // during parsing/imploding we ignore it here.
                        break;
                    case "term":
                        if(attributeTermNamed.getSubterm(0) instanceof IStrategoNamed) {
                            IStrategoNamed attributeValueTermNamed = (IStrategoNamed) attributeTermNamed.getSubterm(0);

                            int subtermCount = attributeValueTermNamed.getSubtermCount();
                            String name = attributeValueTermNamed.getName();

                            if(subtermCount == 0) {
                                switch(name) {
                                    case "recover":
                                        isRecover = true;
                                        break;
                                    case "completion":
                                        isCompletion = true;
                                        break;
                                    case "placeholder-insertion":
                                        isPlaceholderInsertion = true;
                                        break;
                                    case "literal-completion":
                                        isLiteralCompletion = true;
                                        break;
                                    case "ignore-layout":
                                    case "no-lc":
                                    case "ignore-indent":
                                        isIgnoreLayout = true;
                                        break;
                                    case "enforce-newline":
                                        isNewlineEnforced = true;
                                        break;
                                    case "longest-match":
                                        isLongestMatch = true;
                                        break;
                                    case "case-insensitive":
                                        isCaseInsensitive = true;
                                        break;
                                    case "indentpadding":
                                        isIndentPaddingLexical = true;
                                        break;
                                    case "flatten":
                                        isFlatten = true;
                                        break;
                                    default:
                                        break;
                                }
                            } else if(subtermCount == 1 && name.equals("cons") && constructor == null) {
                                constructor = attributeValueTermNamed.getSubterm(0);
                            }
                        }
                        break;
                    case "id":
                        constructor = attributeTermNamed.getSubterm(0);
                        break;
                    default:
                        throw new ParseTableReadException("Unknown production attribute: " + attributeName);
                }
            }

            return new ProductionAttributes(type, constructor, isRecover, isBracket, isCompletion,
                isPlaceholderInsertion, isLiteralCompletion, isIgnoreLayout, isNewlineEnforced, isLongestMatch,
                isCaseInsensitive, isIndentPaddingLexical, isFlatten);
        } else if(attributesTerm.getName().equals("no-attrs")) {
            return new ProductionAttributes(ProductionType.NO_TYPE, null, false, false, false, false, false, false,
                false, false, false, false, false);
        }

        throw new ParseTableReadException("Unknown production attribute type: " + attributesTerm);
    }


}
