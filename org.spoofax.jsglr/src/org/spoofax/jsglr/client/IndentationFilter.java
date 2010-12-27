package org.spoofax.jsglr.client;

import static org.spoofax.jsglr.client.Term.termAt;

import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermAppl;
import org.spoofax.jsglr.shared.terms.ATermList;

public class IndentationFilter {

    public static void resolveAmbiguitiesByIndentation(ATerm node)
    {
        if (node.getChildCount()>1)
        {
            ATermList contents;
            if ("amb".equals(((ATermAppl) node).getName())){
                contents = termAt(node, 0);
            }
            else{
                contents = termAt(node, 1);
            }
            for (int i = 0; i < contents.getChildCount(); i++) {
                resolveAmbiguitiesByIndentation((ATerm) contents.getChildAt(i));
            }
        }

        if ("amb".equals(((ATermAppl) node).getName())){
            ATermList ambs = termAt(node, 0);
            node = (ATerm) ambs.getChildAt(0);
        }
    }

}
