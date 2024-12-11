package code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import code.util.TextFile;

public class Day2 {
    public static void main(String[] args) throws IOException {
        TextFile file = new TextFile("./inputs/inputs-day2.txt", "r");
        ArrayList<int[]> rows = parseRows(file);
        file.close();

        part1(rows);
        part2(rows);
    }

    private static ArrayList<int[]> parseRows(TextFile file) {
        ArrayList<int[]> records = new ArrayList<>();

        String line = file.readLineSafe();

        while (line != null && line != "") {
            String[] numbers = line.split("\s+");
            int[] record = new int[numbers.length];

            Arrays.setAll(record, n->Integer.parseInt(numbers[n]));
            records.add(record);
            line = file.readLineSafe();
        }

        return records;
    }

    private static void part1(ArrayList<int[]> records) {
        int safeReports = 0;

        for (int[] record : records) {
            if (recordIsSafe(record))
                safeReports++;
        }

        System.out.println("Safe reports: " + safeReports);
    }

    private static boolean recordIsSafe(int[] record) {
        boolean unsafe = false;
        int direction = 0;

        for (int i = 1; i < record.length; i++) {
            int curSample = record[i];
            int lastSample = record[i-1];

            if (direction == 0)
                direction = lastSample < curSample ? 1 : -1;

            // Should probably clean this up but I don't care to right now.
            int delta = curSample - lastSample;

            // Ensure its within the specified safety range.
            if (!(delta != 0 && signof(delta) == signof(direction) && Math.abs(delta) <= 3))
                unsafe = true;
        }

        return !unsafe;
    }

    private static void part2(ArrayList<int[]> records) {
        int safeReports = 0;

        for (int[] record : records) {
            if (recordIsSafe(record) || recordIsSafeProblemDampener(record)) {
                System.out.println("safe record");
                safeReports++;
            }

            else
                System.out.println("id unsafe record");
        }

        System.out.println("Safe reports: " + safeReports);
    }

    // Potentially unsafe record -- identify if the "Problem Dampener" can handle it
    // Could implement into the *recordIsSafe* logic but I'll do it separately.
    // It's basically the same code anyway.
    // TODO: Incomplete! I have lost what little remains of my brain power.
    private static boolean recordIsSafeProblemDampener(int[] record) {
        boolean unsafe = false;
        int direction = 0;

        System.out.println(Arrays.toString(record));
        ArrayList<Integer> fixedList = new ArrayList<>();

        for (int i = 1; i < record.length - 1; i++) {
            int curSample = record[i];
            int forwardSample = record[i+1];
            int lastSample = record[i-1];

            if (direction == 0)
                direction = lastSample < curSample ? 1 : -1;

            int sampleDir = lastSample < curSample ? 1 : -1;
            int forwardSampleDir = lastSample < forwardSample ? 1 : -1;

            int delta = curSample - lastSample;
            int forwardDelta = forwardSample - lastSample;

            boolean inRange = (signof(delta) == signof(direction) && Math.abs(delta) <= 3);

            // Attempt to remove an entry, then evaluate it again.

            // Personal heuristics I found: in most cases as long as we're not on the edge,
            // we can only remove a direction reversal.
            if (sampleDir != forwardSampleDir) {}
            else
                fixedList.add(curSample);
        }

        return recordIsSafe(toPrimitiveIntArray(fixedList));
    }

    private static int[] toPrimitiveIntArray(ArrayList<Integer> arr) {
        int[] outArr = new int[arr.size()];
        Arrays.setAll(outArr, n->arr.get(n));
        return outArr;
    }

    private static int signof(int num) {
        // Could just do a comparison operator or some bit manipulation but nah.
        return Math.abs(num) == num ? 1 : -1;
    }
}
