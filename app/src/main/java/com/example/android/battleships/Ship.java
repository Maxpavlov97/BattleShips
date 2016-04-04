package com.example.android.battleships;


public class Ship {

    int length;
    int direction;
    int x;
    int y;

    boolean isSunk;
    boolean[] isHit;
    int[][] coords;


    public Ship(int x, int y, int length, int direction){
        this.x=x;
        this.y=y;
        this.length=length;
        this.direction=direction;
        isHit = new boolean[length];
        isSunk = false;
        for(int i=0; i<length; i++){
            isHit[i]= false;
        }
        coords=new int[length][2];
        for(int i=0; i<length;i++){
            if(direction==0||direction==2) { //if vertical
                coords[i][0] = x;
                if(direction==0){   //up
                    coords[i][1]=y-i;
                }
                else{               //down
                    coords[i][1]=y+i;
                }
            }
            else if(direction==1||direction==3) { //if horizontal
                coords[i][1] = y;
                if(direction==1){ //right
                    coords[i][0]=x+i;
                }
                else{           //left
                    coords[i][0]=x-i;
                }
            }
        }
        if(length==1){
            coords[0][0]=x;
            coords[0][1]=y;
        }
    }
    
    public boolean isHere(int x, int y){
        for(int i=0; i<length; i++){
            if(coords[i][0]==x&&coords[i][1]==y){
                return true;
            }
        }
        return false;
    }

    public boolean shotAt(int x, int y){
        for(int i=0; i<length; i++){
            if(coords[i][0]==x && coords[i][1]==y){
                if(isHit[i]==false){
                    isHit[i]=true;
                    return checkSunk();
                }
            }
        }
        return false;
    }
    public boolean checkSunk(){
        boolean ans = true;
        for(int i=0; i<length; i++){
            if(isHit[i]==false){
                ans = false;
            }
        }
        isSunk = ans;
        return ans;
    }

    public String toString(){
        if(direction==0)
            return "X: "+x+" Y: "+y+" Length: "+length+" Up";
        if(direction==1)
            return "X: "+x+" Y: "+y+" Length: "+length+" Right";
        if(direction==2)
            return "X: "+x+" Y: "+y+" Length: "+length+" Down";
        if(direction==3)
            return "X: "+x+" Y: "+y+" Length: "+length+" Left";
        else
            return "X: "+x+" Y: "+y+" Length: "+length;
    }


}
