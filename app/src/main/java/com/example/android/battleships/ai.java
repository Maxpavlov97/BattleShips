package com.example.android.battleships;

/**
 * Created by Max on 4/2/2016.
 */
public class ai {
    final int UNKNOWN = 0;
    final int HIT = 1;
    final int EMPTY = 2; //aka no chance of a hit here
    final int POTENTIAL = 3;
    final int SUNK = 4;

    final int UP = 0;
    final int RIGHT = 1;
    final int DOWN = 2;
    final int LEFT = 3;

    Board board;
    int[][] map;
    int numCells;

    int hits=0;
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
        if(hits==0){
            //System.out.println("Shooting randomly");
            while(true) {
                int x = (int) (Math.random() * numCells);
                int y = (int) (Math.random() * numCells);
                if (canShoot(x, y)) {
                    map[x][y] = POTENTIAL;
                    break;
                }
            }
        }
        else{
            //System.out.println("Trying to sink ship");
            //find tile to shoot at that connects to the found ship
            int i=0;
            int j=0;
            outerLoop: //finds the hit ship to work with
            for(i=0; i<numCells; i++) {
                for (j = 0; j < numCells; j++) {
                    if(map[i][j]==HIT){
                        break outerLoop;
                    }
                }
            }
            if(hits==1){
                getInitialDirections(i, j);
            }
            else{
                getDirection(i,j);
                System.out.println("Up="+up+", right="+right+", down="+down+", left="+left);
                if(up) {
                    findNextPoint(i, j, UP);
                }
                if(right) {
                    findNextPoint(i, j, RIGHT);
                }
                if(down) {
                    findNextPoint(i, j, DOWN);
                }
                if(left) {
                    findNextPoint(i, j, LEFT);
                }
            }
        }
        //System.out.println("About to pick");
        pickPotential();
    }

    private void pickPotential(){
        System.out.println("");
        System.out.println("Potentials");
        int potentials = 0;
        for(int i=0; i<numCells; i++){
            for(int j=0; j<numCells; j++){
               int a = map[i][j];
                if(a==POTENTIAL){
                    potentials++;
                    System.out.println(MainActivity.alphabet[j]+", "+(i+1));
                }
            }
        }
        //System.out.println(potentials+" potentials");
        int chosen = (int)(Math.random()*potentials);
        for(int i=0; i<numCells; i++){
            for(int j=0; j<numCells; j++){
                if(map[i][j]==POTENTIAL&&chosen--==0){
                    System.out.println("picked "+MainActivity.alphabet[j]+", "+(i+1));
                    int ans = board.shot(i,j);
                    if(ans == 0){
                        map[i][j]=EMPTY;
                    }
                    if(ans == 1){
                        map[i][j]=HIT;
                        hits++;
                    }
                    if(ans == 2){
                        map[i][j]=HIT;
                        hits++;

                        hits = 0;
                        sunk();
                    }
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
            while(j-->1){
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
            while(j++<numCells-1){
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
            while(i++<numCells-1){
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
    
    public void getDirection(int x, int y){
        if(checkHit(x,y-1)||checkHit(x,y+1)){
            left = false;
            right = false;
        }
        if(checkHit(x-1,y)||checkHit(x+1,y)){
            up = false;
            down = false;
        }
    }

    public void getInitialDirections(int x, int y){
        up = checkCell(x,y-1);
        right = checkCell(x+1,y);
        down = checkCell(x,y+1);
        left = checkCell(x-1,y);
        if(up){
            map[x][y-1]=POTENTIAL;
        }
        if(right){
            map[x+1][y]=POTENTIAL;
        }
        if(down){
            map[x][y+1]=POTENTIAL;
        }
        if(left){
            map[x-1][y]=POTENTIAL;
        }
    }

    public boolean checkCell(int x, int y){
        if(x>=0&&x<numCells&&y>=0&&y<numCells){
            if(map[x][y]==UNKNOWN){
                return true;
            }
        }
        return false;
    }

    public boolean checkHit(int x, int y){
        if(x>=0&&x<numCells&&y>=0&&y<numCells){
            if(map[x][y]==HIT){
                return true;
            }
        }
        return false;
    }

    public void sunk(){
        System.out.println("Ship is sunk, the following points are marked EMPTY:");
        for(int i=0; i<numCells; i++){
            for(int j=0; j<numCells; j++){
                if(map[i][j]==HIT){
                    map[i][j]=SUNK;
                    markEmptyPoints(i-1,j);
                    markEmptyPoints(i+1,j);
                    markEmptyPoints(i,j-1);
                    markEmptyPoints(i,j+1);
                }
            }
        }
    }

    public void markEmptyPoints(int x, int y){
        //System.out.println(MainActivity.alphabet[y]+", "+(x+1)+" is "+map[x][y]);
        if(x>=0&&x<numCells&&y>=0&&y<numCells&&(map[x][y]==UNKNOWN||map[x][y]==POTENTIAL)){
            map[x][y]=EMPTY;
            System.out.println(MainActivity.alphabet[y]+", "+(x+1));
        }
    }
}
