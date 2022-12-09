package org.kevin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5MoveCrates {

    public static class Move {
        public final int from;
        public final int to;
        public final int qty;

        public Move(int from, int to, int qty) {
            this.from = from;
            this.to = to;
            this.qty = qty;
        }
    }


    private static List<Optional<Character>> parseRow(String row) {
        List<Optional<Character>> result = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            String entry = row.substring(i * 4, i * 4 + 3);
            String cleaned = entry.replaceAll("\\s", "");
            if (cleaned.isEmpty()) {
                result.add(Optional.empty());
            } else {
                result.add(Optional.of(Character.valueOf(cleaned.charAt(1))));
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Path.of(ClassLoader.getSystemResource("ship.txt").toURI()));
        List<List<Optional<Character>>> loads = lines.subList(0, 8).stream().map(row -> parseRow(row)).collect(Collectors.toList());
        loads.stream().forEach(res -> System.out.println(res));
        Collections.reverse(loads);
        List<Deque<Character>> cols = IntStream.range(0, 9).mapToObj(x -> (Deque<Character>) new ArrayDeque<Character>()).collect(Collectors.toList());
        loads.forEach(load -> addLoadToCols(load, cols));

        List<Move> moves = lines.subList(10, lines.size()).stream().map(line -> parseMove(line)).collect(Collectors.toList());
        moves.stream().forEach( move -> applyMove(move, cols ));

        cols.forEach( col -> System.out.print( col.peekLast()));

    }

    private static void applyMove(Move move, List<Deque<Character>> cols) {
        Deque<Character> from = cols.get( move.from -1);
        Deque<Character> to = cols.get( move.to -1);
        Deque<Character> forMove = new ArrayDeque<>();
        for(int i = 0 ; i < move.qty; i ++)
        {
            forMove.add( from.removeLast());
        }
        while(!forMove.isEmpty())
        {
            to.add(forMove.removeLast());
        }
    }

    private static Move parseMove(String line) {
        String[] tokens = line.split( "\\s");
        int qty = Integer.parseInt(tokens[1]);
        int from = Integer.parseInt(tokens[3]);
        int to = Integer.parseInt(tokens[5]);
        return new Move(from,to,qty);
    }

    private static void addLoadToCols(List<Optional<Character>> load, List<Deque<Character>> cols) {
        for (int i = 0; i < 9; i++) {
            Deque<Character> col = cols.get(i);
            load.get(i).ifPresent(c -> col.addLast(c));
        }
    }

}
