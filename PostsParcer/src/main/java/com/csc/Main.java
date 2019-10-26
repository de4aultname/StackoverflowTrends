package com.csc;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //parseRawData();
        parseToVW();
        //serializeTags();
        //createTagsFrequenciesCSV();
    }

    private static void createTagsFrequenciesCSV() {
        List<String> headers = new ArrayList<>() {{
            add("tag");
            add("count");
        }};
        try {
            FileWriter fileWriter = new FileWriter("tags.csv");
            String[] headersArr = headers.toArray(new String[0]);
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter,
                    CSVFormat.DEFAULT.withHeader(headersArr));
            Map<String, Integer> tagsFrequencies = TagsFrequenciesSerializer.deserialize();
            for (Map.Entry<String, Integer> entry : tagsFrequencies.entrySet()) {
                csvPrinter.print(entry.getKey());
                csvPrinter.print(entry.getValue());
                csvPrinter.println();
                csvPrinter.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while creating output file: " + e.getMessage());
        }
    }

    private static void serializeTags() {
        TagsFrequenciesSerializer serializer = new TagsFrequenciesSerializer("Posts.xml");
        serializer.serialize();
    }

    private static void parseToVW() {
        VWConverter vwConverter = new VWConverter("DynamicPostTagsUngathered.csv");
        List<String> colsToRetrieve = new ArrayList<>() {{
            add("Tags");
        }};
        vwConverter.convertToVW(colsToRetrieve, "vw.tags.100.txt", "post");
    }

    private static void parseRawData() {
        Parser parser = new Parser("Posts.xml");
        String entryType = "posts";
        parser.setTagFrequencyThreshold(100);
        List<String> cols = new ArrayList<>() {{
            add("Id");
            add("CreationDate");
            add("Score");
            add("OwnerUserId");
            add("Tags");
        }};
        parser.parseToCSV(entryType, cols, "DynamicPostTagsUngathered.csv");
    }
}

