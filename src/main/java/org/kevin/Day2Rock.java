package org.kevin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class Day2Rock {
    public enum Result {
        LOSS(0), DRAW(3), WIN(6);
        public int score;

        Result(int score) {
            this.score = score;
        }
    }

    public enum Weapon {
        ROCK(1, "A", "X"), PAPER(2, "B", "Y"), SCISSORS(3, "C", "Z");
        public final int score;
        public final String player1Code;
        public final String player2Code;

        Weapon(int score, String player1Code, String player2Code) {
            this.score = score;
            this.player1Code = player1Code;
            this.player2Code = player2Code;
        }

    }

    public static Integer toScore(String input) {
        String[] codes = input.trim().split("\\s");
        String player1Code = codes[0];
        String player2Code = codes[1];
        Weapon player1Weapn = mapPlayer1CodeToWeapon(player1Code);
      //  Weapon player2Weapn = mapPlayer2CodeToWeapon(player2Code);
        Result result = mapPlayer2CodeToResult(player2Code);
       // int score = innerToScore(player1Weapn, player2Weapn);

        int score = innerToScore(player1Weapn, result);
        System.out.println(score);
        return score;
    }

    private static Result mapPlayer2CodeToResult(String code) {
        if (Weapon.ROCK.player2Code.equals(code)) {
            return Result.LOSS;
        } else if (Weapon.PAPER.player2Code.equals(code)) {
            return Result.DRAW;
        } else if (Weapon.SCISSORS.player2Code.equals(code)) {
            return Result.WIN;
        } else {
            throw new RuntimeException("Unknown weapon" + code);
        }
    }

    private static Integer innerToScore(Weapon player1Weapn, Weapon player2Weapn) {
        if (player1Weapn.equals(player2Weapn)) {
            return player2Weapn.score + Result.DRAW.score;
        } else {
            switch (player2Weapn) {
                case ROCK:
                    switch (player1Weapn) {
                        case SCISSORS:
                            return player2Weapn.score + Result.WIN.score;
                        case PAPER:
                            return player2Weapn.score + Result.LOSS.score;
                        default:
                            throw new RuntimeException("should not get here");
                    }
                case PAPER:
                    switch (player1Weapn) {
                        case SCISSORS:
                            return player2Weapn.score + Result.LOSS.score;
                        case ROCK:
                            return player2Weapn.score + Result.WIN.score;
                        default:
                            throw new RuntimeException("should not get here");
                    }
                case SCISSORS:
                    switch (player1Weapn) {
                        case PAPER:
                            return player2Weapn.score + Result.WIN.score;
                        case ROCK:
                            return player2Weapn.score + Result.LOSS.score;
                        default:
                            throw new RuntimeException("should not get here");
                    }
            }
        }
        throw new RuntimeException("should not get here");
    }

    private static Integer innerToScore(Weapon player1Weapn, Result result) {
        if (Result.DRAW.equals(result)) {
            return player1Weapn.score + Result.DRAW.score;
        } else {
            switch (result) {
                case LOSS:
                    switch (player1Weapn) {
                        case SCISSORS:
                            return Weapon.PAPER.score + Result.LOSS.score;
                        case PAPER:
                            return Weapon.ROCK.score + Result.LOSS.score;
                        case ROCK:
                            return Weapon.SCISSORS.score + Result.LOSS.score;
                    }
                case WIN:
                    switch (player1Weapn) {
                        case SCISSORS:
                            return Weapon.ROCK.score + Result.WIN.score;
                        case PAPER:
                            return Weapon.SCISSORS.score + Result.WIN.score;
                        case ROCK:
                            return Weapon.PAPER.score + Result.WIN.score;
                    }
            }
            throw new RuntimeException("should not get here");
        }
    }

    private static Weapon mapPlayer1CodeToWeapon(String code) {
        if (Weapon.ROCK.player1Code.equals(code)) {
            return Weapon.ROCK;
        } else if (Weapon.PAPER.player1Code.equals(code)) {
            return Weapon.PAPER;
        } else if (Weapon.SCISSORS.player1Code.equals(code)) {
            return Weapon.SCISSORS;
        } else {
            throw new RuntimeException("Unknown weapon" + code);
        }
    }

    private static Weapon mapPlayer2CodeToWeapon(String code) {
        if (Weapon.ROCK.player2Code.equals(code)) {
            return Weapon.ROCK;
        } else if (Weapon.PAPER.player2Code.equals(code)) {
            return Weapon.PAPER;
        } else if (Weapon.SCISSORS.player2Code.equals(code)) {
            return Weapon.SCISSORS;
        } else {
            throw new RuntimeException("Unknown weapon" + code);
        }
    }


    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Path.of(ClassLoader.getSystemResource("rock.txt").toURI()));
        int result = lines.stream().map(line -> toScore(line)).mapToInt(Integer::intValue).sum();
        System.out.println(result);

    }

}
