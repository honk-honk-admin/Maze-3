

import java.util.Arrays;

public class Maze {

    /************* ATTRIBUTES ************************/
    /* Here we describe the attributes of a maze:
     * it is represented by a 2D array of characters
     * and of course, it has a known dimension
     */
    private int dimension;
    private char[][] labyrinth;

    /************* CONSTRUCTORS ************************/
    /* At some point, we need to declare our Maze:
     * We use constructors for that.
     */
    /* Default Constructor:
     * By default, we will make the maze size 10x10
     */
    public Maze() {
        dimension = 10;
        labyrinth = new char[10][10];
    }

    /* Sometimes we know the dimension we want
     * and it is not the default dimension
     */
    public Maze(int dim) {
        dimension = dim;
        labyrinth = new char[dim][dim];
    }

    /* Sometimes we know exactly the maze we want
     * so we go ahead and store it in labyrinth
     * and we get the dimension from that
     */
    public Maze(char[][] L) {
        if (L.length != L[0].length) { // if L is not square, we revert to default constructor
            dimension = 10;
            labyrinth = new char[10][10];
        } else {
            dimension = L.length;
            labyrinth = new char[dimension][dimension];
            for (int i=0; i<dimension; i++)
                for (int j=0; j<dimension; j++)
                    labyrinth[i][j] = L[i][j];
        }
    }

    /************* GETTERS ************************/
    public char[][] getLabyrinth() {
        return labyrinth;
    }

    /************* MODIFIERS ************************/
    public void changeLabyrinthElement(char c, int row, int col) {
        labyrinth[row][col] = c;
    }


    /************* METHODS ************************/
    /* GIVEN TO YOU:
     * Now what is a maze made of???
     * Wouldn't it be nice to actually see it for ourselves?
     */
    public void print() {
        System.out.println("My maze: ");
        for (int i=0; i<dimension; i++) {
            for (int j=0; j<dimension; j++)
                //if (labyrinth[i][j] == '_' || labyrinth[i][j] == 'O' || labyrinth[i][j] == 'X')
                System.out.print(labyrinth[i][j] + " ");
            //else
            //	System.out.print("_" + " ");
            System.out.println();
        }
    }

    /* GIVEN TO YOU:
     * This is the method that checks all 4 directions (R, D, L, U) and returns an array of 4 boolean values
     * true for possible, false for not possible
     * The 1st boolean value corresponds to whether we can go right
     * The 2nd boolean value corresponds to whether we can go down
     * The 3rd boolean value corresponds to whether we can go left
     * The 4th boolean value corresponds to whether we can go up
     */
    public boolean[] directionsForward(int row, int col) {
        boolean[] direction = new boolean[4]; // {R,D,L,U}
        // I can move to the right unless:
        // I am in the rightmost column OR there is a X to my right
        direction[0] = (col != dimension-1) && (labyrinth[row][col+1] != 'X') && (labyrinth[row][col+1] != 'v');
        // I can move down unless:
        // I am in the bottom row OR there is a X below me
        direction[1] = (row != dimension-1) && (labyrinth[row+1][col] != 'X') && (labyrinth[row+1][col] != 'v');
        // I can move to the left unless:
        // I am in the first column OR there is a X to the left of me
        direction[2] = (col != 0) && (labyrinth[row][col-1] != 'X') && (labyrinth[row][col-1] != 'v');
        // I can move up unless:
        // I am in the first row OR there is a X above me
        direction[3] = (row != 0) && (labyrinth[row-1][col] != 'X') && (labyrinth[row-1][col] != 'v');
        return direction;
    }

    public boolean[] directionsBackward(int row, int col) {
        boolean[] direction = new boolean[4]; // {R,D,L,U}
        // I can move to the right unless:
        // I am in the rightmost column OR there is a X to my right OR if the pebble is green (we can go there anymore)
        direction[0] = (col != dimension-1) && (labyrinth[row][col+1] != 'X') && (labyrinth[row][col+1] != 'w');
        // I can move down unless:
        // I am in the bottom row OR there is a X below me OR if the pebble is green (we can go there anymore)
        direction[1] = (row != dimension-1) && (labyrinth[row+1][col] != 'X') && (labyrinth[row+1][col] != 'w');
        // I can move to the left unless:
        // I am in the first column OR there is a X to the left of me OR if the pebble is green (we can go there anymore)
        direction[2] = (col != 0) && (labyrinth[row][col-1] != 'X') && (labyrinth[row][col-1] != 'w');
        // I can move up unless:
        // I am in the first row OR there is a X above me OR if the pebble is green (we can go there anymore)
        direction[3] = (row != 0) && (labyrinth[row-1][col] != 'X') && (labyrinth[row-1][col] != 'w');
        return direction;
    }

