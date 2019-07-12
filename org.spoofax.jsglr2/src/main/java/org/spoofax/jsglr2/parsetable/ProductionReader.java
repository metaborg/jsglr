package org.spoofax.jsglr2.parsetable;

import static org.spoofax.terms.StrategoListIterator.iterable;
import static org.spoofax.terms.Term.*;

import java.util.Iterator;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.interpreter.terms.*;
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

        // A tuple of the production right hand side, left hand side and attributes
        IStrategoAppl productionTerm = termAt(productionWithIdTerm, 0);
        IStrategoAppl lhsTerm = termAt(productionTerm, 1);
        IStrategoList rhsTerm = termAt(productionTerm, 0);
        IStrategoAppl attributesTerm = termAt(productionTerm, 2);

        ProductionAttributes attributes = readProductionAttributes(attributesTerm);

        String sort = getSort(lhsTerm);
        String startSymbolSort = getStartSymbolSort(lhsTerm, rhsTerm);
        String descriptor = lhsTerm.toString() + " -> " + rhsTerm.toString();
        boolean isLayout = getIsLayout(lhsTerm);
        boolean isLayoutParent = getIsLayoutParent(lhsTerm, descriptor);
        boolean isLiteral = getIsLiteral(lhsTerm);
        boolean isLexical = getIsLexical(lhsTerm);
        boolean isLexicalRhs = getIsLexicalRhs(rhsTerm);
        boolean isList = getIsList(lhsTerm, attributes.isFlatten);
        boolean isOptional = getIsOptional(lhsTerm);
        boolean isStringLiteral = getIsStringLiteral(rhsTerm);
        boolean isNumberLiteral = getIsNumberLiteral(rhsTerm);
        boolean isOperator = getIsOperator(lhsTerm, isLiteral);

        boolean skippableLayout = isLayout && !isLayoutParent;
        boolean skippableLexical = sort == null && (isLexical || (isLexicalRhs && !isLiteral));

        boolean isSkippableInParseForest = skippableLayout || skippableLexical;

        boolean isContextFree = !(isLayout || isLiteral || isLexical || isLexicalRhs);

        SymbolReader symbolReader = new SymbolReader(characterClassReader);
        ISymbol lhs = symbolReader.read(lhsTerm);
        ISymbol[] rhs = new ISymbol[rhsTerm.size()];
        for(int i = 0; i < rhsTerm.size(); i++)
            rhs[i] = symbolReader.read((IStrategoAppl) rhsTerm.getSubterm(i));

        return new Production(productionId, sort, startSymbolSort, descriptor, isContextFree, isLayout, isLiteral,
            isLexical, isLexicalRhs, isSkippableInParseForest, isList, isOptional, isStringLiteral, isNumberLiteral,
            isOperator, attributes);
    }

    private String getSort(IStrategoAppl lhs) {
        for(IStrategoTerm current = lhs; current.getSubtermCount() > 0 && isTermAppl(current); current =
            termAt(current, 0)) {
            String sort = tryGetSort((IStrategoAppl) current);

            if(sort != null)
                return sort;
        }

        return null;
    }

    public String tryGetFirstSort(IStrategoList lhs) {
        for(IStrategoTerm current : lhs.getAllSubterms()) {
            String sort = tryGetSort((IStrategoAppl) current);

            if(sort != null)
                return sort;
        }

        return null;
    }

    private String tryGetSort(IStrategoAppl appl) {
        IStrategoConstructor cons = appl.getConstructor();

        if("sort".equals(cons.getName()))
            return javaString(termAt(appl, 0));
        else if("cf".equals(cons.getName()) || "lex".equals(cons.getName()))
            return tryGetSort(applAt(appl, 0));
        else if("parameterized-sort".equals(cons.getName()))
            return getParameterizedSortName(appl);
        else if("char-class".equals(cons.getName()))
            return null;
        else if("alt".equals(cons.getName()))
            return getAltSortName(appl);
        else
            return null;
    }

    private static final int PARAMETRIZED_SORT_NAME = 0;
    private static final int PARAMETRIZED_SORT_ARGS = 1;

    private String getParameterizedSortName(IStrategoAppl parameterizedSort) {
        StringBuilder result = new StringBuilder();

        result.append(((IStrategoNamed) termAt(parameterizedSort, PARAMETRIZED_SORT_NAME)).getName());
        result.append('_');

        IStrategoList args = termAt(parameterizedSort, PARAMETRIZED_SORT_ARGS);

        for(IStrategoTerm arg : iterable(args)) {
            result.append(((IStrategoNamed) arg).getName());
        }

        return result.toString();
    }

    private final int ALT_SORT_LEFT = 0;
    private final int ALT_SORT_RIGHT = 1;

    private String getAltSortName(IStrategoAppl node) {
        String left = getSort(applAt(node, ALT_SORT_LEFT));
        String right = getSort(applAt(node, ALT_SORT_RIGHT));

        return left + "_" + right + "0";
    }

    private String getStartSymbolSort(IStrategoAppl lhs, IStrategoList rhs) {
        if("<START>".equals(tryGetSort(lhs))) {
            return tryGetFirstSort(rhs);
        }

        return null;
    }

    private boolean getIsLayout(IStrategoTerm lhs) {
        IStrategoTerm details = termAt(lhs, 0);

        if(!isTermAppl(details))
            return false;

        if("opt".equals(((IStrategoAppl) details).getConstructor().getName()))
            details = termAt(details, 0);

        return "layout".equals(((IStrategoAppl) details).getConstructor().getName());
    }

    private boolean getIsLayoutParent(IStrategoTerm lhs, String descriptor) {
        return getIsLayout(lhs) && "cf".equals(((IStrategoAppl) lhs).getConstructor().getName())
            && !"cf(layout) -> [cf(layout),cf(layout)]".equals(descriptor)
            && !"cf(opt(layout)) -> []".equals(descriptor);
    }

    private boolean getIsLiteral(IStrategoAppl lhs) {
        String constructorName = lhs.getConstructor().getName();

        return "lit".equals(constructorName) || "cilit".equals(constructorName);
    }

    private boolean getIsLexical(IStrategoAppl lhs) {
        String constructorName = lhs.getConstructor().getName();

        return "lex".equals(constructorName);
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

    private boolean getIsList(IStrategoAppl lhs, boolean isFlatten) {
        IStrategoConstructor constructor = getIterConstructor(lhs);

        return getIsIterFun(constructor) || "seq".equals(constructor.getName()) || isFlatten;
    }

    private IStrategoConstructor getIterConstructor(IStrategoAppl lhs) {
        IStrategoAppl details = lhs;

        if("varsym".equals(details.getConstructor().getName()))
            details = termAt(details, 0);

        if("cf".equals(details.getConstructor().getName()))
            details = termAt(details, 0);

        if("opt".equals(details.getConstructor().getName()))
            details = termAt(details, 0);

        return details.getConstructor();
    }

    private boolean getIsIterFun(IStrategoConstructor constructor) {
        String constructorName = constructor.getName();

        return "iter".equals(constructorName) || "iter-star".equals(constructorName)
            || "iter-plus".equals(constructorName) || "iter-sep".equals(constructorName)
            || "iter-star-sep".equals(constructorName) || "iter-plus-sep".equals(constructorName);
    }

    private boolean getIsOptional(IStrategoAppl lhs) {
        if("opt".equals(lhs.getConstructor().getName()))
            return true;

        IStrategoTerm contents = termAt(lhs, 0);

        return contents.getSubtermCount() == 1 && isTermAppl(contents)
            && "opt".equals(((IStrategoAppl) contents).getConstructor().getName());
    }

    private boolean getIsStringLiteral(IStrategoTerm rhs) {
        return topdownHasSpaces(rhs);
    }

    private boolean getIsNumberLiteral(IStrategoTerm rhs) {
        IStrategoTerm range = getFirstRange(rhs);

        return range != null && intAt(range, 0) == '0' && intAt(range, 1) == '9';
    }

    private boolean getIsOperator(IStrategoAppl lhs, boolean isLiteral) {
        // An operator literal is always a literal
        if(!isLiteral)
            return false;

        IStrategoString lit = termAt(lhs, 0);
        String contents = lit.stringValue();

        // Operators are literals with all characters not being letters
        for(int i = 0; i < contents.length(); i++) {
            char c = contents.charAt(i);

            if(Character.isLetter(c))
                return false;
        }

        return true;
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
