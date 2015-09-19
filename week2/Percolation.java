import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by romster on 11.09.15.
 */
public class Percolation {

    private int virtualTop;
    private int virtualBot;

    private final WeightedQuickUnionUF unitedCells;
    private final WeightedQuickUnionUF filledCells;
    //    private final int[][] cells;
    private final boolean[] cellStatus;

    private final int n;


    // create N-by-N grid, wiфth all sites blocked
    public Percolation(int n) {
        this.n = n;
        if (n <= 0) {
            throw new IllegalArgumentException("n should be > 0");
        }
        int elementaCount = n * n;
        cellStatus = new boolean[elementaCount];
        virtualTop = elementaCount;
        virtualBot = elementaCount + 1;
        unitedCells = new WeightedQuickUnionUF(virtualBot + 1);
        filledCells = new WeightedQuickUnionUF(virtualBot);
    }

    /**
     * open site (row i, column j) if it is not open already
     *
     * @param i
     * @param j
     * @return
     */
    public void open(int i, int j) {
        if (isOpen(i, j)) {
            return;
        }
        int cellId = getCellId(i, j);
        cellStatus[cellId] = true;
        if (i == 1) {
            unitedCells.union(virtualTop, cellId);
            filledCells.union(virtualTop, cellId);
        }
        if (i == n) {
            unitedCells.union(virtualBot, cellId);
        }
        int[] neighbours = getNeighbours(i, j);
        for (int x = 0; x < neighbours.length; x++) {
            int neighbourId = neighbours[x];
            if (neighbourId > -1 && cellStatus[neighbourId]) {
                unitedCells.union(cellId, neighbourId);
                filledCells.union(cellId, neighbourId);
            }
        }

    }

    /**
     * is site (row i, column j) open?
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isOpen(int i, int j) {
        if (i < 1 || i > n ||
                j < 1 || j > n) {
            throw new IndexOutOfBoundsException("i and j should be > 0 and < " + n);
        }
        int cellId = getCellId(i, j);
        return cellStatus[cellId];
    }

    /**
     * is site (row i, column j) full?
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isFull(int i, int j) {
        if (i < 1 || i > n ||
                j < 1 || j > n) {
            throw new IndexOutOfBoundsException("i and j should be > 0 and < " + n);
        }
        int cellId = getCellId(i, j);
        return filledCells.connected(virtualTop, cellId);
    }

    /**
     * does the system percolate?
     *
     * @return
     */
    public boolean percolates() {
        boolean result = unitedCells.connected(virtualTop, virtualBot);
        return result;
    }

    /**
     * @param i from 1
     * @param j from 1
     * @return -1 if  i or j are illegal arguments
     */
    private int getCellIdSafety(int i, int j) {
        if (i <= 0 || j <= 0
                || i > n || j > n) {
            return -1;
        }
        return getCellId(i, j);
    }


    /**
     * @param i from 1 to N (includes)
     * @param j from 1 to N (includes)
     * @return
     */
    private int getCellId(int i, int j) {
        return (i - 1) * n + (j - 1);
    }

    /**
     * @param i from 1
     * @param j from 1
     * @return int[4] arr; arr[x] == -1 if there is no neighbour;
     */
    private int[] getNeighbours(int i, int j) {
        int[] arr = new int[4];
        arr[0] = getCellIdSafety(i - 1, j);
        arr[1] = getCellIdSafety(i + 1, j);
        arr[2] = getCellIdSafety(i, j - 1);
        arr[3] = getCellIdSafety(i, j + 1);
        return arr;
    }


    public static void main(String[] args) {

    }
}
