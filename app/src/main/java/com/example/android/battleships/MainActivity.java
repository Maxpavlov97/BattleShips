package com.example.android.battleships;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
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
    boolean clickable = true; //debounce for onclick
    boolean buildMode = true; //true if you are in the phase of putting down your ships.
    boolean buildPhase = true; //true if you are putting down the first point for a ship, false if you are selecting the final cell

    Ship ship;
    int shipLength;
    int shipsToPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ship_setup);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenX = size.x;
        screenY = size.y;
        numCells = 10;
        cellSize = screenX / (numCells + 1);
        shipLength = Ships.shipsToPlace.length;
        shipsToPlace = Ships.shipsToPlace[shipLength-1];

        playerBoard = new Board(numCells,this);
        board = playerBoard;

        //boardView = (RelativeLayout) findViewById(R.id.boardLayout);
        setUpBoard();
        boardView.setBackgroundColor(Color.rgb(0,162,232));
        System.out.println(R.string.app_name);


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
                        shipsToPlace--;
                        while (shipsToPlace == 0) { // figures out what ship is next to place
                            System.out.println("Done placing " + Ships.shipNames[shipLength-1] + "s");
                            shipLength--;
                            if (shipLength == 0) {
                                buildMode = false;
                                System.out.println("Finished placing Ships, game is ready to begin");
                                playGame();
                                return;
                            }
                            ((TextView)findViewById(R.id.shipName)).setText(""+Ships.shipNames[shipLength-1]);

                            ((TextView)findViewById(R.id.shipCounter)).setText(""+shipsToPlace);
                            shipsToPlace = Ships.shipsToPlace[shipLength-1];
                        }
                    }
                    else{
                        clearBuilds(new View(this));
                    }
                }

            }
            //game
        }
        else{ // firing
            if(yourTurn && !boardShown && board.getBoard()[x][y].getImageId()==(R.drawable.cloud)){
                board.shoot(x, y);
                System.out.println("player shot");
                yourTurn = false;
                System.out.println("about to pause");
                System.out.println("done pausing");
                //switchBoards();
                opponent.shoot();
                System.out.println("ai shot");
                System.out.println("about to pause");
                System.out.println("done pausing");
               // switchBoards();
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
        shipLength = Ships.shipsToPlace.length;
        shipsToPlace = Ships.shipsToPlace[shipLength-1];
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
}
