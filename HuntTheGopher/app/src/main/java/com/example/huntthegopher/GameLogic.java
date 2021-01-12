package com.example.huntthegopher;

import android.util.Log;

public class GameLogic {
    private Cell[][] board;
    private boolean isWon;
    private int gopherX;
    private int gopherY;
    private String winner;

    public GameLogic() {
        board = new Cell[10][10];

        //initialize the entire board to unvisited
        for(Cell[] cell : board) {
            for(Cell c : cell) {
                c = Cell.Unvisited;
            }
        }

        //init the gopher randomly
        gopherX = (int) (Math.random() * 9);
        gopherY = (int) (Math.random() * 9);
        /*gopherX = 1;
        gopherY = 1;*/
        Log.i("GameLogic", "Gopher loc: " + gopherX + ", " + gopherY);
        board[gopherX][gopherY] = Cell.Gopher;

        isWon = false;
        winner = "none";
    }

    public Move playMove(int posx, int posy, Cell cell) {
        //check success
        if(board[posx][posy] == Cell.Gopher) {
            setIsWon(true);
            setBoardPosition(posx, posy, cell);
            if(cell == Cell.Blue) {
                winner = "Blue";
            }
            else if(cell == Cell.Red) {
                winner = "Red";
            }
            return Move.Success;
        }
        //check disaster
        else if(board[posx][posy] == Cell.Blue || board[posx][posy] == Cell.Red) {
            return Move.Disaster;
        }
        //check near miss
        else if(isNearMiss(posx, posy)) {
            setBoardPosition(posx, posy, cell);
            return Move.NearMiss;
        }
        //check close guess
        else if(isCloseGuess(posx, posy)) {
            setBoardPosition(posx, posy, cell);
            return Move.CloseGuess;
        }
        //otherwise, it is a complete miss
        else {
            setBoardPosition(posx, posy, cell);
            return Move.CompleteMiss;
        }
    }

    //guess is one of the 8 holes adjacent to the gopher’s hole
    public boolean isNearMiss(int posx, int posy) {
        int diffX = Math.abs(posx - gopherX);
        int diffY = Math.abs(posy - gopherY);

        if((diffX == 0 || diffX == 1) && (diffY == 0 || diffY == 1)) {
            return true;
        }
        return false;
    }

    //guess is 2 holes away from the gopher’s hole in any directions.
    public boolean isCloseGuess(int posx, int posy) {
        int diffX = Math.abs(posx - gopherX);
        int diffY = Math.abs(posy - gopherY);

        if((diffX >= 0 && diffX <= 2) && (diffY >= 0 && diffY <= 2)) {
            return true;
        }
        return false;
    }

    //checks if the position is valid
    public boolean isValidPosition(int posx, int posy) {
        if(posx >= 0 && posx < 10 && posy >= 0 && posy < 10) {
            return true;
        }
        return false;
    }

    private void setBoardPosition(int posx, int posy, Cell cell) {
        board[posx][posy] = cell;
    }

    public Cell getBoardPosition(int posx, int posy) {
        return board[posx][posy];
    }

    private void setIsWon(boolean won) {
        isWon = won;
    }

    public boolean getIsWon() {
        return isWon;
    }

    public String getWinner() {
        return winner;
    }

}
