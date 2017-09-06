package org.spoofax.jsglr2.tests.util.iterators;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.spoofax.jsglr2.util.iterators.SingleElementIterable;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class SingleElementIteratorsTest {

    private String concat(Iterable<String> iterator) {
        String result = "";
        
        for (String element : iterator) {
            result += element;
        }
        
        return result;
    }
    
    @Test
    public void testSingleElement() {
        Iterable<String> singleElementIterable = new SingleElementIterable<String>("a"); 
        
        assertEquals(concat(singleElementIterable), "a");
    }
    
    @Test
    public void testSingleElementWithEmptyList() {
        Iterable<String> singleElementWithListIterable = new SingleElementWithListIterable<String>("a", Arrays.asList()); 
        
        assertEquals(concat(singleElementWithListIterable), "a");
    }
    
    @Test
    public void testSingleElementWithSingleElementList() {
        Iterable<String> singleElementWithListIterable = new SingleElementWithListIterable<String>("a", Arrays.asList("b")); 
        
        assertEquals(concat(singleElementWithListIterable), "ab");
    }
    
    @Test
    public void testSingleElementWithMultipleElementList() {
        Iterable<String> singleElementWith2ListIterable = new SingleElementWithListIterable<String>("a", Arrays.asList("b", "c"));
        Iterable<String> singleElementWith3ListIterable = new SingleElementWithListIterable<String>("a", Arrays.asList("b", "c", "d")); 

        assertEquals(concat(singleElementWith2ListIterable), "abc");
        assertEquals(concat(singleElementWith3ListIterable), "abcd");
    }
	
}
