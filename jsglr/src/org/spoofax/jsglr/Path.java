/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import aterm.ATerm;

public class Path<T> implements Iterable<List<T>> {

    private List<List<T>> lists;
    
    public Path() {
        lists = new Vector<List<T>>();
        lists.add(new Vector<T>());
    }
  

    public void add(T t) {
        for(List<T> l : lists) {
            l.add(t);
        }
    }

   private List<List<T>> copy() {
        List<List<T>> newList = new Vector<List<T>>(lists.size());
        for(List<T> e : lists) {
            newList.add(e);
        }
        return newList;
    }

    public Iterator<List<T>> iterator() {
        return new Iterator<List<T>>() {
            private Iterator<List<T>> localIter = lists.iterator(); 
            public boolean hasNext() {
                return localIter.hasNext();
            }

            public List<T> next() {
                return localIter.next();
            }

            public void remove() {
               localIter.remove();
            }
        };
    }


    public void add(Path<T> p) {
        List<List<T>> newList = new Vector<List<T>>();
        
        for(List<T> ls : p) {
            List<List<T>> n = copy();
            for(List<T> ls2 : n) 
                ls2.addAll(ls);
            newList.addAll(n);
        }
    }


    public static List<ATerm> collectTerms(List<Link> path) {
        List<ATerm> ret = new Vector<ATerm>(path.size());
        for(Link ln : path)
            ret.add(ln.label);
        return ret;
    }
    
    
}
