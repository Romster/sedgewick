import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class Board {

    private final int[] blocks;
    private int zeroI;
    private int N;

    /**
     * construct a board from an N-by-N array of blocks
     * (where blocks[i][j] = block in row i, column j)
     *
     * @param blocks
     */
    public Board(int[][] blocks) {
        validate(blocks);
        this.blocks = transformInput(blocks);
        this.N = blocks.length;
    }

    private Board(int[] blocks, int dimension, int zeroI) {
        this.blocks = blocks;
        this.N = dimension;
        this.zeroI = zeroI;
    }

    /**
     * @return board dimension N
     */
    public int dimension() {
        return N;
    }

    /**
     * @return number of blocks out of place
     */
    public int hamming() {
        int hamm = 0;
        for (int i = 0; i < blocks.length; i++) {
            int correct = i + 1;
            int val = blocks[i];
            if (val == correct || val == 0) {
                continue;
            } else {
                hamm++;
            }
        }
        return hamm;
    }

    /**
     * @return sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < blocks.length; i++) {
            int val = blocks[i];
            if (val == 0) continue;
            if (val - 1 != i) {
                manhattan += distance(i, val - 1);
            }
        }
        return manhattan;
    }

    /**
     * @return is this board the goal board?
     */
    public boolean isGoal() {
        for (int i = 0; i < blocks.length; i++) {
            int val = blocks[i];
            if (val == 0) continue;
            if (val != i + 1) return false;
        }
        return true;
    }

    /**
     * @return a board that is obtained by exchanging any pair of blocks
     */
    public Board twin() {
        int[] newArr = Arrays.copyOf(blocks, blocks.length);
        int i = 0;
        if (zeroI == 0 || zeroI == 1) i += dimension();
        swap(newArr, i, i + 1);
        return new Board(newArr, dimension(), zeroI);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;
        if (zeroI != board.zeroI) return false;
        return Arrays.equals(this.blocks, board.blocks);

    }


    /**
     * @return all neighboring boards. last neighbor can be same as previous
     */
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>(4);
        //left
        if (zeroI % dimension() != 0) {
            addNeighbor(neighbors, zeroI - 1);
        }
        //right
        if (zeroI % dimension() != dimension() - 1) {
            addNeighbor(neighbors, zeroI + 1);
        }
        //top
        if (zeroI / dimension() != 0) {
            addNeighbor(neighbors, zeroI - dimension());

        }
        //bottom
        if (zeroI / dimension() != dimension() - 1) {
            addNeighbor(neighbors, zeroI + dimension());
        }
        return neighbors;
    }

    /**
     * string representation of this board
     *
     * @return
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        int newLine = dimension();
        for (int i = 0; i < blocks.length; i++) {
            s.append(String.format("%2d ", blocks[i]));
            newLine--;
            if (newLine == 0) {
                newLine = dimension();
                s.append("\n");
            }
        }
        return s.toString();
    }


    private void validate(int[][] input) {
        int n = input.length;
        int maxValue = n * n - 1;
        boolean includesZero = false;
        for (int i = 0; i < n; i++) {
            if (input[i].length != n) throw new IllegalArgumentException("Must be NxN array");
            for (int j = 0; j < n; j++) {
                int currValue = input[i][j];
                if (currValue == 0) {
                    if (!includesZero) {
                        includesZero = true;
                        zeroI = i * n + j;
                    } else {
                        throw new IllegalArgumentException("Only one 0-element allowed");
                    }
                } else if (currValue < 0 || currValue > maxValue) {
                    throw new IllegalArgumentException("An N-by-N array must contains the N2 integers " +
                            "between 0 and N2 − 1, " +
                            "where 0 represents the blank square.");
                }

            }
        }
        if (!includesZero) {
            throw new IllegalArgumentException("must contains one 0-element");
        }
    }


    private int distance(int from, int to) {
        int a = from;
        int b = to;
        int aXIndex = a % dimension();
        int bXIndex = b % dimension();
        int xDistance = Math.abs(aXIndex - bXIndex);
        int aYIndex = a / dimension();
        int bYIndex = b / dimension();
        int yDistance = Math.abs(aYIndex - bYIndex);
        int distance = xDistance + yDistance;
        return distance;
    }

    /**
     * @param neighbors
     * @param newZeroI
     */
    private Board addNeighbor(List<Board> neighbors, int newZeroI) {
        int[] newArr = Arrays.copyOf(blocks, blocks.length);
        swap(newArr, zeroI, newZeroI);
        Board newBoard = new Board(newArr, dimension(), newZeroI);
        neighbors.add(newBoard);
        return null;
    }

    private void swap(int[] arr, int i1, int i2) {
        arr[i1] ^= arr[i2];
        arr[i2] ^= arr[i1];
        arr[i1] ^= arr[i2];
    }

    private int[] transformInput(int[][] input) {
        int n = input.length;
        int[] result = new int[n * n];
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < input[i].length; j++) {
                result[index++] = input[i][j];
            }
        }
        return result;
    }


}
