package org.spoofax.jsglr2.parser;

public class PositionInterval {

    private Position start;
    private Position end;
    
    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public void setStart(Position start) {
        this.start = start;
    }

    public void setEnd(Position end) {
        this.end = end;
    }

    public PositionInterval(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    @Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }

    @Override public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        PositionInterval other = (PositionInterval) obj;
        if(end == null) {
            if(other.end != null)
                return false;
        } else if(!end.equals(other.end))
            return false;
        if(start == null) {
            if(other.start != null)
                return false;
        } else if(!start.equals(other.start))
            return false;
        return true;
    }
    
    @Override public String toString() {
        // TODO Auto-generated method stub
        return start + ", " + end;
    }
}
