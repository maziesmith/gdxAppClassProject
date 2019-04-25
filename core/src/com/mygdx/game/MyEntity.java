package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MyEntity {

    public Sprite sprt; //Entity Sprite
    public float posX; //horizontal position of sprite
    public float posY; //vertical position of sprite
    public float rot; //degrees of rotation
    public float sizeX;
    public float sizeY;
    public int health;
    public double fuelTimeCounter;
    public double noFuelCounter;
    public int fuel;// added fuel attribute
    public float speed;
    //new shit
    public Vector2 direction;
    public Vector2 velocity;
    public Bullet newBullet;

    public MyEntity(Sprite sprt_, float posX_, float posY_, float sizeX_, float sizeY_) {
        sprt = sprt_;
        posX = posX_;
        posY = posY_;
        sizeX = sizeX_;
        sizeY = sizeY_;
        speed = (float)(Gdx.graphics.getHeight()*.00694);//5
        rot = 0;
        health = 100;
        fuel = 100;  //initialized fuel
        fuelTimeCounter = 0;   // counter to decrease fuel  each second
        noFuelCounter = 0;      //counter for when fuel is at 0 start health each second
        sprt.setSize(sizeX_, sizeY_); //sets the size of the entity sprite
        sprt.setOriginCenter(); //sets the point of selection of the entity sprite to the center
        direction = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        this.sprt.setPosition(posX_, posY_); //sets the starting position of our entity
    }

    //new movement method dependant on angle of rotation
    public void movement() {

        direction.x = MathUtils.cosDeg(rot + 90);
        direction.y = MathUtils.sinDeg(rot + 90);
        velocity.x = direction.x * speed;
        velocity.y = direction.y * speed;
        posX += velocity.x;
        posY += velocity.y;
        sprt.setPosition(posX, posY);
    }

    //shoot bullets when pressed
    public void shoot(boolean isPressed) {
        if (isPressed){
            newBullet = new Bullet(posX + (sizeX/2), posY + (sizeY/2), 10, 10);
            newBullet.setTrajectory(this.rot);
            Bullet.bullets.add(newBullet);

            //if there are more than 10 bullets on screen, remove latest bullet shot.
            if (Bullet.bullets.size() > 10){
                Bullet.bullets.remove(0);
            }
            newBullet = null;
        }
    }


    //Manages the rotation of the ship
    public void rotation() {
        sprt.setPosition(posX - sprt.getWidth() / 2, posY - sprt.getHeight() / 2);

        float xInput = Gdx.input.getX();
        float yInput = (Gdx.graphics.getHeight() - Gdx.input.getY());

        float angle = (MathUtils.radiansToDegrees * MathUtils.atan2(yInput - posY, xInput - posX)) - 90;

        if (angle < 0) {
            angle += 360;
        }
        sprt.setRotation(angle);
        rot = angle;
    }

}
