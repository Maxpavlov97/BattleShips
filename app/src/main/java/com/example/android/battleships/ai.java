package com.example.android.battleships;

/**
 * Created by Max on 4/2/2016.
 */
public class ai {
    final int UNKNOWN = 0;
    final int HIT = 1;
    final int EMPTY = 2; //aka no chance of a hit here
    final int POTENTIAL = 3;

    final int UP = 0;
    final int RIGHT = 1;
    final int DOWN = 2;
    final int LEFT = 3;

    Board board;
    int[][] map;
    int numCells;

    boolean foundShip = false;
    boolean multipleHits = false;
    boolean knowDirection = false;
    int direction;
    boolean knowOrientation;
    boolean vertical;
    boolean up;
    boolean down;
    boolean left;
    boolean right;



    public ai(Board b){
        numCells = b.getNumCells();
        map = new int[numCells][numCells];
        board = b;
    }

    public void shoot(){
        //System.out.println("AI Shooting");
        if(!foundShip){
            //System.out.println("!foundShip");
            while(true) {
                int x = (int) (Math.random() * numCells);
                int y = (int) (Math.random() * numCells);
                System.out.println(x+", "+y);
                if (canShoot(x, y)) {
                    map[x][y] = POTENTIAL;
                    break;
                }
            }
        }
        else{
            //System.out.println("foundShip");
            //find tile to shoot at that connects to the found ship
            for(int i=0; i<numCells; i++) {
                for (int j = 0; j < numCells; j++) {
                    if(map[i][j]==HIT){
                        if(!knowOrientation){
                            getDirections(i, j);
                        }
                        if(knowDirection){
                            findNextPoint(i,j,direction);
                        }
                    }
                }
            }
        }
        //System.out.println("About to pick");
        pickPotential();
    }

    private void pickPotential(){
       // System.out.println("picking potential");
        int potentials = 0;
        for(int i=0; i<numCells; i++){
            for(int j=0; j<numCells; j++){
               int a = map[i][j];
                if(a==POTENTIAL){
                    potentials++;
                }
            }
        }
        System.out.println(potentials+" potentials");
        int chosen = (int)(Math.random()*potentials);
        System.out.println("chose "+ chosen);
        for(int i=0; i<numCells; i++){
            for(int j=0; j<numCells; j++){
                if(map[i][j]==POTENTIAL&&chosen--==0){
                    board.shot(i,j);
                    System.out.println("picked "+MainActivity.alphabet[j]+", "+(i+1));
                }
            }
        }
        clearPotentials();
    }

    public void clearPotentials(){
        for(int i =0; i<numCells; i++){
            for(int j=0; j<numCells; j++){
                if(map[i][j]==POTENTIAL){
                    map[i][j]=UNKNOWN;
                }
            }
        }
    }


    public void findShot(){

    }
    
    public boolean canShoot(int i, int j){
        if(i>=0&&i<numCells&&j>=0&&j<numCells&&map[i][j]==UNKNOWN)
            return true;
        return false;
    }

    public boolean findNextPoint(int i, int j, int d){
        if(d==UP){
            while(j-->0){
                if(map[i][j]==UNKNOWN){
                    map[i][j]=POTENTIAL;
                    return true;
                }
                else if(map[i][j]!=HIT){
                    System.out.println("map["+i+"]["+j+"]="+map[i][i]);
                    up=false;
                    return false;
                }
            }
        }
        if(d==DOWN){
            while(j++<numCells){
                if(map[i][j]==UNKNOWN){
                    map[i][j]=POTENTIAL;
                    return true;
                }
                else if(map[i][j]!=HIT){
                    System.out.println("map["+i+"]["+j+"]="+map[i][i]);
                    down=false;
                    return false;
                }
            }
        }
        if(d==LEFT){
            while(i-->0){
                if(map[i][j]==UNKNOWN){
                    map[i][j]=POTENTIAL;
                    return true;
                }
                else if(map[i][j]!=HIT){
                    System.out.println("map["+i+"]["+j+"]="+map[i][i]);
                    left=false;
                    return false;
                }
            }
        }
        if(d==RIGHT){
            while(i++<numCells){
                if(map[i][j]==UNKNOWN){
                    map[i][j]=POTENTIAL;
                    return true;
                }
                else if(map[i][j]!=HIT){
                    System.out.println("map["+i+"]["+j+"]="+map[i][i]);
                    right=false;
                    return false;
                }
            }
        }
        return false;

    }

    public boolean checkDirection(int i, int j, int d){
        if(i>=0&&i<numCells&&j>=0&&j<numCells){
            if(map[i][j]==1){
                if(d==UP){
                    knowOrientation = true;
                    vertical = true;
                    left=false;
                    right = false;
                }
                if(d==DOWN){
                    knowOrientation = true;
                    vertical = true;
                    left=false;
                    right = false;
                }
                if(d==RIGHT){
                    knowOrientation = true;
                    vertical = false;
                    up=false;
                    down = false;
                }
                if(d==LEFT){
                    knowOrientation = true;
                    vertical = false;
                    up=false;
                    down = false;
                }
                return true;
            }
            else if(map[i][j]==0){
                if(d==UP){
                    up=true;
                    if(!multipleHits){
                        findNextPoint(i,j,d);
                    }
                }
                if(d==DOWN){
                    down = true;
                    if(!multipleHits){
                        findNextPoint(i,j,d);
                    }
                }
                if(d==RIGHT){
                    right = true;
                    if(!multipleHits){
                        findNextPoint(i,j,d);
                    }
                }
                if(d==LEFT){
                    left = true;
                    if(!multipleHits){
                        findNextPoint(i,j,d);
                    }
                }
                return true;
            }
        }
        return false;

    }

    public void getDirections(int i, int j){
        knowDirection=false;
        knowOrientation=false;

        checkDirection(i - 1, j, LEFT);
        checkDirection(i + 1, j, RIGHT);
        checkDirection(i, j - 1, UP);
        checkDirection(i,j+1, DOWN);
        
        if(knowOrientation){
            if(vertical){
                if((up&&!down)||(!up&&down)){
                    knowDirection=true;
                    if(up){
                        direction=UP;
                    }
                    else{
                        direction=DOWN;
                    }
                }
            }
            else{
                if((left&&!right)||(!left&&right)){
                    knowDirection=true;
                    if(left){
                        direction=LEFT;
                    }
                    else{
                        direction=RIGHT;
                    }
                }
            }
        }
    }

/*
    public boolean thereAreNoHits(){
        for(int i=0; i<numCells; i++){
            for(int j=0; j<numCells; j++){
                if(map[i][j]==HIT){
                    return false;
                }
            }
        }
        return true;
    }
*/


}
