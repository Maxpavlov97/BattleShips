package com.example.android.battleships;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    int screenX;
    int screenY;
    int numCells;

    RelativeLayout boardView;
    Board board;

    boolean clickable = true; //debounce for onclick
    boolean buildMode = true; //true if you are in the phase of putting down your ships.
    boolean buildPhase = true; //true if you are putting down the first point for a ship, false if you are selecting the final cell

    Ship ship;
    int shipLength;
    int shipsToPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenX = size.x;
        screenY = size.y;
        numCells = 6;
        shipLength = Ship.shipsToPlace.length;
        shipsToPlace = Ship.shipsToPlace[shipLength-1];

        board = new Board(numCells);

        boardView = (RelativeLayout) findViewById(R.id.boardLayout);
        boardView.setBackgroundColor(Color.rgb(0,162,232));
        System.out.println(R.string.app_name);

        createBoard();


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
                        while (shipsToPlace == 0) {
                            System.out.println("Done placing " + Ship.shipNames[shipLength-1] + "s");
                            shipLength--;
                            if (shipLength == 0) {
                                buildMode = false;
                                System.out.println("Finished placing Ships, game is ready to begin");
                                return;
                            }
                            shipsToPlace = Ship.shipsToPlace[shipLength-1];
                        }
                    }
                    else{
                        System.out.println("ship not built try again");
                    }
                }

            }
            //game
        }
    }
    //*/


    public void createBoard()
    {
        int id = 1;
        int cellSize = (int)Math.floor(screenX - (0 * 2)) / (numCells + 1);
        RelativeLayout.LayoutParams params;
        System.out.println("ScreenSize: "+screenX);
        System.out.println("CellSize: "+cellSize);
        for(int i=0; i<=numCells;i++)   //creates alpha grid on top of board
        {
            TextView a = new TextView(this);
            a.setId(id++);
            if(i!=0){
                a.setText(""+(i));
                a.setGravity(Gravity.CENTER);
            }
            if(i%2==0) {
                a.setBackgroundColor(Color.rgb(100,100,200));
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
            boardView.addView(a);
        }
        for(int y=0; y<numCells; y++){
            TextView a = new TextView(this);
            a.setId(id++);

            a.setText(String.valueOf(alphabet[y]));
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
            boardView.addView(a);

            for(int x=0; x<numCells; x++){
                board.getBoard()[x][y] = new Cell(this, id++);
                params = new RelativeLayout.LayoutParams(cellSize, cellSize);
                params.addRule(RelativeLayout.BELOW,id-(numCells+2));
                params.addRule(RelativeLayout.RIGHT_OF,id-2);
                board.getBoard()[x][y].setLayoutParams(params);
                board.getBoard()[x][y].setOnClickListener(this);
                board.getBoard()[x][y].setCoords(x, y);
                board.getBoard()[x][y].setScaleType(ImageView.ScaleType.CENTER_CROP);
                boardView.addView(board.getBoard()[x][y]);
            }
        }

    }

    public void resetBoard(View v){
        createBoard();
        buildMode=true;
        buildPhase=true;
        shipLength = Ship.shipsToPlace.length;
        shipsToPlace = Ship.shipsToPlace[shipLength-1];
    }
}
