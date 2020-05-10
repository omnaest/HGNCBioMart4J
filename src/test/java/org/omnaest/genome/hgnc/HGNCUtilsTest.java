package org.omnaest.genome.hgnc;

import org.junit.Test;

public class HGNCUtilsTest
{

    @Test
    public void testLoadDefaultFromInputStream() throws Exception
    {
        HGNCUtils.loadDefaultByREST()
                 .getGeneSymbols()
                 .forEach(System.out::println);
        ;
    }

}
