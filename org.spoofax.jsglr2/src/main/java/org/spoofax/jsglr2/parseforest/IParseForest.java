package org.spoofax.jsglr2.parseforest;

public interface IParseForest {

    int width();

    String descriptor();

    static int sumWidth(IParseForest... parseForests) {
        int width = 0;
        for(IParseForest parseForest : parseForests) {
            if(parseForest == null)
                continue; // Skippable parse nodes can be null
            width += parseForest.width();
        }
        return width;
    }

}
