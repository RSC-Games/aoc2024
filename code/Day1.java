package code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import code.util.TextFile;

public class Day1 {

    public static void main(String[] args) throws IOException {
        TextFile inputs = new TextFile("./inputs/inputs-day1.txt", "r");
        ArrayList<Integer[]> lists = parseLists(inputs);
        inputs.close();

        Integer[] l1 = lists.get(0);
        Integer[] l2 = lists.get(1);

        part1(l1, l2);
        part2(l1, l2);
    }

    public static void part1(Integer[] l1, Integer[] l2) {
        // Actual puzzle starts here.
        // Comparing diffs:
        int diff = 0;

        // I'll need to sort both of these... Can I do it without converting them to primitives?
        // Yep... thanks stdlib.
        Arrays.sort(l1);
        Arrays.sort(l2);

        for (int i = 0; i < l1.length; i++) {
            diff += Math.abs(l2[i] - l1[i]);
        }

        System.out.println("Calculated diff: " + diff);
    }

    private static ArrayList<Integer[]> parseLists(TextFile inputs) {
        ArrayList<Integer> l1 = new ArrayList<>();
        ArrayList<Integer> l2 = new ArrayList<>();

        String line = inputs.readLineSafe();

        while (line != null && line != "") {
            String[] numbers = line.split("\s+");

            l1.add(Integer.parseInt(numbers[0]));
            l2.add(Integer.parseInt(numbers[1]));

            line = inputs.readLineSafe();
        }

        // I hate how much I have to do to get these arrays out.
        // Should have used C++ or Python.
        ArrayList<Integer[]> parsedLists = new ArrayList<Integer[]>();

        parsedLists.add((Integer[])l1.toArray(new Integer[l1.size()]));
        parsedLists.add((Integer[])l2.toArray(new Integer[l2.size()]));

        return parsedLists;
    }

    private static void part2(Integer[] l1, Integer[] l2) {
        // I'll use a naive algorithm for finding the quantity of each number.
        // I could probably use binary search to locate the value since the arrays
        // are sorted. Then, I could walk in both directions until each end is no
        // longer the same number.
        // I'll actually do this algorithm since I don't want to spend the time
        // writing and debugging a (probably easier) exhaustive algorithm.
        int similarity = 0;

        for (int i = 0; i < l1.length; i++) {
            int searchNum = l1[i];

            // Find any particular instance of this value for the counting algorithm.
            int idx = Arrays.binarySearch(l2, searchNum);

            // No matches found.
            if (idx < 0)
                continue;
            
            // Identify the occurrences.
            int downIdx = idx;
            int upIdx = idx;

            while (downIdx > 0 && l2[downIdx] == searchNum) downIdx--;
            while (upIdx < l2.length && l2[upIdx] == searchNum) upIdx++;

            // Flaws in my implementation allow the ranges to be one above in some cases.
            if (l2[downIdx] != searchNum) downIdx++;

            int qty = (upIdx - downIdx);

            similarity += qty * searchNum;
        }

        System.out.println("Similarity score: " + similarity);
    }
}