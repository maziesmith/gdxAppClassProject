package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Background {

    public Sprite sprt; //Entity Sprite
    public float posX; //horizontal position of sprite
    public float posY; //vertical position of sprite
    public float rot; //degrees of rotation
    public int sizeX;
    public int sizeY;

    public Background(Sprite sprt_, int posX_, int posY_, int sizeX_, int sizeY_) {
        sprt = sprt_;
        posX = posX_;
        posY = posY_;
        sizeX = sizeX_;
        sizeY = sizeY_;
        sprt.setSize(sizeX_, sizeY_); //sets the size of the entity sprite
        this.sprt.setPosition(posX_, posY_); //sets the starting position of our entity
    }
}