    /* GIVEN TO YOU:
     * given a location (row, col) in a labyrinth, identify where to go
     * the priority is to go to a non-visited place in the priority order R,D,L,U
     * if some are X or v, move to the next option. If none, then go back to picking a v in reverse order

     * We would like to return R,D,L,U
     * but:
     * when we return R, we add 0 to row and 1 to col (0,1)
     * when we return D, we add 1 to row and 0 to col (1,0)
     * when we return L, we add 0 to row and -1 to col (0,-1)
     * when we return U, we add -1 to row and 0 to col (-1,0)
     * So instead, we are going to return a pair (r,c)
     */
    public int[] move(int row, int col) {

        int[] result = new int[2];

        // we define an array that will hold information about which directions are visitable
        boolean[] direction = directionsForward(row,col); // {R,D,L,U} // directions(row, col);

        /* Below is a table that helps you:
         * the 1st row (moves[0][0] and moves[0][1]) holds the +/-1 for the row and col in case we move Right
         * the 2nd row (moves[1][0] and moves[1][1]) holds the +/-1 for the row and col in case we move Down
         * the 3rd row (moves[2][0] and moves[2][1]) holds the +/-1 for the row and col in case we move Left
         * the 4th row (moves[3][0] and moves[3][1]) holds the +/-1 for the row and col in case we move Up
         */
        int[][] moves = new int[4][2];
        moves[0][0] = 0;  moves[0][1] = 1;
        moves[1][0] = 1;  moves[1][1] = 0;
        moves[2][0] = 0;  moves[2][1] = -1;
        moves[3][0] = -1; moves[3][1] = 0;

        // now we are going to see if there is any direction that has not been visited yet = a true
        // if there is: this is where we want to go (in the priority order: so as soon as we find it, we return it)
        if (direction[0]
                && labyrinth[row+moves[0][0]][col+moves[0][1]] != 'X'
                && labyrinth[row+moves[0][0]][col+moves[0][1]] != 'v') {
            // then we return the proper moves:
            return moves[0];
        } else if (direction[1]
                && labyrinth[row+moves[1][0]][col+moves[1][1]] != 'X'
                && labyrinth[row+moves[1][0]][col+moves[1][1]] != 'v') {
            // then we return the proper moves:
            return moves[1];
        } else if (direction[2]
                && labyrinth[row+moves[2][0]][col+moves[2][1]] != 'X'
                && labyrinth[row+moves[2][0]][col+moves[2][1]] != 'v') {
            // then we return the proper moves:
            return moves[2];
        } else if (direction[3]
                && labyrinth[row+moves[3][0]][col+moves[3][1]] != 'X'
                && labyrinth[row+moves[3][0]][col+moves[3][1]] != 'v') {
            // then we return the proper moves:
            return moves[3];
        }

        // However, if at that point, we have not returned anything, we are going to check if there is a visited spot we can go to
        // this time, it is reverse order of priority, because we are tracing back our steps.
        // it means that we need to move backwards:
        // we go back in the priorities, now U, L, D, R
        for (int i=3; i>=0; i--) {
            if (direction[i]  && labyrinth[row+moves[i][0]][col+moves[i][1]] == 'v') {
                System.out.println("I can move in direction " + i);
                return moves[i];
            }
        }

        return result;
    }

    /* THIS IS WHERE YOU NEED TO WRITE SOME CODE: PLEASE FOLLOW THE COMMENTS
     * Solve the maze
     * and see how well you did it by returning the number of steps it took you
     * In this method, we take the approach of always sticking to the same priority of moves:
     * it may take longer but it will always allow you to exit a maze
     */
    public int solveMaze() {
        // we need to keep track of where we are in the maze
        // when we start, we are at the top left corner
        int locationR = 0; // this is the row index
        int locationC = 0; // this is the column index

        // just because we are a little bit competitive, we want to keep track of the number of steps it
        // took the computer to exit the maze
        int steps = 0;

        // CHECK WHERE YOU CAN GO FROM THE CURRENT LOCATION (locationR, locationC).
        // you need to get the 1D array of moves {+/-1, +/-1} to add to the row / column to make your move
        int[][] moves = new int[4][2];
        //*******CODE HERE!!!!!!//

        // At the new location in the array, where the O goes, you should now have an O
        // This new location is at indices: (locationR + moves[0], locationC + moves[1])
        // You will use the method changeLabyrinthElement('O', /* index row */, /* index col */) to move O
        changeLabyrinthElement('O',locationR, locationC);

        // then you will remove the O from where it was and replace it for a v (for the red pebble)
        changeLabyrinthElement('v', locationR, locationC);

        // now you can print the maze again to see what is happening
        print();

        return steps;
    }



}
