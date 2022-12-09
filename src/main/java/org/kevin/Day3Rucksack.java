package org.kevin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Day3Rucksack {


    public Day3Rucksack() {

    }

    public void readInput() throws Exception {
        List<String> input = Files.readAllLines(Path.of(ClassLoader.getSystemResource("mochilla.txt").toURI()));

        System.out.println(input.stream().map(s -> s.trim()).mapToInt(line -> parseLine(line)).sum());
        Supplier<List<List<String>>> supplier = () -> new ArrayList<>();
        List<List<String>> result = input.stream().collect(supplier, (accumulator, current) -> {
            List<String> currentSubList = accumulator.get(accumulator.size() - 1);
            if (currentSubList.size() == 3) {
                List<String> newSublist = new ArrayList<>();
                accumulator.add(newSublist);
                currentSubList = newSublist;
            }
            currentSubList.add(current);
        }, (acc, acc2) -> {
            acc.addAll(acc2);
        });
    }


    public void readInput2() throws IOException {
        List<String> input = Files.readAllLines(Path.of("/Users/kevin.oriordan/Documents/mochilla.txt"));

        Supplier<List<List<String>>> supplier = () -> {
            List<List<String>> res = new ArrayList<>();
            res.add(new ArrayList<String>());
            return res;
        };
        List<List<String>> result = input.stream().map(s -> s.trim()).collect(supplier, (accumulator, current) -> {
            List<String> currentSubList = accumulator.get(accumulator.size() - 1);
            if (currentSubList.size() == 3) {
                List<String> newSublist = new ArrayList<>();
                accumulator.add(newSublist);
                currentSubList = newSublist;
            }
            currentSubList.add(current);
        }, (acc, acc2) -> {
            acc.addAll(acc2);
        });

        int res = result.stream().mapToInt(triple -> getCommonCharPriority(triple)).sum();
        System.out.println(res);
    }

    private int getCommonCharPriority(List<String> triple) {
        char[] firstChars = triple.get(0).toCharArray();
        char[] secondChars = triple.get(1).toCharArray();
        char[] thirdChars = triple.get(2).toCharArray();
        Arrays.sort(firstChars);
        Arrays.sort(secondChars);
        Arrays.sort(thirdChars);
        char[] firstIntersection = findALLCommonChars(firstChars, secondChars);
        Arrays.sort(firstIntersection);
        char commonChar = findFirstCommonChar(firstIntersection, thirdChars);
        int result = calculatePriority(commonChar);
        System.out.println(String.format("Common char %s priority %d", commonChar, result));
        return result;
    }

    private int parseLine(String lineTrimmed) {

        if (lineTrimmed.length() % 2 != 0)
            throw new RuntimeException("Unexpected length");
        int splitAt = lineTrimmed.length() / 2;
        String first = lineTrimmed.substring(0, splitAt);
        String second = lineTrimmed.substring(splitAt);
        char[] firstChars = first.toCharArray();
        char[] secondChars = second.toCharArray();
        Arrays.sort(firstChars);
        Arrays.sort(secondChars);
        char commonChar = findFirstCommonChar(firstChars, secondChars);
        int result = calculatePriority(commonChar);
        System.out.println(String.format("Common char %s priority %d", commonChar, result));
        return result;
    }

    private int calculatePriority(char commonChar) {
        return Character.isLowerCase(commonChar) ? commonChar - 'a' + 1 : commonChar - 'A' + 27;
    }

    /* Assumes sorted */
    private char findFirstCommonChar(char[] firstChars, char[] secondChars) {
        int firstIndex = 0;
        int secondIndex = 0;
        while (true) {
            char firstChar = firstChars[firstIndex];
            char secondChar = secondChars[secondIndex];
            if (firstChar == secondChar) {
                return firstChar;
            } else if (firstChar < secondChar) {
                firstIndex++;
            } else {
                secondIndex++;
            }
        }
    }


    /* Assumes sorted */
    private char[] findALLCommonChars(char[] firstChars, char[] secondChars) {
        int firstIndex = 0;
        int secondIndex = 0;
        StringBuffer buffer = new StringBuffer();
        while (true) {
            char firstChar = firstChars[firstIndex];
            char secondChar = secondChars[secondIndex];
            if (firstChar == secondChar) {
                buffer.append(firstChar);
                firstIndex++;
                secondIndex++;

            } else if (firstChar < secondChar) {
                firstIndex++;
            } else {
                secondIndex++;
            }
            if (firstIndex == firstChars.length || secondIndex == secondChars.length)
                break;
        }
        return buffer.toString().toCharArray();
    }

    private int bumpIfPoss(int firstIndex, int length) {
        return (firstIndex == length - 1 ? firstIndex : ++firstIndex);
    }

    public static void main(String[] args) throws IOException {
        new Day3Rucksack().readInput2();
    }
}
