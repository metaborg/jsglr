package org.spoofax.jsglr2.util;

public class TreePrettyPrinter {

    private int indent;
    private StringBuilder builder;

    public TreePrettyPrinter() {
        this.indent = 0;
        this.builder = new StringBuilder();
    }

    public void indent(int delta) {
        assert (indent + delta >= 0);

        indent += delta;
    }

    public void print(String s) {
        print(s, true);
    }

    public void print(String s, boolean indent) {
        printIndent(indent);
        builder.append(s);
    }

    public void println(String s) {
        println(s, true);
    }

    public void println(String s, boolean indent) {
        print(s, indent);
        builder.append('\n');
    }

    private void printIndent(boolean printIndent) {
        if(printIndent) {
            for(int i = 0; i < indent; i++)
                builder.append(' ');
        }
    }

    public String get() {
        return builder.toString();
    }

}
