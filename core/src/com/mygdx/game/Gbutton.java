package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Gbutton {
    public Sprite sprt; //Entity Sprite
    public float posX; //horizontal position of sprite
    public float posY; //vertical position of sprite
    public float sizeX;
    public float sizeY;
    public boolean isUsed;  //check if tank has been used

    public Gbutton(Sprite sprt_, float posX_, float posY_, float sizeX_, float sizeY_) {
        sprt = sprt_;
        posX = posX_;
        posY = posY_;
        sizeX = sizeX_;
        sizeY = sizeY_;
        isUsed = false; //holds if the fuel tank has been used
        sprt.setSize(sizeX_, sizeY_); //sets the size of the entity sprite
        sprt.setOriginCenter(); //sets the point of selection of the entity sprite to the center
        sprt.setPosition(posX_, posY_); //sets the starting position of our entity
    }


}
