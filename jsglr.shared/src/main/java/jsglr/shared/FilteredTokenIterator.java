package jsglr.shared;

import static jsglr.shared.IToken.Kind.TK_EOF;
import static jsglr.shared.IToken.Kind.TK_RESERVED;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This iterator filters out:
 * <ul>
 * <li>Tokens coming from ambiguous regions in the AST (only the first derivation is kept)
 * <li>Empty tokens
 * </ul>
 */
public class FilteredTokenIterator implements Iterator<IToken> {
    final Iterator<IToken> allTokensIterator;
    IToken next = null;
    int offset = -1;

    public FilteredTokenIterator(Iterable<IToken> allTokens) {
        allTokensIterator = allTokens.iterator();
        calculateNext();
    }

    @Override public boolean hasNext() {
        return next != null;
    }

    @Override public IToken next() {
        if(!hasNext())
            throw new NoSuchElementException();

        IToken res = next;
        calculateNext();
        return res;
    }

    private void calculateNext() {
        while(allTokensIterator.hasNext()) {
            next = allTokensIterator.next();
            if(next.getKind() != TK_RESERVED && next.getKind() != TK_EOF
                && next.getStartOffset() == next.getEndOffset() + 1)
                continue; // Skip empty tokens (that are not the start or end token)
            if(next.getStartOffset() > offset) {
                offset = next.getEndOffset();
                return;
            }
        }
        next = null;
    }
}