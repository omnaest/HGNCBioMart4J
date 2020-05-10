package org.omnaest.genome.hgnc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.io.IOUtils;
import org.omnaest.genome.hgnc.rest.HGNCBioMartRESTUtils;
import org.omnaest.utils.ClassUtils;
import org.omnaest.utils.table.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HGNCUtils
{
    public static interface HGNCData
    {
        public Stream<String> getGeneSymbols();

        public Stream<Gene> getGenes();
    }

    public static interface Gene
    {
        public String getSymbol();

        public String getEnsemblGeneId();

        public String getUniprotAccessionId();

        public String getEnzymeCode();

        public String getPubMedId();

        public String getName();
    }

    public static HGNCData loadFrom(File file) throws FileNotFoundException, IOException
    {
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file)))
        {
            return loadFrom(inputStream);
        }
    }

    public static HGNCData loadFrom(String data)
    {
        try
        {
            return loadFrom(IOUtils.toInputStream(data, StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    public static HGNCData loadFrom(InputStream inputStream) throws IOException
    {
        Table table = Table.newInstance()
                           .deserialize()
                           .fromCsv(parser -> parser.from(inputStream)
                                                    .withFormat(CSVFormat.TDF.withFirstRecordAsHeader())
                                                    .enableStreaming());

        return new HGNCData()
        {
            @Override
            public Stream<Gene> getGenes()
            {
                return table.stream()
                            .map(row -> row.asBean(GeneImpl.class));
            }

            @Override
            public Stream<String> getGeneSymbols()
            {
                return this.getGenes()
                           .map(gene -> gene.getSymbol());
            }
        };
    }

    public static HGNCData loadDefault()
    {
        try
        {
            return loadFrom(ClassUtils.loadResource(HGNCUtils.class, "/genes.tsv")
                                      .map(r -> r.asInputStream())
                                      .get());
        }
        catch (IOException e)
        {
            throw new IllegalStateException(e);
        }
    }

    public static HGNCData loadDefaultByREST()
    {
        return loadFrom(HGNCBioMartRESTUtils.fetchGeneData());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GeneImpl implements Gene
    {
        @JsonProperty("Approved symbol")
        private String symbol;

        @JsonProperty("Approved name")
        private String name;

        @JsonProperty("PubMed ID")
        private String pubMedId;

        @JsonProperty("Enzyme (EC) ID")
        private String enzymeCode;

        @JsonProperty("UniProt accession")
        private String uniprotAccessionId;

        @JsonProperty("Ensembl gene ID")
        private String ensemblGeneId;

        @Override
        public String getSymbol()
        {
            return this.symbol;
        }

        @Override
        public String getName()
        {
            return this.name;
        }

        @Override
        public String getPubMedId()
        {
            return this.pubMedId;
        }

        @Override
        public String getEnzymeCode()
        {
            return this.enzymeCode;
        }

        @Override
        public String getUniprotAccessionId()
        {
            return this.uniprotAccessionId;
        }

        @Override
        public String getEnsemblGeneId()
        {
            return this.ensemblGeneId;
        }

        @Override
        public String toString()
        {
            return "GeneImpl [symbol=" + this.symbol + ", name=" + this.name + ", pubMedId=" + this.pubMedId + ", enzymeCode=" + this.enzymeCode
                    + ", uniprotAccessionId=" + this.uniprotAccessionId + ", ensemblGeneId=" + this.ensemblGeneId + "]";
        }

    }

}
