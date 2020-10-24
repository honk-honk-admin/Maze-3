public class Main {

	public static void main(String[] args) {
		
		char[][] myOwnMaze = {
     {'O', '_', '_', '_', 'X', '_'},
     {'X', '_', 'X', '_', 'X', '_'},
     {'X', '_', 'X', 'X', 'X', 'X'},
     {'X', '_', '_', '_', '_', '_'},
     {'_', 'X', 'X', '_', 'X', '_'},
     {'_', 'X', 'X', '_', 'X', '_'}
		};
        // Now I am creating my Maze: I basically design a big bag in which I will make sure to place my labyrinth
		Maze myMaze = new Maze(myOwnMaze);
        // I print the labyrinth inside my maze bag
		myMaze.print();
        myMaze.solveMaze();
    }
}
