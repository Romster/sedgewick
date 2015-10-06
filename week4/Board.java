import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class Board {

    private Board previous;
    private final int[][] blocks;
    private int zeroI;
    private int zeroJ;

    /**
     * construct a board from an N-by-N array of blocks
     * (where blocks[i][j] = block in row i, column j)
     *
     * @param blocks
     */
    public Board(int[][] blocks) {
        validate(blocks);
        this.blocks = deepArrayCopy(blocks);
    }

    /**
     * @return board dimension N
     */
    public int dimension() {
        return blocks.length;
    }

    /**
     * @return number of blocks out of place
     */
    public int hamming() {
        int hamm = 0;
        int n = blocks.length;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                int correct = i * n + j + 1;
                int val = blocks[i][j];
                if (val == correct || val == 0) {
                    continue;
                } else {
                    hamm++;
                }
            }
        }
        return hamm;
    }

    /**
     * @return sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                int val = blocks[i][j];
                if (val == 0) continue;
                int expected = i * dimension() + j + 1;
                if (val != expected) {
                    manhattan += distance(expected, val);
                }
            }
        }
        return manhattan;
    }

    /**
     * @return is this board the goal board?
     */
    public boolean isGoal() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                int val = blocks[i][j];
                if (val == 0) continue;
                if (val != i * dimension() + j + 1) return false;
            }
        }
        return true;
    }

    /**
     * @return a board that is obtained by exchanging any pair of blocks
     */
    public Board twin() {
        int[][] newArr = deepArrayCopy(blocks);
        int i = 0;
        if (zeroI == 0) i++;
        swap(newArr, i, 0, i, 1);
        return new Board(newArr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;
        if (zeroI != board.zeroI || zeroJ != board.zeroJ) return false;
        return Arrays.deepEquals(blocks, board.blocks);

    }


    /**
     * @return all neighboring boards. last neighbor can be same as previous
     */
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>(4);
        Board sameAsPrevious = null;
        Board temp;
        if (zeroI > 0) {
            temp = tryToAddNeighbor(neighbors, zeroI - 1, zeroJ);
            if (temp != null) {
                sameAsPrevious = temp;
            }
        }
        if (zeroI < dimension() - 1) {
            temp = tryToAddNeighbor(neighbors, zeroI + 1, zeroJ);
            if (temp != null) {
                sameAsPrevious = temp;
            }
        }
        if (zeroJ > 0) {
            temp = tryToAddNeighbor(neighbors, zeroI, zeroJ - 1);
            if (temp != null) {
                sameAsPrevious = temp;
            }
        }
        if (zeroJ < dimension() - 1) {
            temp = tryToAddNeighbor(neighbors, zeroI, zeroJ + 1);
            if (temp != null) {
                sameAsPrevious = temp;
            }
        }
        if (sameAsPrevious != null) {
            neighbors.add(sameAsPrevious);
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
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
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
                        zeroI = i;
                        zeroJ = j;
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
        int aXIndex = 0;
        int bXIndex = 0;
        while (a % dimension() != 0) {
            a++;
            aXIndex++;
        }
        while (b % dimension() != 0) {
            b++;
            bXIndex++;
        }
        int xDistance = Math.abs(aXIndex - bXIndex);
        int aYIndex = a / dimension();
        int bYIndex = b / dimension();
        int yDistance = Math.abs(aYIndex - bYIndex);
        int distance = xDistance + yDistance;
        return distance;
    }

    /**
     * perfomance optimization
     *
     * @param neighbors
     * @param newZeroI
     * @param newZeroJ
     * @return null if neighbour is unique, neighbour if it is same as previous (that neighbor
     * will not be added to list)
     */
    private Board tryToAddNeighbor(List<Board> neighbors, int newZeroI, int newZeroJ) {
        int[][] newArr = deepArrayCopy(blocks);
        swap(newArr, zeroI, zeroJ, newZeroI, newZeroJ);
        Board newBoard = new Board(newArr);
        newBoard.previous = this;
        if (isSameAsPrevious(newBoard)) {
            return newBoard;
        } else {
            neighbors.add(newBoard);
            return null;
        }
    }

    private void swap(int[][] arr, int i1, int j1, int i2, int j2) {
        arr[i1][j1] ^= arr[i2][j2];
        arr[i2][j2] ^= arr[i1][j1];
        arr[i1][j1] ^= arr[i2][j2];
    }

    private int[][] deepArrayCopy(int[][] arr) {
        int[][] newArr = new int[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = Arrays.copyOf(arr[i], arr[i].length);
        }
        return newArr;
    }

    private boolean isSameAsPrevious(Board newBoard) {
        return previous != null
                && newBoard.zeroI == previous.zeroI
                && newBoard.zeroJ == previous.zeroJ;
    }

}
