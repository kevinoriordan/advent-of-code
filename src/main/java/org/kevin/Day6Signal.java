package org.kevin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day6Signal {

    public static  void main(String[] args) throws Exception {

        List<String> lines = Files.readAllLines(Path.of(ClassLoader.getSystemResource("signal.txt").toURI()));
        lines.stream().mapToInt( line -> getStartIndex(line)).forEach( index -> System.out.print(index));
    }

    private static int getStartIndex(String line) {
        int startIndex = 0, endIndexEx = 14;
        Deque<Character> test = new ArrayDeque<>();
        char[] all = line.toCharArray();
        for( int i= 0 ; i < 14; i++)
        {
            test.addLast(all[i]);
        }
        while( !allUnique(test))
        {
            endIndexEx++;
            test.removeFirst();
            test.add( all[endIndexEx -1]);
        }
        return endIndexEx;

    }

    private static boolean allUnique(Deque<Character> test) {
        Set<Character> set = new HashSet<>();
        test.forEach( c -> set.add(c));
        return set.size() == 14;
    }

}
