package com.example.android.battleships;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class Board {
    Cell[][] board;
    int[][] points;
    Ships ships;
    int[] shipsRemaining;
    int numCells;
    boolean defeated;
    Context context;

    int pX;     //prefix p means temporary
    int pY;
    int pSize;

    static final int UP = 0;
    static final int RIGHT = 1;
    static final int DOWN = 2;
    static final int LEFT = 3;

    private int shots;
    private int misses;
    private int hits;
    private int shipsSunk;

    public Board(int i, Context c) {
        board= new Cell[i][i];
        numCells = i;
        context = c;
        points = new int[i][i];
        ships = new Ships();
        defeated = false;

        shots = 0;
        misses = 0;
        hits = 0;
        shipsSunk = 0;

        shipsRemaining = new int[Ships.shipsToPlace.length];
        for(int q=0; q<Ships.shipsToPlace.length; q++){
            shipsRemaining[q]=Ships.shipsToPlace[q];
        }

        clear();
    }

    public Cell[][] getBoard(){
        return board;
    }

    public void shoot(int x, int y){
        shots++;
        //if(board[x][y].getImageId()==R.drawable.cloud) {//if cell is not yet fired at
            if (ships.getShip(x, y) != null) { // if cell fired at contains ship
                if(ships.getShip(x,y).shotAt(x,y)) {    //shoots at the cell and returns true if ship is sunk
                    sinkShip(ships.getShip(x,y).x,ships.getShip(x,y).y,ships.getShip(x,y).length,ships.getShip(x,y).direction);
                    hits++;
                    shipsSunk++;
                }
                else{
                    board[x][y].setImage(R.drawable.hit);
                    hits++;
                }
            }
            else{
                board[x][y].setImage(R.drawable.sea);  //missed
                misses++;
            }
        //}
    }
    public int shot(int x, int y){
        shots++;
        if (ships.getShip(x, y) != null) { // if cell fired at contains ship
            if(ships.getShip(x,y).shotAt(x,y)) {    //shoots at the cell and returns true if ship is sunk
                sinkShip(ships.getShip(x,y).x,ships.getShip(x,y).y,ships.getShip(x,y).length,ships.getShip(x,y).direction);
                hits++;
                shipsSunk++;
                System.out.println("ship sunk");
                return 2;
            }
            else{
                board[x][y].setImage(R.drawable.hit);
                hits++;
                System.out.println("hit");
                return 1;
            }
        }
        else{
            board[x][y].setImage(R.drawable.miss);  //missed
            misses++;
            System.out.println("miss");
            return 0;
        }

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
        if(ans){
            board[x][y].setImage(R.drawable.build2);
        }
        return ans;
    }

    public boolean pickAvailableShip(Cell c){
        boolean ans = false;
        int cX=c.getXCoord();
        int cY=c.getYCoord();
        if(cX==pX&&cY==pY){
            if(pSize==1){
                board[pX][pY].setImage(R.drawable.submarine);
                board[pX][pY].build(true);
                ships.addShip(new Ship(pX,pY,pSize,4));
                return true;
            }
            else {
                return false;
            }
        }
        if(c.getImageId()==R.drawable.build){
            ans = true;
            if(cX>pX) { //picked right
                placeShip(pX,pY,pSize,RIGHT);
            }
            if(cX<pX) { //picked left
                placeShip(pX,pY,pSize,LEFT);
            }
            if(cY>pY) { //picked down
                placeShip(pX,pY,pSize,DOWN);
            }
            if(cY<pY) { //picked up
                placeShip(pX,pY,pSize,UP);
            }
        }
        return ans;
    }



    public void placeShip(int x, int y, int s, int d){
        if(s==1){
            board[x][y].setImage(R.drawable.submarine);
            ships.addShip(new Ship(x,y,s,4));
            board[x][y].build(true);
        }
        else{
            if(d==RIGHT) {
                for (int i = 0; i < s; i++) {
                    if(i==0)
                        board[x+i][y].setImage(R.drawable.ship_end_right);
                    else if(i==s-1)
                        board[x+i][y].setImage(R.drawable.ship_front_right);
                    else
                        board[x+i][y].setImage(R.drawable.ship_middle_horizontal);
                    board[x+i][y].build(true);
                }
                ships.addShip(new Ship(x, y, s, 1));
            }
            if(d==LEFT) {
                for (int i = 0; i < s; i++) {
                    if(i==0)
                        board[x-i][y].setImage(R.drawable.ship_end_left);
                    else if(i==s-1)
                        board[x-i][y].setImage(R.drawable.ship_front_left);
                    else
                        board[x-i][y].setImage(R.drawable.ship_middle_horizontal);
                    board[x-i][y].build(true);
                }
                ships.addShip(new Ship(x, y, s, 3));
            }
            if(d==UP) {
                for (int i = 0; i < s; i++) {
                    if(i==0)
                        board[x][y-i].setImage(R.drawable.ship_end_up);
                    else if(i==s-1)
                        board[x][y-i].setImage(R.drawable.ship_front_up);
                    else
                        board[x][y-i].setImage(R.drawable.ship_middle_vertical);
                    board[x][y-i].build(true);
                }
                ships.addShip(new Ship(x, y, s, 0));
            }
            if(d==DOWN) {
                for (int i = 0; i < s; i++) {
                    if(i==0)
                        board[x][y+i].setImage(R.drawable.ship_end_down);
                    else if(i==s-1)
                        board[x][y+i].setImage(R.drawable.ship_front_down);
                    else
                        board[x][y+i].setImage(R.drawable.ship_middle_vertical);
                    board[x][y+i].build(true);
                }
                ships.addShip(new Ship(x, y, s, 2));
            }
        }
    }

    public void sinkShip(int x, int y, int s, int d){
        shipsRemaining[s-1]--;
        checkForGG();
        //System.out.println(shipsRemaining[s-1]+" "+Ships.shipNames[s-1]+"s remaining");
        if(s==1){
            board[x][y].setImage(R.drawable.submarine_hit);
            board[x][y].build(true);
        }
        else{
            if(d==RIGHT) {
                for (int i = 0; i < s; i++) {
                    if(i==0)
                        board[x+i][y].setImage(R.drawable.ship_end_right_hit);
                    else if(i==s-1)
                        board[x+i][y].setImage(R.drawable.ship_front_right_hit);
                    else
                        board[x+i][y].setImage(R.drawable.ship_middle_horizontal_hit);
                    board[x+i][y].build(true);
                }
            }
            if(d==LEFT) {
                for (int i = 0; i < s; i++) {
                    if(i==0)
                        board[x-i][y].setImage(R.drawable.ship_end_left_hit);
                    else if(i==s-1)
                        board[x-i][y].setImage(R.drawable.ship_front_left_hit);
                    else
                        board[x-i][y].setImage(R.drawable.ship_middle_horizontal_hit);
                    board[x-i][y].build(true);
                }
            }
            if(d==UP) {
                for (int i = 0; i < s; i++) {
                    if(i==0)
                        board[x][y-i].setImage(R.drawable.ship_end_up_hit);
                    else if(i==s-1)
                        board[x][y-i].setImage(R.drawable.ship_front_up_hit);
                    else
                        board[x][y-i].setImage(R.drawable.ship_middle_vertical_hit);
                    board[x][y-i].build(true);
                }
            }
            if(d==DOWN) {
                for (int i = 0; i < s; i++) {
                    if(i==0)
                        board[x][y+i].setImage(R.drawable.ship_end_down_hit);
                    else if(i==s-1)
                        board[x][y+i].setImage(R.drawable.ship_front_down_hit);
                    else
                        board[x][y+i].setImage(R.drawable.ship_middle_vertical_hit);
                    board[x][y+i].build(true);
                }
            }
        }
    }

    public void placeEnemyShip(int x, int y, int s, int d){

        ships.addShip(new Ship(x, y, s, d));

        for (int i = 0; i < s; i++) {
            if(d==RIGHT) {
                board[x + i][y].build(true);
            }
            if(d==LEFT) {
                board[x-i][y].build(true);
            }
            if(d==UP) {
                board[x][y-i].build(true);
            }
            if(d==DOWN) {
                board[x][y+i].build(true);
            }
        }
    }


    public void clearBuilds(){
        for(int x=0; x<numCells; x++){
            for(int y=0; y<numCells; y++){
                if(board[x][y].getImageId()==R.drawable.build||board[x][y].getImageId()==R.drawable.build2){
                    board[x][y].setImage(R.drawable.cloud);
                    //board[x][y].build(false);
                }
            }
        }
    }

    public void checkForGG(){
        boolean alive = false;
        for(int i=0; i<shipsRemaining.length; i++){
            if(shipsRemaining[i]>0){
                alive = true;
            }
        }
        if(!alive){
            System.out.println("Defeated");
            defeated = true;
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

    public void displayBoard(RelativeLayout r, int cellSize, Context c){
        int id = 1;
        r.removeAllViews();

        RelativeLayout.LayoutParams params;
        //System.out.println("CellSize: "+cellSize);
        for(int i=0; i<=numCells;i++)   //creates alpha grid on top of board
        {
            TextView a = new TextView(c);
            a.setId(id++);
            if(i!=0){
                a.setText(""+(i));
                a.setGravity(Gravity.CENTER);
            }
            if(i%2==0) {
                a.setBackgroundColor(Color.rgb(100, 100, 200));
            }
            else{
                a.setBackgroundColor(Color.rgb(50,50,150));
            }
            params = new RelativeLayout.LayoutParams(cellSize, cellSize);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            if(i==0) {
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
            else{
                params.addRule(RelativeLayout.RIGHT_OF,id-2);
            }
            a.setLayoutParams(params);
            r.addView(a);
        }
        for(int y=0; y<numCells; y++){
            TextView a = new TextView(c);
            a.setId(id++);

            a.setText(String.valueOf(MainActivity.alphabet[y]));
            a.setGravity(Gravity.CENTER);
            if(y%2==0) {
                a.setBackgroundColor(Color.rgb(50,50,150));
            }
            else{
                a.setBackgroundColor(Color.rgb(100,100,200));
            }
            params = new RelativeLayout.LayoutParams(cellSize, cellSize);
            params.addRule(RelativeLayout.BELOW,id-(numCells+1));
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            a.setLayoutParams(params);
            r.addView(a);

            for(int x=0; x<numCells; x++){
                board[x][y].setId(id++);
                params = new RelativeLayout.LayoutParams(cellSize, cellSize);
                params.addRule(RelativeLayout.BELOW,id-(numCells+2));
                params.addRule(RelativeLayout.RIGHT_OF, id - 2);
                board[x][y].setLayoutParams(params);
                board[x][y].setCoords(x, y);
                board[x][y].setScaleType(ImageView.ScaleType.CENTER_CROP);
                r.addView(board[x][y]);
            }
        }
    }

    public void clear(){
        for(int x=0; x<numCells; x++){
            for(int y=0; y<numCells; y++){
                board[x][y]=new Cell(context);
                board[x][y].setCoords(x, y);
            }
        }
        ships.clear();
    }

    static public Board createAiBoard(int n, Context c){
        System.out.println("Starting ai board build");
        Board ans = new Board(n, c);

        int[] ships = Ships.shipsToPlace;
        for (int i = ships.length - 1; i >= 0; i--) {
            System.out.println(Ships.shipNames[i]);
            for (int j = 0; j < ships[i]; j++) {
                System.out.println(j+1+" out of "+ ships[i]);
                int attempts = 100;
                outerLoop:
                while (true) {  //keeps trying until it can place a ship
                    attempts--;
                    //System.out.println("while loop attempts left:"+attempts);
                    int x = (int) (Math.random() * n);
                    int y = (int) (Math.random() * n);
                    int d = (int) (Math.random() * 4);
                    if (ans.getBoard()[x][y].isEmpty()) {
                        if (d == UP && ans.check(x, y, i + 1, UP)) {
                            ans.placeEnemyShip(x, y, i + 1, UP);
                            break;
                        }
                        if (d == RIGHT && ans.check(x, y, i + 1, RIGHT)) {
                            ans.placeEnemyShip(x, y, i + 1, RIGHT);
                            break;
                        }
                        if (d == DOWN && ans.check(x, y, i + 1, DOWN)) {
                            ans.placeEnemyShip(x, y, i + 1, DOWN);
                            break;
                        }
                        if (d == LEFT && ans.check(x, y, i + 1, LEFT)) {
                            ans.placeEnemyShip(x, y, i + 1, LEFT);
                            break;
                        }
                    }
                    if (attempts == 0) {    //was unable to find a place to put a ship randomly
                        System.out.println("Unable to place a ship for ai randomly, going into search mode");
                        for(x=0; x<n; x++){
                            for(y=0; y<n; y++){
                                if (ans.getBoard()[x][y].isEmpty()) {
                                    if (ans.check(x, y, i + 1, UP)) {
                                        ans.placeEnemyShip(x, y, i + 1, UP);
                                        System.out.println("Search mode succeeded");
                                        break outerLoop;
                                    }
                                    if (ans.check(x, y, i + 1, RIGHT)) {
                                        ans.placeEnemyShip(x, y, i + 1, RIGHT);
                                        System.out.println("Search mode succeeded");
                                        break outerLoop;
                                    }
                                    if (ans.check(x, y, i + 1, DOWN)) {
                                        ans.placeEnemyShip(x, y, i + 1, DOWN);
                                        System.out.println("Search mode succeeded");
                                        break outerLoop;
                                    }
                                    if (ans.check(x, y, i + 1, LEFT)) {
                                        ans.placeEnemyShip(x, y, i + 1, LEFT);
                                        System.out.println("Search mode succeeded");
                                        break outerLoop;
                                    }
                                }
                            }
                        }
                        System.out.println("Search mode failed, restarting method");
                        return createAiBoard(n,c);
                    }
                }
            }

        }
        return ans;
    }

    static public Board createPlayerBoard(int n, Context c){
        System.out.println("Starting player board build");
        Board ans = new Board(n, c);

        int[] ships = Ships.shipsToPlace;
        for (int i = ships.length - 1; i >= 0; i--) {
            System.out.println(Ships.shipNames[i]);
            for (int j = 0; j < ships[i]; j++) {
                System.out.println(j+1+" out of "+ ships[i]);
                int attempts = 100;
                outerLoop:
                while (true) {  //keeps trying until it can place a ship
                    attempts--;
                    //System.out.println("while loop attempts left:"+attempts);
                    int x = (int) (Math.random() * n);
                    int y = (int) (Math.random() * n);
                    int d = (int) (Math.random() * 4);
                    if (ans.getBoard()[x][y].isEmpty()) {
                        if (d == UP && ans.check(x, y, i + 1, UP)) {
                            ans.placeShip(x, y, i + 1, UP);
                            break;
                        }
                        if (d == RIGHT && ans.check(x, y, i + 1, RIGHT)) {
                            ans.placeShip(x, y, i + 1, RIGHT);
                            break;
                        }
                        if (d == DOWN && ans.check(x, y, i + 1, DOWN)) {
                            ans.placeShip(x, y, i + 1, DOWN);
                            break;
                        }
                        if (d == LEFT && ans.check(x, y, i + 1, LEFT)) {
                            ans.placeShip(x, y, i + 1, LEFT);
                            break;
                        }
                    }
                    if (attempts == 0) {    //was unable to find a place to put a ship randomly
                        System.out.println("Unable to place a ship for player randomly, going into search mode");
                        for(x=0; x<n; x++){
                            for(y=0; y<n; y++){
                                if (ans.getBoard()[x][y].isEmpty()) {
                                    if (ans.check(x, y, i + 1, UP)) {
                                        ans.placeShip(x, y, i + 1, UP);
                                        System.out.println("Search mode succeeded");
                                        break outerLoop;
                                    }
                                    if (ans.check(x, y, i + 1, RIGHT)) {
                                        ans.placeShip(x, y, i + 1, RIGHT);
                                        System.out.println("Search mode succeeded");
                                        break outerLoop;
                                    }
                                    if (ans.check(x, y, i + 1, DOWN)) {
                                        ans.placeShip(x, y, i + 1, DOWN);
                                        System.out.println("Search mode succeeded");
                                        break outerLoop;
                                    }
                                    if (ans.check(x, y, i + 1, LEFT)) {
                                        ans.placeShip(x, y, i + 1, LEFT);
                                        System.out.println("Search mode succeeded");
                                        break outerLoop;
                                    }
                                }
                            }
                        }
                        System.out.println("Search mode failed, restarting method");
                        return createPlayerBoard(n,c);
                    }
                }
            }

        }
        return ans;
    }

    public int getNumCells(){
        return numCells;
    }
    public int[] getStats(){
        int[] ans = {shots, hits, misses, shipsSunk};
        return ans;
    }
}


