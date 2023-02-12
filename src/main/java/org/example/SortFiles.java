package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SortFiles {

    private static final String SORT_ASCENDING = "-a";
    private static final String SORT_DESCENDING = "-d";
    private static final String SORT_STRINGS = "-s";
    private static final String SORT_INTEGERS = "-i";

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Not enough arguments provided.");
            return;
        }

        boolean sortAscending = true;
        boolean sortStrings = false;
        String outputFileName = "";
        List<String> inputFileNames = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case SORT_ASCENDING:
                    sortAscending = true;
                    break;
                case SORT_DESCENDING:
                    sortAscending = false;
                    break;
                case SORT_STRINGS:
                    sortStrings = true;
                    break;
                case SORT_INTEGERS:
                    sortStrings = false;
                    break;
                default:
                    if (outputFileName.isEmpty()) {
                        outputFileName = args[i];
                    } else {
                        inputFileNames.add(args[i]);
                    }
                    break;
            }
        }

        if (outputFileName.isEmpty() || inputFileNames.isEmpty()) {
            System.out.println("Not enough arguments provided or incorrect arguments.");
            return;
        }

        List<String> lines = new ArrayList<>();
//        for (int i = 0; i < inputFileNames.size(); i++){
//            String inputFileName = inputFileNames.get(i);
//        }
        for (String inputFileName : inputFileNames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + inputFileName);

            }
        }

        List<Integer> integers = new ArrayList<>();

        if (sortStrings) {
            lines = sorted(lines, sortAscending);
            printToFile(lines,outputFileName);
        } else {
            for(String s : lines) {
                try{
                    integers.add(Integer.valueOf(s));}
                catch (NumberFormatException numberFormatException){
                    System.out.println("Line is not in integer format: " + s);
                }
            }

            integers = sorted(integers,sortAscending);
            printToFile(integers,outputFileName);


        }



    }



    public static <T> void printToFile(List<T> data, String outputFileName){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (T object : data) {
                if (object instanceof  String)
                    writer.write((String) object);
                else
                    writer.write(((Integer) object).toString());

                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + outputFileName);
        }
    }


    public static <T> List<T> sorted(List<T> list, boolean asc) {
        if (list.size() < 2) {
            return list;
        }

        int mid = list.size() / 2;
        return merged(
                sorted(list.subList(0, mid), asc),
                sorted(list.subList(mid, list.size()),asc),asc);
    }

    private static  <T> List<T> merged(List<T> left, List<T> right, boolean asc) {
        int leftIndex = 0;
        int rightIndex = 0;
        List<T> merged = new ArrayList<>();

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex) instanceof String){
                if (asc) {
                    if ( ((String) left.get(leftIndex)).compareTo ((String) right.get(rightIndex))>0) {
                        merged.add(left.get(leftIndex++));
                    } else {
                        merged.add(right.get(rightIndex++));
                    }
                }else {
                    if (((String) left.get(leftIndex)).compareTo ((String) right.get(rightIndex))<0) {
                        merged.add(left.get(leftIndex++));
                    } else {
                        merged.add(right.get(rightIndex++));
                    }
                }
            }else {
                if (asc) {
                    if ((int) left.get(leftIndex) < (int) right.get(rightIndex)) {
                        merged.add(left.get(leftIndex++));
                    } else {
                        merged.add(right.get(rightIndex++));
                    }
                }else {
                    if ((int) left.get(leftIndex) > (int) right.get(rightIndex)) {
                        merged.add(left.get(leftIndex++));
                    } else {
                        merged.add(right.get(rightIndex++));
                    }
                }
            }

        }
        merged.addAll(left.subList(leftIndex, left.size()));
        merged.addAll(right.subList(rightIndex, right.size()));
        return merged;
    }

}


