package org.spoofax.jsglr2.integrationtest.features;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.OriginDescriptor;
import org.spoofax.terms.ParseError;

public class OriginsTest extends BaseTestWithSdf3ParseTables {

    public OriginsTest() {
        super("tokenization.sdf3");
    }

    @TestFactory public Stream<DynamicTest> operator() throws ParseError {
        return testOrigins("x+x", Arrays.asList(
        //@formatter:off
            new OriginDescriptor("AddOperator", 0, 2),
            new OriginDescriptor("Id", 0, 0),
            new OriginDescriptor("Id", 2, 2)
        //@formatter:on
        ));
    }

}
