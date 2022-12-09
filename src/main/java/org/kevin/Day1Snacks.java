package org.kevin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day1Snacks {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Path.of(ClassLoader.getSystemResource("elves_cals.txt").toURI()));
        List<Long> currentSnacks = new ArrayList<>();
        long currentMax = 0;

        Comparator<Long> c = ( a, b ) -> a.compareTo(b) * -1;
        PriorityQueue<Long> totals = new PriorityQueue<>( c);
        for( String line : lines)
        {
            String sanitized = line.trim().replaceAll("\\s","");
            if(sanitized.isEmpty())
            {
                long total = currentSnacks.stream().mapToLong(Long::longValue).sum();
                totals.add(total);
                if( total > currentMax)
                {
                    System.out.println(Arrays.toString(currentSnacks.toArray()));
                    System.out.println( String.format("New max %d", total));
                    currentMax = total;
                }
                currentSnacks.clear();
            }
            else {
                currentSnacks.add(  Long.parseLong(sanitized));
            }
        }
        System.out.println(totals.poll());
        System.out.println(totals.poll());
        System.out.println(totals.poll());


    }
}