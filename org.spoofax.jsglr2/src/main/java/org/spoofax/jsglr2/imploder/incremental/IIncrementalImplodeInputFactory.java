package org.spoofax.jsglr2.imploder.incremental;

import java.util.WeakHashMap;

import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.parseforest.IParseNode;

public interface IIncrementalImplodeInputFactory<ParseNode extends IParseNode, Tree, Input extends IncrementalImplodeInput<ParseNode, Tree>> {

    Input get(String inputString, WeakHashMap<ParseNode, TreeImploder.SubTree<Tree>> cache);

}
