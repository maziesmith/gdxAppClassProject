package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    public Sprite sprt; //Entity Sprite
    public float posX; //horizontal position of sprite
    public float posY; //vertical position of sprite
    public float rot; //degrees of rotation
    public float sizeX;
    public float sizeY;
    public float speed;
    public Bullet newBullet;
    public int collisionDmg;
    public Vector2 direction;
    public Vector2 velocity;
    public int enemyType;
    public int enemyShootTimer = 1;
    private Texture texture0 = new Texture("EnemyC1.png");
    private float screenSizeX = Gdx.graphics.getWidth();
    private float screenSizeY = Gdx.graphics.getHeight();

    public Enemy(int enemyType_, float posX_, float posY_, float sizeX_, float sizeY_) {
        posX = posX_;
        posY = posY_;
        sizeX = sizeX_;
        sizeY = sizeY_;
        rot = 0;
        direction = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        //enemy type 0 is enemy that stops and shoots from a distance
        if (enemyType_ == 0){
            enemyType = 0;
            sprt = new Sprite(texture0);
        }
        else { //enemy type 1 crashes with player ship
            enemyType = 1;
            //create sprite type 1
        }
        speed = (float)(Gdx.graphics.getHeight()*.00277);//2
        collisionDmg = 20;

        sprt.setSize(sizeX_, sizeY_); //sets the size of the entity sprite
        sprt.setOriginCenter(); //sets the point of selection of the entity sprite to the center
        sprt.setPosition(posX_, posY_); //sets the starting position of our entity
    }

    public void rotation(MyEntity player) {
        sprt.setPosition(posX - sprt.getWidth() / 2, posY - sprt.getHeight() / 2);
        float angle = (MathUtils.radiansToDegrees * MathUtils.atan2(player.posY - posY, player.posX - posX)) - 90;

        if (angle < 0) {
            angle += 360;
        }
        sprt.setRotation(angle);
        rot = angle;
    }

    public void enemyShoot(MyEntity player) {
        enemyShootTimer++;
        if (player.posX < posX + screenSizeX/2 && player.posX > posX - screenSizeX/2 && player.posY < posY + screenSizeY/2 && player.posY > posY - screenSizeY/2 && enemyShootTimer % 90 == 0) {
            newBullet = new Bullet(posX + (sizeX / 2), posY + (sizeY / 2), 10, 10);
            newBullet.setTrajectory(this.rot);
            Bullet.enemyBullets.add(newBullet);

            //if there are more than 10 bullets on screen, remove latest bullet shot.
            if (Bullet.enemyBullets.size() > 10) {
                Bullet.enemyBullets.remove(0);
            }
            newBullet = null;
        }
    }

    public void movement(MyEntity player) {

        direction.x = MathUtils.cosDeg(rot + 90);
        direction.y = MathUtils.sinDeg(rot + 90);
        velocity.x = direction.x * speed;
        velocity.y = direction.y * speed;
        if (enemyType == 0 && player.posX < posX + screenSizeX/4 && player.posX > posX - screenSizeX/4 && player.posY < posY + screenSizeY/4 && player.posY > posY - screenSizeY/4) {
            posX += 0;
            posY += 0;
        }
        else{
            posX += velocity.x;
            posY += velocity.y;
        }
        sprt.setPosition(posX, posY);
    }
}
