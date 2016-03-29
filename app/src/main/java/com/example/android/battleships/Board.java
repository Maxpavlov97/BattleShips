package com.example.android.battleships;

import android.content.Context;

/**
 * Created by Max on 3/28/2016.
 */
public class Board {
    Cell[][] board;
    int numCells;

    int pX;     //prefix p means temporary
    int pY;
    int pSize;

    static final int UP = 0;
    static final int RIGHT = 1;
    static final int DOWN = 2;
    static final int LEFT = 3;

    public Board(int i) {
        board= new Cell[i][i];
        numCells = i;
    }

    public Cell[][] getBoard(){
        return board;
    }

    public boolean drawAvailableShips(int x, int y, int size){
        boolean ans = false;
        pX=x;
        pY=y;
        pSize=size;
        if(check(x, y, size, UP)){
            for(int i=0; i<size; i++){
                board[x][y-i].setImage(R.drawable.build);
                //board[x][y-i].build(true);
            }
            ans = true;
        }
        if(check(x, y, size, RIGHT)){
            for(int i=0; i<size; i++){
                board[x+i][y].setImage(R.drawable.build);
                //board[x+i][y].build(true);
            }
            ans = true;
        }
        if(check(x, y, size, DOWN)){
            for(int i=0; i<size; i++){
                board[x][y+i].setImage(R.drawable.build);
                //board[x][y+i].build(true);
            }
            ans = true;
        }
        if(check(x, y, size, LEFT)){
            for(int i=0; i<size; i++){
                board[x-i][y].setImage(R.drawable.build);
                //board[x-i][y].build(true);
            }
            ans = true;
        }
        return ans;
    }

    public boolean pickAvailableShip(Cell c){
        boolean ans = false;
        int cX=c.getXCoord();
        int cY=c.getYCoord();
        if(cX==pX&&cY==pY){
            if(pSize==1 && c.getImageId()==R.drawable.build){
                board[pX][pY].setImage(R.drawable.submarine);
                board[pX][pY].build(true);
                return true;
            }
            else {
                return false;
            }
        }
        if(c.getImageId()==R.drawable.build){
            ans = true;
            if(cX>pX) { //picked right
                for (int i = 0; i < pSize; i++) {
                    if(i==0)
                        board[pX+i][pY].setImage(R.drawable.ship_end_right);
                    else if(i==pSize-1)
                        board[pX+i][pY].setImage(R.drawable.ship_front_right);
                    else
                        board[pX+i][pY].setImage(R.drawable.ship_middle_horizontal);
                    board[pX+i][pY].build(true);
                }
            }
            if(cX<pX) { //picked left
                for (int i = 0; i < pSize; i++) {
                    if(i==0)
                        board[pX-i][pY].setImage(R.drawable.ship_end_left);
                    else if(i==pSize-1)
                        board[pX-i][pY].setImage(R.drawable.ship_front_left);
                    else
                        board[pX-i][pY].setImage(R.drawable.ship_middle_horizontal);
                    board[pX-i][pY].build(true);
                }
            }
            if(cY>pY) { //picked down
                for (int i = 0; i < pSize; i++) {
                    if(i==0)
                        board[pX][pY+i].setImage(R.drawable.ship_end_down);
                    else if(i==pSize-1)
                        board[pX][pY+i].setImage(R.drawable.ship_front_down);
                    else
                        board[pX][pY+i].setImage(R.drawable.ship_middle_vertical);
                    board[pX][pY+i].build(true);
                }
            }
            if(cY<pY) { //picked up
                for (int i = 0; i < pSize; i++) {
                    if(i==0)
                        board[pX][pY-i].setImage(R.drawable.ship_end_up);
                    else if(i==pSize-1)
                        board[pX][pY-i].setImage(R.drawable.ship_front_up);
                    else
                        board[pX][pY-i].setImage(R.drawable.ship_middle_vertical);
                    board[pX][pY-i].build(true);
                }
            }
        }
        return ans;
    }

    public void clearBuilds(){
        for(int x=0; x<numCells; x++){
            for(int y=0; y<numCells; y++){
                if(board[x][y].getImageId()==R.drawable.build){
                    board[x][y].setImage(R.drawable.cloud);
                    //board[x][y].build(false);
                }
            }
        }
    }

    public boolean checkForSpace(int x, int y, int size, int direction){
        size--;
        if(direction == UP && (y-size>=0)){
            return true;
        }
        else if(direction == RIGHT && (x+size<numCells)){
            return true;
        }
        else if(direction == DOWN && (y+size<numCells)){
            return true;
        }
        else if(direction == LEFT && (x-size>=0)){
            return true;
        }
        return false;
    }
    
    public boolean checkForObstruction(int x, int y, int size, int direction){
        if(y-1!=-1 && !board[x][y-1].isEmpty())
            return false;
        if(x+1!=numCells && !board[x+1][y].isEmpty())
            return false;
        if(y+1!=numCells && !board[x][y+1].isEmpty())
            return false;
        if(x-1!=-1 && !board[x-1][y].isEmpty())
            return false;

        for(int i=0; i<=size; i++)
        {
            if(direction == UP && y-i>=0)
            {
                if(!board[x][y-i].isEmpty())
                    return false;
                if(x-1>=0 && !board[x-1][y-i].isEmpty())
                    return false;
                if(x+1<numCells && !board[x+1][y-i].isEmpty())
                    return false;
            }

            else if(direction == RIGHT && x+i<numCells)
            {
                if(!board[x+i][y].isEmpty())
                    return false;
                if(y-1>=0 && !board[x+i][y-1].isEmpty())
                    return false;
                if(y+1<numCells && !board[x+i][y+1].isEmpty())
                    return false;
            }

            else if(direction == DOWN && y+i<numCells)
            {
                if(!board[x][y+i].isEmpty())
                    return false;
                if(x-1>=0 && !board[x-1][y+i].isEmpty())
                    return false;
                if(x+1<numCells && !board[x+1][y+i].isEmpty())
                    return false;
            }

            else if(direction == LEFT && x-i>=0)
            {
                if(!board[x-i][y].isEmpty())
                    return false;
                if(y-1>=0 && !board[x-i][y-1].isEmpty())
                    return false;
                if(y+1<numCells && !board[x-i][y+1].isEmpty())
                    return false;
            }
        }
        return true;
    }
    public boolean check(int x, int y, int s, int d){
        return checkForSpace(x,y,s,d)&&checkForObstruction(x,y,s,d);
    }


}


