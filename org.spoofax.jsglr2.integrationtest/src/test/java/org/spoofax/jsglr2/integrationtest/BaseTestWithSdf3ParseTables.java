package org.spoofax.jsglr2.integrationtest;

import org.junit.BeforeClass;
import org.metaborg.core.MetaborgException;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.JSGLR2Variants.ParseTableVariant;
import org.spoofax.jsglr2.tests.util.BaseTest;

public abstract class BaseTestWithSdf3ParseTables extends BaseTest {
    
    private String sdf3Resource;
    
    BaseTestWithSdf3ParseTables(String sdf3Resource) {
        this.sdf3Resource = sdf3Resource;
    }

    private static Sdf3ToParseTable sdf3ToParseTable;
    
    @BeforeClass
    public static void setup() throws MetaborgException {
        sdf3ToParseTable = new Sdf3ToParseTable();
    }

    public IParseTable getParseTable(ParseTableVariant variant) throws Exception {
        return sdf3ToParseTable.getParseTable(variant, sdf3Resource);
    }

}