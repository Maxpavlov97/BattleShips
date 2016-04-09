package com.example.android.battleships;

/**
 * Created by Max on 3/29/2016.
 */
public class Ships {
    static public int[] shipsToPlace = {2,2,1,1,1};
    static public String[] shipNames = {"Submarine","Destroyer","Cruiser","Battleship","Aircraft Carrier"};

    Ship[] ships;
    int i=0;

    public Ships(){
        ships = new Ship[totalShips()];
    }

    public void addShip(Ship a){
        ships[i++]=a;
        //System.out.println(a);
    }

    public Ship getShip(int x, int y){
        for(Ship ship:ships){
            if(ship.isHere(x,y)){
                return ship;
            }
        }
        return null;
    }

    static public int totalShips(){
        int ans=0;
        for(int i=0; i<shipsToPlace.length; i++){
            ans+= shipsToPlace[i];
        }
        return ans;
    }
    public void clear(){
        ships = new Ship[totalShips()];
        i=0;
    }
}
