package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

import java.util.Vector;

public class Bullet {

    private static final Logger log = new Logger(GdxSpaceGameT1.class.getName(), Logger.DEBUG);

    public static Vector<Bullet> bullets = new Vector<Bullet>();
    public static Vector<Bullet> enemyBullets = new Vector<Bullet>();
    public Texture pellet;
    public Sprite sprt; //Entity Sprite
    public float posX; //horizontal position of sprite
    public float posY; //vertical position of sprite
    public float sizeX;
    public float sizeY;
    public float speed;
    public int lifeTimer = 1;
    public Vector2 direction;
    public Vector2 velocity;
    boolean bulletShot;

    public Bullet(float posX_, float posY_, int sizeX_, int sizeY_){
        pellet = new Texture("bullet.png");
        sprt = new Sprite(pellet);
        posX = posX_;
        posY = posY_;
        sizeX = sizeX_;
        sizeY = sizeY_;
        speed = (float)(Gdx.graphics.getHeight()*.01388);//10
        sprt.setSize(sizeX_, sizeY_); //sets the size of the entity sprite
        sprt.setOriginCenter(); //sets the point of selection of the entity sprite to the center
        direction = new Vector2(0,0);
        velocity = new Vector2(0,0);
        bulletShot = false;
        this.sprt.setPosition(posX_, posY_); //sets the starting position of our entity
    }

    //sets initial bullet trajectory
    public void setTrajectory(float rot) {
        if (!bulletShot){ //preserves bullet direction when shot once
            direction.x = MathUtils.cosDeg(rot+90);
            direction.y = MathUtils.sinDeg(rot+90);
            bulletShot = true;
            //log.debug("Bullet Shot");
        }
    }

    //moves bullets
    public void BulletMovement(){
        velocity.x = direction.x * speed;
        velocity.y = direction.y * speed;
        posX += velocity.x;
        posY += velocity.y;
        sprt.setPosition(posX, posY);

        //bullet destroyed after 2 seconds, lifeTimer counts every frame
        lifeTimer++;
        if (lifeTimer % 120 == 0){
            bullets.remove(this);
            enemyBullets.remove(this);
        }
        //log.debug("Bullet travels");
    }
}
