package org.spoofax.jsglr2.stack;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.paths.EmptyStackPath;
import org.spoofax.jsglr2.stack.paths.NonEmptyStackPath;
import org.spoofax.jsglr2.stack.paths.StackPath;

public abstract class StackManager<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>> {

    public abstract StackNode createInitialStackNode(Parse<ParseForest, StackNode> parse, IState state);

    public abstract StackNode createStackNode(Parse<ParseForest, StackNode> parse, IState state);

    public abstract StackLink<ParseForest, StackNode> createStackLink(Parse<ParseForest, StackNode> parse,
        StackNode from, StackNode to, ParseForest parseNode);

    public void rejectStackLink(Parse<ParseForest, StackNode> parse, StackLink<ParseForest, StackNode> link) {
        link.reject();

        parse.observing.notify(observer -> observer.rejectStackLink(link));
    }

    public StackLink<ParseForest, StackNode> findDirectLink(StackNode from, StackNode to) {
        for(StackLink<ParseForest, StackNode> link : stackLinksOut(from)) {
            if(link.to == to)
                return link;
        }

        return null;
    }

    public List<StackPath<ParseForest, StackNode>> findAllPathsOfLength(StackNode stack, int length) {
        List<StackPath<ParseForest, StackNode>> paths = new ArrayList<StackPath<ParseForest, StackNode>>();

        StackPath<ParseForest, StackNode> pathsOrigin = new EmptyStackPath<ParseForest, StackNode>(stack);

        findAllPathsOfLength(pathsOrigin, length, paths);

        return paths;
    }

    private void findAllPathsOfLength(StackPath<ParseForest, StackNode> path, int length,
        List<StackPath<ParseForest, StackNode>> paths) {
        if(length == 0)
            paths.add(path);
        else {
            StackNode lastStackNode = path.head();

            for(StackLink<ParseForest, StackNode> linkOut : stackLinksOut(lastStackNode)) {
                StackPath<ParseForest, StackNode> extendedPath =
                    new NonEmptyStackPath<ParseForest, StackNode>(linkOut, path);

                findAllPathsOfLength(extendedPath, length - 1, paths);
            }
        }
    }

    protected abstract Iterable<StackLink<ParseForest, StackNode>> stackLinksOut(StackNode stack);

    public ParseForest[] getParseForests(ParseForestManager<ParseForest, ?, ?> parseForestManager,
        StackPath<ParseForest, StackNode> pathBegin) {
        ParseForest[] res = parseForestManager.parseForestsArray(pathBegin.length);

        if(res != null) {
            StackPath<ParseForest, StackNode> path = pathBegin;

            for(int i = 0; i < pathBegin.length; i++) {
                NonEmptyStackPath<ParseForest, StackNode> nonEmptyPath =
                    (NonEmptyStackPath<ParseForest, StackNode>) path;

                res[i] = nonEmptyPath.link.parseForest;

                path = nonEmptyPath.tail;
            }

            return res;
        }

        return null;
    }

}
