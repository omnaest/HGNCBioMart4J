package org.omnaest.genome.hgnc.rest;

import org.junit.Ignore;
import org.junit.Test;

public class HGNCBioMartRESTUtilsTest
{

    @Test
    @Ignore
    public void testFetchGeneData() throws Exception
    {
        String data = HGNCBioMartRESTUtils.fetchGeneData();
        System.out.println(data);
    }

}
