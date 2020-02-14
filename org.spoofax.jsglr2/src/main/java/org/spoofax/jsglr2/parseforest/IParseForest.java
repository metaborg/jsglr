package org.spoofax.jsglr2.parseforest;

public interface IParseForest {

    /** The width of this parse forest, in number of <b>characters</b> (not number of code points). */
    int width();

    String descriptor();

    static int sumWidth(IParseForest... parseForests) {
        int width = 0;
        for(IParseForest parseForest : parseForests) {
            width += parseForest.width();
        }
        return width;
    }

}
