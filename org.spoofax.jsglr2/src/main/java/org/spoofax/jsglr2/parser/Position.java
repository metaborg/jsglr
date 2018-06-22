package org.spoofax.jsglr2.parser;

public class Position {

    final public int offset, line, column;

    public Position(int offset, int line, int column) {
        this.offset = offset;
        this.line = line;
        this.column = column;
    }
    
    public Position(Position p) {
        this.offset = p.offset;
        this.line = p.line;
        this.column = p.column;
    }

    public Position nextColumn() {
        return new Position(offset + 1, line, column + 1);
    }

    public Position nextLine() {
        return new Position(offset + 1, line + 1, 1);
    }

    public String coordinatesToString() {
        return "" + line + ":" + column;
    }
    
    @Override public String toString() {
        return "l: " + line + " c: " + column + " offset: " + offset;
    }

    @Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + line;
        result = prime * result + offset;
        return result;
    }

    @Override public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if(column != other.column)
            return false;
        if(line != other.line)
            return false;
        if(offset != other.offset)
            return false;
        return true;
    }
    

}
