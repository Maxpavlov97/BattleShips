package com.example.android.battleships;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    int screenX;
    int screenY;
    int numCells;
    int cellSize;

    RelativeLayout boardView;
    Board board;
    Board playerBoard;
    Board opponentBoard;
    ai opponent;

    boolean boardShown = true; //true if playerBoard is shown, false if opponents

    boolean yourTurn;
    boolean gameOver = false;
    boolean clickable = true; //debounce for onclick
    boolean buildMode = true; //true if you are in the phase of putting down your ships.
    boolean buildPhase = true; //true if you are putting down the first point for a ship, false if you are selecting the final cell

    Ship ship;
    int shipLength;
    int shipsLeftToPlace;
    //public int[] shipsToPlace = {3,2,2,1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ship_setup);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenX = size.x;
        screenY = size.y;

        numCells=10;

        mainMenu();



    }

    @Override
    public void onClick(View view) {
        Cell a = (Cell) view;
        int x = a.getXCoord();
        int y = a.getYCoord();
        System.out.println(a.getId() + ": " + y + ", " + (x));
        //*
        if (clickable) {
            if (buildMode) {
                while (shipsLeftToPlace == 0) { // figures out what ship is next to place
                    //System.out.println("Done placing " + Ships.shipNames[shipLength-1] + "s");
                    shipLength--;
                    if (shipLength == 0) {
                        buildMode = false;
                        System.out.println("Finished placing Ships, game is ready to begin");
                        playGame();
                        return;
                    }

                    shipsLeftToPlace = Ships.shipsToPlace[shipLength-1];
                }
                if (buildPhase) {
                    if (board.drawAvailableShips(x, y, shipLength)) {  //returns true if it was able to draw ships
                        buildPhase = false; //allows the program to proceed to picking a ship to build
                        System.out.println("potential ships drawn");
                    } else {
                        System.out.println("cant draw potential ships");
                    }
                }
                else {
                    if (board.pickAvailableShip(a)) {
                        buildPhase = true;
                        System.out.println("ship built");
                        board.clearBuilds();
                        shipsLeftToPlace--;
                        while (shipsLeftToPlace == 0) { // figures out what ship is next to place
                            //System.out.println("Done placing " + Ships.shipNames[shipLength-1] + "s");
                            shipLength--;
                            if (shipLength == 0) {
                                buildMode = false;
                                System.out.println("Finished placing Ships, game is ready to begin");
                                playGame();
                                return;
                            }
                            shipsLeftToPlace = Ships.shipsToPlace[shipLength-1];
                        }

                        ((TextView)findViewById(R.id.shipName)).setText("" + Ships.shipNames[shipLength - 1]);

                        ((TextView)findViewById(R.id.shipCounter)).setText(""+shipsLeftToPlace);

                    }
                    else{
                        clearBuilds(new View(this));
                    }
                }

            }
            //game
        }
        else if(!gameOver){ // firing
            if(yourTurn && !boardShown && board.getBoard()[x][y].getImageId()==(R.drawable.cloud)){
                board.shoot(x, y);
                //System.out.println("player shot");
                yourTurn = false;
                if(opponentBoard.defeated){
                    runVictoryScreen(true);
                    return;
                }
                //System.out.println("about to pause");
                //System.out.println("done pausing");
                pause(500);
                switchBoards();
                pause(500);
                opponent.shoot();
                pause(500);
                //System.out.println("ai shot");
                if(playerBoard.defeated){
                    runVictoryScreen(false);
                    return;
                }
                pause(500);
                //System.out.println("about to pause");
                //System.out.println("done pausing");
                switchBoards();
                pause(500);
                yourTurn = true;
            }
        }

    }
    //*/


    public void createBoard()
    {
        boardView.removeAllViews();
        board.displayBoard(boardView, cellSize, this);
        for(int x=0; x<numCells; x++){
            for(int y=0; y<numCells; y++){
                board.getBoard()[x][y].setOnClickListener(this);
            }
        }
    }

    public void clearBuilds(View view){
        if(!buildPhase&&buildMode) {
            board.clearBuilds();
            buildPhase = true;
        }
    }


    public void switchBoards(View view){
            if(boardShown){
                boardShown = false;
                board = opponentBoard;
                ((TextView)findViewById(R.id.switchBoard)).setText("Your Board");
            }
            else{
                boardShown = true;
                board = playerBoard;
                ((TextView)findViewById(R.id.switchBoard)).setText("Opponent's Board");
            }
            boardView.removeAllViews();
            createBoard();

    }

    public void switchBoards(){
        if(boardShown){
            boardShown = false;
            board = opponentBoard;
            ((TextView)findViewById(R.id.switchBoard)).setText("Switch To Your Board");
        }
        else{
            boardShown = true;
            board = playerBoard;
            ((TextView)findViewById(R.id.switchBoard)).setText("Switch To Opponent's Board");
        }
        boardView.removeAllViews();
        createBoard();

    }

    public void resetBoard(View v){
        boardView.removeAllViews();
        board.clear();
        createBoard();
        buildMode=true;
        buildPhase=true;
        //for(int i=Ships.shipsToPlace.length-1; i>=0; i--){
           // if(Ships.shipsToPlace[i]!=0){
                shipLength = Ships.shipsToPlace.length;
                shipsLeftToPlace = Ships.shipsToPlace[shipLength-1];
           // }
        //}
    }

    public void randomPlayerBoard(View view){
        playerBoard=Board.createPlayerBoard(numCells, this);
        board = playerBoard;
        playGame();
    }

    public void setUpBoard(){
        setContentView(R.layout.ship_setup);
        boardView = (RelativeLayout) findViewById(R.id.buildLayout);
        clickable = true;
        buildMode = true;
        buildPhase = true;
        for(int i=Ships.shipsToPlace.length-1; i>=0; i--){
            if(Ships.shipsToPlace[i]!=0){
                ((TextView)findViewById(R.id.shipName)).setText(""+Ships.shipNames[i]);
                ((TextView)findViewById(R.id.shipCounter)).setText(""+Ships.shipsToPlace[i]);
                break;
            }
        }

        createBoard();
    }

    public void playGame(){
        setContentView(R.layout.game);
        boardView.removeAllViews();
        boardView = (RelativeLayout) findViewById(R.id.boardLayout);
        createBoard();
        opponentBoard = Board.createAiBoard(numCells,this);
        opponent = new ai(playerBoard);
        clickable = false;
        yourTurn = true;
        switchBoards();
    }

    public void pause(int i){
        boardView.invalidate();
        try {
            Thread.sleep(i);
        }
        catch (InterruptedException e){
            System.out.print("Exception thrown");
        }

    }

    public void makeAiFire(View view) {
        opponent.shoot();
    }

    public void runVictoryScreen(boolean didPlayerWin){
        gameOver=true;
        setContentView(R.layout.end_screen);
        TextView txt = (TextView) findViewById(R.id.youwon);
        if(didPlayerWin) {
            txt.setText("You Won!");
            ((TextView)findViewById(R.id.youwon2)).setText("Nice Work!");
        }
        else{
            txt.setText("You Lost.");
            ((TextView)findViewById(R.id.youwon2)).setText("Better luck next time.");
        }
        int[] stats = opponentBoard.getStats();

        txt = (TextView) findViewById(R.id.shots1);
        txt.setText(stats[0]+" Shots Fired");

        txt = (TextView) findViewById(R.id.hits1);
        txt.setText(stats[1]+" Hits");

        txt = (TextView) findViewById(R.id.misses1);
        txt.setText(stats[2]+" Misses");

        txt = (TextView) findViewById(R.id.percentage1);
        if(stats[0]!=0)
            txt.setText(((100*stats[1])/stats[0])+"% Accuracy");

        txt = (TextView) findViewById(R.id.sunk1);
        txt.setText(stats[3]+" Ships Sunk");


        stats = playerBoard.getStats();

        txt = (TextView) findViewById(R.id.shots2);
        txt.setText(stats[0]+" Shots Fired");

        txt = (TextView) findViewById(R.id.hits2);
        txt.setText(stats[1]+" Hits");

        txt = (TextView) findViewById(R.id.misses2);
        txt.setText(stats[2]+" Misses");

        txt = (TextView) findViewById(R.id.percentage2);
        if(stats[0]!=0)
            txt.setText(((100*stats[1])/stats[0])+"% Accuracy");

        txt = (TextView) findViewById(R.id.sunk2);
        txt.setText(stats[3]+" Ships Sunk");
    }

    public void mainMenu(View view){
        mainMenu();
    }

    public void mainMenu(){
        setContentView(R.layout.main_menu);
    }

    public void play(View view){
        play();
    }

    public void play(){
        cellSize = screenX / (numCells + 1);
        shipLength = Ships.shipsToPlace.length;
        shipsLeftToPlace = Ships.shipsToPlace[shipLength-1];


        playerBoard = new Board(numCells,this);
        board = playerBoard;

        System.out.println("AircraftCarrier: "+Ships.shipsToPlace[3]);
        System.out.println("Battleships: "+Ships.shipsToPlace[2]);
        System.out.println("Cruisers: "+Ships.shipsToPlace[1]);
        System.out.println("Submarines: "+Ships.shipsToPlace[0]);

        //boardView = (RelativeLayout) findViewById(R.id.boardLayout);
        setUpBoard();
        boardView.setBackgroundColor(Color.rgb(0, 162, 232));
        System.out.println(R.string.app_name);
        gameOver=false;
    }

    public void settings(View view){
        settings();
    }

    public void settings(){
        setContentView(R.layout.settings);
        ((TextView)findViewById(R.id.boardsize)).setText(numCells + "x" + numCells);
        ((TextView)findViewById(R.id.aircraft)).setText(""+Ships.shipsToPlace[3]);
        ((TextView)findViewById(R.id.battleship)).setText(""+Ships.shipsToPlace[2]);
        ((TextView)findViewById(R.id.cruiser)).setText(""+Ships.shipsToPlace[1]);
        ((TextView)findViewById(R.id.subs)).setText("" + Ships.shipsToPlace[0]);
    }

    public void howToPlay(View view){
        howToPlay();
    }

    public void howToPlay(){
        setContentView(R.layout.how_to_play);

    }

    public void settingsIncrement(View view){
        if(view.getId()==R.id.boardSize_minus && enoughSpaceForAllTheseShips(-1)){
            numCells--;
            ((TextView)findViewById(R.id.boardsize)).setText(numCells + "x" + numCells);
        }
        if(view.getId()==R.id.boardSize_plus && numCells<26){
            numCells++;
            ((TextView)findViewById(R.id.boardsize)).setText(numCells + "x" + numCells);
        }
        if(view.getId()==R.id.aircraft_minus && Ships.shipsToPlace[3]>0 && checkForShips()){
            Ships.shipsToPlace[3]--;
            ((TextView)findViewById(R.id.aircraft)).setText(""+Ships.shipsToPlace[3]);
        }
        if(view.getId()==R.id.aircraft_plus && enoughSpaceForAllTheseShips(4)){
            Ships.shipsToPlace[3]++;
            ((TextView)findViewById(R.id.aircraft)).setText(""+Ships.shipsToPlace[3]);
        }
        if(view.getId()==R.id.battleship_minus && Ships.shipsToPlace[2]>0 && checkForShips()){
            Ships.shipsToPlace[2]--;
            ((TextView)findViewById(R.id.battleship)).setText(""+Ships.shipsToPlace[2]);
        }
        if(view.getId()==R.id.battleship_plus && enoughSpaceForAllTheseShips(3)){
            Ships.shipsToPlace[2]++;
            ((TextView)findViewById(R.id.battleship)).setText(""+Ships.shipsToPlace[2]);
        }
        if(view.getId()==R.id.cruiser_minus && Ships.shipsToPlace[1]>0 && checkForShips()){
            Ships.shipsToPlace[1]--;
            ((TextView)findViewById(R.id.cruiser)).setText(""+Ships.shipsToPlace[1]);
        }
        if(view.getId()==R.id.cruiser_plus && enoughSpaceForAllTheseShips(2)){
            Ships.shipsToPlace[1]++;
            ((TextView)findViewById(R.id.cruiser)).setText(""+Ships.shipsToPlace[1]);
        }
        if(view.getId()==R.id.subs_minus && Ships.shipsToPlace[0]>0 && checkForShips()){
            Ships.shipsToPlace[0]--;
            ((TextView)findViewById(R.id.subs)).setText(""+Ships.shipsToPlace[0]);
        }
        if(view.getId()==R.id.subs_plus && enoughSpaceForAllTheseShips(1)){
            Ships.shipsToPlace[0]++;
            ((TextView)findViewById(R.id.subs)).setText(""+Ships.shipsToPlace[0]);
        }
    }

    public boolean enoughSpaceForAllTheseShips(int s){
        int ships=s;
        int space = numCells*numCells;
        if(s==-1){
            ships=0;
            space = (numCells-1)*(numCells-1);
        }
        //space+=numCells*4; //ships placed on the border block off less space
        for(int i=0; i<Ships.shipsToPlace.length; i++){
            ships+=(i+1)*Ships.shipsToPlace[i];
            //ships+=(2+((i+1)*2))/2;
        }
        if(ships==0){
            return false;
        }
        else if(space/ships>2){
            return true;
        }
        return false;
    }

    public boolean checkForShips() {
        int counter = 0;
        for (int i = 0; i < Ships.shipsToPlace.length; i++) {
            counter+=Ships.shipsToPlace[i];
        }
        return counter > 1;
    }
}
