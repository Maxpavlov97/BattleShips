package com.example.android.battleships;

import android.content.DialogInterface;
import android.graphics.Point;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Cell[][] board;
    int screenX;
    int screenY;
    int numCells;
    RelativeLayout boardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenX = size.x;
        screenY = size.y;
        numCells = 10;

        board = new Cell[numCells][numCells];
        boardView = (RelativeLayout)findViewById(R.id.boardLayout);
        //boardView = new RelativeLayout(this);
        System.out.println(R.string.app_name);

        //Cell test = new Cell(this);
        //test.setImage(Cell.ship_front_up);

       // boardView.addView(test);
        createBoard();
        boardView.invalidate();
        //setContentView(R.layout.game);
        //setContentView(boardView);

    }
    @Override
    public void onClick(View view)
    {
        Cell clicked = (Cell)view;
        clicked.setImage(R.drawable.sea);
    }

    public void createBoard()
    {
        int cellSize = (screenX-(16*2))/numCells;
        for(int x=0; x<numCells; x++)
        {
            //System.out.println("x="+x);
            for(int y=0; y<numCells; y++)
            {
                //System.out.println("y="+y);
                board[x][y]=new Cell(this,(x*numCells)+y);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(cellSize, cellSize);
                board[x][y].setScaleType(ImageView.ScaleType.FIT_CENTER);
                if(x==0) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    System.out.println("Parent Top");
                }
                else {
                    params.addRule(RelativeLayout.BELOW, ((x-1) * numCells));
                    System.out.println("Below:"+x*numCells);
                }
                if(y==0) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    System.out.println("Parent Left");
                }
                else {
                    params.addRule(RelativeLayout.RIGHT_OF, y-1);
                    System.out.println("Right of:"+y);
                }

                board[x][y].setLayoutParams(params);
                boardView.addView(board[x][y]);
                board[x][y].setOnClickListener(this);
                System.out.println(board[x][y]);
                System.out.println();

            }
        }
    }
}
