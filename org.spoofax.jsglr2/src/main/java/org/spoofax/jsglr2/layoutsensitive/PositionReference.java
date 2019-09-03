package org.spoofax.jsglr2.layoutsensitive;

import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.PositionInterval;

public class PositionReference {

    int addr; // address of the variable

    public PositionReference(int pos, PositionInterval v, Parse<?, ?, ?> parse) {
        parse.longestMatchPos.put(pos, v);
        addr = pos;
    }

    public PositionReference(int newLocation, int pointsTo, Parse<?, ?, ?> parse) {
        parse.longestMatchPos.put(newLocation, pointsTo);
        addr = newLocation;
    }

    public PositionInterval getValue(Parse<?, ?, ?> parse) {
        Object result = parse.longestMatchPos.get(addr);
        while(!(result instanceof PositionInterval)) {
            result = parse.longestMatchPos.get(result);
        }
        return (PositionInterval) result;
    }

    public void updatePosition(PositionInterval newValue, Parse<LayoutSensitiveParseForest, ?, ?> parse) {
        parse.longestMatchPos.put(addr, newValue);
    }
}
