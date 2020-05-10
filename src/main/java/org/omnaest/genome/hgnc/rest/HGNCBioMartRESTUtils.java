package org.omnaest.genome.hgnc.rest;

import org.omnaest.utils.ClassUtils;
import org.omnaest.utils.ClassUtils.Resource;
import org.omnaest.utils.rest.client.RestClient;
import org.omnaest.utils.rest.client.RestClient.MediaType;

public class HGNCBioMartRESTUtils
{
    public static String fetchGeneData()
    {
        return RestClient.newStringRestClient()
                         .withContentMediaType(MediaType.APPLICATION_FORM_URL_ENCODED)
                         .request()
                         .toUrl("https://biomart.genenames.org/martservice/results")
                         .post(RestClient.formBuilder()
                                         .put("download", "true")
                                         .put("query", ClassUtils.loadResource("query.xml")
                                                                 .map(Resource::asString)
                                                                 .get())
                                         .build(),
                               String.class);
    }
}
