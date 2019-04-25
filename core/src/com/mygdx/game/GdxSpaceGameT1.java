package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Random;

import java.util.Vector;

public class GdxSpaceGameT1 implements ApplicationListener {


    //debugger
    private static final Logger log = new Logger(GdxSpaceGameT1.class.getName(), Logger.DEBUG);

    private MyEntity ship;
    private Enemy enemy;
    private Background background;
    private Vector<Enemy> enemies = new Vector<Enemy>();
    private FuelTank fuelBomb;
    private Location home;
    private Bullet newBullet;
    private Gbutton pause;
    private Gbutton shoot;

    private float mouseX;
    private float mouseY;
    private float mouseX2;
    private float mouseY2;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private SpriteBatch batch;
    private Texture img;
    private Sprite sprt;
    private BitmapFont font;
    private int frameDmgCounter;
    private boolean isDamaged;
    private int eventScreen;
    private int gameOverFrmCounter;
    private int unPauseFrmCounter;
    private int bulletShotCounter;
    private boolean canContinue;
    private boolean isPaused;
    public Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Random rand = new Random();

    private int enemiesEliminated;
    private int enemySpawnTick;
    private int spawnPosX;
    private int spawnPosY;

    //Screen size
    private int screenSizeX;
    private int screenSizeY;

    //World size
    private int worldSizeX;
    private int worldSizeY;

    //to get ratio of sprite size divide alpha sprite size by a ratio of 1080x720 (alpha screen size)

    @Override
    public void create() {
        enemiesEliminated = 0;
        enemySpawnTick = 1;
        bulletShotCounter = 1;
        frameDmgCounter = 0;


        //Screen Size
        screenSizeX = Gdx.graphics.getWidth();
        screenSizeY = Gdx.graphics.getHeight();

        //World size
        worldSizeX = screenSizeX * 3; // * screen to game ratio size
        worldSizeY = screenSizeY * 3; // * screen to game ratio size
        //World size is planned to be 12 times screen size

        Gdx.app.setLogLevel(Application.LOG_DEBUG); //initialize logger
        camera = new OrthographicCamera();
        viewport = new FitViewport(screenSizeX, screenSizeY, camera);

        batch = new SpriteBatch();
        //for background
        img = new Texture("WorldC1a.png");
        sprt = new Sprite(img);
        background = new Background(sprt, 0, 0, worldSizeX, worldSizeY);
        //for ship
        img = new Texture("ShipC1.png");
        sprt = new Sprite(img);
        ship = new MyEntity(sprt, worldSizeX/2, worldSizeY/2,(float)(screenSizeX*.046), (float)(screenSizeY*.104));
        //50/1080 = .046 for X, 75/720 = .104 for Y
        //for enemy
//        img = new Texture("EnemyC1.png");
//        sprt = new Sprite(img);
//        enemies.add(new Enemy(sprt, 400, 400, (float)(screenSizeX*.037), (float)(screenSizeY*.055))); //size=40,40
//        sprt = new Sprite(img);
//        enemies.add(new Enemy(sprt, 1000, 600,  (float)(screenSizeX*.037), (float)(screenSizeY*.055)));
//        sprt = new Sprite(img);
//        enemies.add(new Enemy(sprt, 100, 0,  (float)(screenSizeX*.037), (float)(screenSizeY*.055)));
        //for FuelBomb
        img = new Texture("FuelBomb.png");
        sprt = new Sprite(img);
        fuelBomb = new FuelTank(sprt, (float)(worldSizeX/2 *.8), (float)(worldSizeY/2 * .8), (float)(screenSizeX*.037), (float)(screenSizeY*.055));//size=40,40
        //for Home
        img = new Texture("home.png");
        sprt = new Sprite(img);
        home = new Location(sprt, (float)(worldSizeX/2 *1.2), (float)(worldSizeY/2 * 1.2), (float)(screenSizeX*.055), (float)(screenSizeY*.083));//size=60,60
        //hud text
        font = new BitmapFont(Gdx.files.internal("oswald-32.fnt"));
        frameDmgCounter = 0;
        eventScreen = 0;
        //for pause button
        img = new Texture("PauseBtn.png");
        sprt = new Sprite(img);
        pause = new Gbutton(sprt, ship.posX,ship.posY + screenSizeY/2, (float)(screenSizeX*.065), (float)(screenSizeY*.097));//size=70,70
        //for shooting button
        img = new Texture("ShootBtn.png");
        sprt = new Sprite(img);
        shoot = new Gbutton(sprt, (ship.posX + screenSizeX/2) -300 , (ship.posY - screenSizeY/2) + 100, (float)(screenSizeX*.150), (float)(screenSizeY*.242));//size=200,200


        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds((ship.posX - screenSizeX/2) + 25 , (ship.posY - screenSizeY/2) + 25, (float)(screenSizeX*.138), (float)(screenSizeY*.208));//size=150,150


        //Create a Stage and add TouchPad
        stage = new Stage(viewport,batch);
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);

        img = null;
        sprt = null;
    }

    @Override
    public void resize(int i, int i1) {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

    }

    @Override
    public void render() {
        //Start Screen
        if (eventScreen == 0) {
            drawStartScreen();
        }
        //Game Screen
        if (eventScreen == 1) {
            drawGame();
        }
        if (eventScreen == 4) {
            drawPause();
        }
        //Game Over Screen
        if (eventScreen == 5) {
            drawGameOver();
        }
        if (eventScreen == 6) {
            //drawWinning Screen
        }
    }

    //Draws Start Screen (Event 0)
    public void drawStartScreen() {
        boolean leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        Gdx.gl.glClearColor(0, 0, 0, 0);//render black background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch,
                "Touch the screen to play!",
                Gdx.graphics.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);
        if (leftPressed) {
            eventScreen = 1;
        }
        batch.end();
    }

    //Draws Game Screen (Event 1)
    public void drawGame() {
        isPaused = false;

        //mouse and touch inputs
        mouseX = Gdx.input.getX()+(ship.sprt.getX() - Gdx.graphics.getWidth()/2); //get mouse/touch horizontal position
        mouseY = Gdx.input.getY() + (ship.sprt.getY() + Gdx.graphics.getHeight()/2-70 ); //get mouse/touch vertical position,touch measured from starting from the top towards the bottom.
        mouseX2 = Gdx.input.getX(1)+(ship.sprt.getX() - Gdx.graphics.getWidth()/2); //get mouse/touch horizontal position
        mouseY2 = Gdx.input.getY(1) + (ship.sprt.getY() + Gdx.graphics.getHeight()/2-70 ); //get mouse/touch vertical position,touch measured from starting from the top towards the bottom.
        boolean leftPressed = Gdx.input.isTouched(0); //returns true if button/screen is pressed
        boolean rightPressed = Gdx.input.isTouched(1); //returns true if button/screen is pressed

        // sets the touchpad in the corner of the screen while and it will move with the ship
        touchpad.setBounds((ship.posX - Gdx.graphics.getWidth()/2) + 100 , (ship.posY - Gdx.graphics.getHeight()/2) + 100, (float)(screenSizeX*.138  ), (float)(screenSizeY*.208));

        //updates the position of the pause button sprite and values
        pause.sprt.setPosition(ship.posX , (ship.posY + Gdx.graphics.getHeight()/2) - pause.sprt.getHeight());
        pause.posX= pause.sprt.getX();
        pause.posY= pause.sprt.getY()+pause.sprt.getHeight()/2;

        //updates the position of the shoot button sprite and values
        shoot.sprt.setPosition((ship.posX + Gdx.graphics.getWidth()/2) - 300,(ship.posY - Gdx.graphics.getHeight()/2) + 100);
        shoot.posX = shoot.sprt.getX();
        shoot.posY = shoot.sprt.getY();

        gameOverFrmCounter = 1;
        unPauseFrmCounter++;
        bulletShotCounter++;
        enemySpawnTick++;



    //To verify coordinates with mouse
        if(leftPressed) {
            log.debug("Mouse Y: " + (mouseY-(float)((screenSizeY*1.528))));
            //log.debug("pause Y: " + pause.posY);
            log.debug("shoot Y: " + (shoot.posY));
            //log.debug("Ship pos: " + ship.posY);
        }


        Gdx.gl.glClearColor(0, 0, 0, 0);//render black background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //calls function that utilizes the touchpad as input for player movement

        shipMovement(leftPressed);

        //Formula to get the positon of the touch X position when the ship is moving outside the screen dimensions:(Gdx.input.getX()+(ship.sprt.getX() - Gdx.graphics.getWidth()/2))
        //(touch position in X relative to our screen dimensions+(position of the ship outside screen dimensions - screenwidth/2)
        //Formula to get the position of the touch Y position when the ship is moving outside the screen dimensions: Gdx.input.getY() + (ship.sprt.getY() + Gdx.graphics.getHeight()/2-70 )
        //(touch position in Y relative to our screen dimensions+((position of the ship outside screen dimensions - shipheight) + screenheight/2 )

        //If mouse is pressed on pause button, pause the game
        if (leftPressed && unPauseFrmCounter>60 && mouseX > pause.sprt.getX()
                && mouseX < (pause.sprt.getX()+ pause.sprt.getWidth())
                && mouseY >= pause.posY  && mouseY <= (pause.posY + ship.sprt.getHeight())
                ){
            eventScreen = 4;
            isPaused = true;
        }

        //If second finger is pressed on pause button while pressing the screen somewhere else pause the game
        if (rightPressed && unPauseFrmCounter>60 && mouseX2 > pause.sprt.getX()
                && mouseX2 < (pause.sprt.getX()+ pause.sprt.getWidth())
                && mouseY2 >= pause.posY  && mouseY2 <= (pause.posY + ship.sprt.getHeight())
        ){
            eventScreen = 4;
            isPaused = true;
        }

        // mouseY position - shootY position = roughly 1100, ratio of screenY (720/1100) = 1.528

        //If mouse is pressed on shoot button, shoot
        if (leftPressed && (bulletShotCounter % 15 == 0) && mouseX > shoot.sprt.getX()
                && mouseX < (shoot.sprt.getX()+ shoot.sprt.getWidth())
                && (mouseY - (float)((screenSizeY*1.528))) <= (shoot.posY + shoot.sprt.getHeight()/2 ) && (mouseY - (float)((screenSizeY*1.528))) >= (shoot.posY - shoot.sprt.getHeight()/2)
        ){
            ship.shoot(leftPressed);
        }


        //If mouse is pressed on shoot button, shoot
        if (rightPressed && (bulletShotCounter % 10 == 0) && mouseX2 > shoot.sprt.getX()
                && mouseX2 < (shoot.sprt.getX()+ shoot.sprt.getWidth())
                && (mouseY2 - (float)((screenSizeY*1.528))) <= (shoot.posY + shoot.sprt.getHeight()/2 ) && (mouseY2 - (float)((screenSizeY*1.528))) >= (shoot.posY - shoot.sprt.getHeight()/2)
        ){
            ship.shoot(leftPressed);
        }

        //spawn enemies
        if(enemies.size() < 10)
        {
            if (enemySpawnTick % 300 == 0) {
                spawnPosX = rand.nextInt(worldSizeX);
                spawnPosY = rand.nextInt(worldSizeY);
                enemies.add(new Enemy(0, spawnPosX, spawnPosY, (float)(screenSizeX * .037), (float)(screenSizeY * .055))); //size=40,40
                log.debug("Enemy Spawned!");
            }
        }



        /*collision test for Fuel Tanks
        after 3600 frames (1 minute) ship can once again be refueled*/
        setFuelBomb();

        //Tracks player fuel and health
        playerStatusModifier();

        //enemy movement
        for (int i = 0; i < enemies.size(); i++) {

            enemyMovement(i);
            enemies.get(i).enemyShoot(ship);
            float enemyPosX = enemies.get(i).posX;
            float enemyPosY = enemies.get(i).posY;

            //enemy bullet detection and elimination
            for (int j = 0; j < Bullet.bullets.size(); j++) {
                float bulletPosX = Bullet.bullets.get(j).posX;
                float bulletPosY = Bullet.bullets.get(j).posY;

                if (bulletPosX < enemyPosX + 25 && bulletPosX > enemyPosX - 25 && bulletPosY < enemyPosY + 25 && bulletPosY > enemyPosY - 25){
                    enemies.remove(i);
                    Bullet.bullets.remove(j);
                    j--;
                    enemiesEliminated++;
                }
            }
        }


        //bullet movement
        for(int i = 0; i < Bullet.bullets.size(); i++){
            Bullet.bullets.get(i).BulletMovement();
        }
        //enemy bullet movement
        for(int i = 0; i < Bullet.enemyBullets.size(); i++){
            float bulletPosX = Bullet.enemyBullets.get(i).posX;
            float bulletPosY = Bullet.enemyBullets.get(i).posY;
            Bullet.enemyBullets.get(i).BulletMovement();

            //bullet collision on player ship
            if (bulletPosX < ship.posX + 25 && bulletPosX > ship.posX - 25 && bulletPosY < ship.posY + 25 && bulletPosY > ship.posY - 25){
                Bullet.enemyBullets.remove(i);
                i--;
                ship.health -= 10;
            }

        }

        //respawn point
        if (home.isUsed == false &&
                ship.posX < home.posX + 70 && ship.posX > home.posX - 70 && ship.posY < home.posY + 70 && ship.posY > home.posY - 70) {
            home.isUsed = true;
            log.debug("Spawn Point Set!");
        }

        //draw sprites into the screen
        batch.begin();
        //render background
        background.sprt.draw(batch);
        //ship.newBullet.sprt.draw(batch);
        //if ship health health reaches zero, don't render the ship
        if (ship.health > 0) {
            ship.sprt.draw(batch);  //draws ship while it has health
        } else {
            eventScreen = 5;
        }
        //draw enemy on the screen
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).sprt.draw(batch);
        }

        //draw bullets on the screen
        for(int i = 0; i < Bullet.bullets.size(); i++){
            Bullet.bullets.get(i).sprt.draw(batch);
            //log.debug("bullet " + i + " drawn");
        }

        //draw enemy bullets on the screen
        for(int i = 0; i < Bullet.enemyBullets.size(); i++){
            Bullet.enemyBullets.get(i).sprt.draw(batch);
            //log.debug("bullet " + i + " drawn");
        }

        //render bomb while active. if the fuel bomb has been used don't render while isUsed is true.
        if (fuelBomb.isUsed == false)
            fuelBomb.sprt.draw(batch);

        home.sprt.draw(batch);

        //Manages the movement of the camera with the ship
        camera.position.y = ship.sprt.getY();
        camera.position.x = ship.sprt.getX();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        //ends camera movement code segement

        //Draws the health left
        font.draw(batch,
                "Ship Health = " + ship.health + " | ",
                (ship.posX - Gdx.graphics.getWidth()/2) +20f,
                (ship.posY + Gdx.graphics.getHeight()/2) - 20f);

       //Draws the fuel left
        font.draw(batch,
                " Ship Fuel = " + ship.fuel,
                (ship.posX - Gdx.graphics.getWidth()/2) +250f,
                (ship.posY + Gdx.graphics.getHeight()/2) - 20f);

        //Draws eliminations
        //Draws the fuel left
        font.draw(batch,
                " Enemies Eliminated = " + enemiesEliminated + " | Enemies on Stage = " + enemies.size(),
                (ship.posX - Gdx.graphics.getWidth()/2) + 20f,
                (ship.posY + Gdx.graphics.getHeight()/2) - 60f);

        pause.sprt.draw(batch);
        shoot.sprt.draw(batch);

        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        //Confirms a successfully rendered frame
        //log.debug("Game Running...");
    }

    public void drawPause(){
        mouseX = Gdx.input.getX(); //get mouse/touch horizontal position
        mouseY = Gdx.input.getY() + (ship.sprt.getY() + Gdx.graphics.getHeight()/2-70 ); //get mouse/touch vertical position,touch measured from starting from the top towards the bottom.
        mouseX2 = Gdx.input.getX(1)+(ship.sprt.getX() - Gdx.graphics.getWidth()/2); //get mouse/touch horizontal position
        mouseY2 = Gdx.input.getY(1) + (ship.sprt.getY() + Gdx.graphics.getHeight()/2-70 ); //get mouse/touch vertical position,touch measured from starting from the top towards the bottom.
        boolean rightPressed = Gdx.input.isTouched(1); //returns true if button/screen is pressed
        boolean leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT); //returns true if button/screen is pressed
        unPauseFrmCounter = 1;

        if (gameOverFrmCounter % 60 == 0) {
            canContinue = true;
            //log.debug("Count:" + gameOverFrmCounter + "Can Continue!");
        } else {
            canContinue = false;
            gameOverFrmCounter++;
        }

        if (leftPressed && canContinue && (mouseX+(ship.sprt.getX() - Gdx.graphics.getWidth()/2)) > pause.sprt.getX()
                && (mouseX+(ship.sprt.getX() - Gdx.graphics.getWidth()/2)) < pause.sprt.getX()+ pause.sprt.getWidth()
                && mouseY >= pause.posY  && mouseY <= (pause.posY + ship.sprt.getHeight())){
            eventScreen = 1;
        }

        //If second finger is pressed on pause button while pressing the screen somewhere else pause the game
        if (rightPressed && canContinue && mouseX2 > pause.sprt.getX()
                && mouseX2 < (pause.sprt.getX()+ pause.sprt.getWidth())
                && mouseY2 >= pause.posY  && mouseY2 <= (pause.posY + ship.sprt.getHeight())
        ){
            eventScreen = 1;
        }


        batch.begin();

        //render background
        background.sprt.draw(batch);
        //ship.newBullet.sprt.draw(batch);
        //if ship health health reaches zero, don't render the ship
        if (ship.health > 0) {
            ship.sprt.draw(batch);  //draws ship while it has health
        } else {
            eventScreen = 5;
        }
        //draw enemy on the screen
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).sprt.draw(batch);
        }

        //draw bullets on the screen
        for(int i = 0; i < Bullet.bullets.size(); i++){
            Bullet.bullets.get(i).sprt.draw(batch);
            //log.debug("bullet " + i + " drawn");
        }

        //draw enemy bullets on the screen
        for(int i = 0; i < Bullet.enemyBullets.size(); i++){
            Bullet.enemyBullets.get(i).sprt.draw(batch);
            //log.debug("bullet " + i + " drawn");
        }

        //render bomb while active. if the fuel bomb has been used don't render while isUsed is true.
        if (fuelBomb.isUsed == false)
            fuelBomb.sprt.draw(batch);

        home.sprt.draw(batch);

        //Manages the movement of the camera with the ship
        camera.position.y = ship.sprt.getY();
        camera.position.x = ship.sprt.getX();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        //ends camera movement code segement

        //Draws the health left
        font.draw(batch,
                "Ship Health = " + ship.health + " | ",
                (ship.posX - Gdx.graphics.getWidth()/2) +20f,
                (ship.posY + Gdx.graphics.getHeight()/2) - 20f);

        //Draws the fuel left
        font.draw(batch,
                " Ship Fuel = " + ship.fuel,
                (ship.posX - Gdx.graphics.getWidth()/2) +250f,
                (ship.posY + Gdx.graphics.getHeight()/2) - 20f);

        //Draws eliminations
        //Draws the fuel left
        font.draw(batch,
                " Enemies Eliminated = " + enemiesEliminated + " | Enemies on Stage = " + enemies.size(),
                (ship.posX - Gdx.graphics.getWidth()/2) + 20f,
                (ship.posY + Gdx.graphics.getHeight()/2) - 60f);

        pause.sprt.draw(batch);
        //shoot.sprt.draw(batch);
        font.draw(batch,
                "GAME PAUSED!",
                camera.position.x,
                camera.position.y);
        batch.end();

    }

    //Draws Game Over Screen (Event 5)
    public void drawGameOver() {
        boolean leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        //after 3 seconds you can continue playing game
        if (gameOverFrmCounter % 180 == 0) {
            canContinue = true;
            log.debug("Count:" + gameOverFrmCounter + "Can Continue!");
        } else {
            canContinue = false;
            gameOverFrmCounter++;
        }

        //reset ship attributes
        ship.health = 100;
        ship.fuel = 100;
        //set spawn point if home is used
        if (home.isUsed) {
            ship.sprt.setPosition(home.posX, home.posY);
            ship.posX = home.posX;
            ship.posY = home.posY;
        }

        Gdx.gl.glClearColor(0, 0, 0, 0);//render black background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch,
                "Game Over!\nTouch the screen to keep playing...",
                camera.position.x,
                camera.position.y);
        if (leftPressed && canContinue) {
            eventScreen = 1;
        }
        batch.end();
    }


    public void enemyMovement(int i) {
            enemies.get(i).rotation(ship);
            enemies.get(i).movement(ship);
    }



    public void shipMovement(boolean pressed){
        //set the ship's position using the speed and  on each of its axis components(x & y)

        if(ship.fuel != 0) {
            ship.sprt.setX(ship.sprt.getX() + touchpad.getKnobPercentX() * ship.speed);
            ship.sprt.setY(ship.sprt.getY() + touchpad.getKnobPercentY() * ship.speed);
        }
        else{
            ship.sprt.setX(ship.sprt.getX() + touchpad.getKnobPercentX() * (ship.speed/2));
            ship.sprt.setY(ship.sprt.getY() + touchpad.getKnobPercentY() * (ship.speed/2));
        }

        //ship movement now considers world bounds
        if (ship.posX < screenSizeX/4){
            ship.sprt.setX(ship.sprt.getX() + (float)(worldSizeY * 0.0012));
        }
        else if (ship.posX > worldSizeX - screenSizeX/4){
            ship.sprt.setX(ship.sprt.getX() - (float)(worldSizeY * 0.0012));
        }

        if (ship.posY < screenSizeY/4){
            ship.sprt.setY(ship.sprt.getY() + (float)(worldSizeY * 0.0012));
        }
        else if (ship.posY > worldSizeY - screenSizeY/4){
            ship.sprt.setY(ship.sprt.getY() - (float)(worldSizeY * 0.0012));
        }

        ship.posX = ship.sprt.getX();
        ship.posY = ship.sprt.getY();

        float xInput = touchpad.getKnobPercentX();
        float yInput = touchpad.getKnobPercentY();

        float angle = (MathUtils.radiansToDegrees * MathUtils.atan2(yInput , xInput )) - 90;

        if (angle < 0) {
            angle += 360;
        }

        //log.debug("knob percent = " + touchpad.getKnobPercentX());

        if ( touchpad.getKnobPercentX() > .1 || touchpad.getKnobPercentY() > .1 || touchpad.getKnobPercentX() < -.1 || touchpad.getKnobPercentY() < -.1) {
            ship.sprt.setRotation(angle);
            ship.rot = angle;
        }
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();

    }

    public void setFuelBomb() {
        if (fuelBomb.frameFuelCounter % 420 == 0) {
            fuelBomb.isUsed = false;
        }
        // if the ship collides with tank fully restore the ships fuel
        if (fuelBomb.isUsed == false &&
                ship.posX < fuelBomb.posX + 40 && ship.posX > fuelBomb.posX - 40 && ship.posY < fuelBomb.posY + 40 && ship.posY > fuelBomb.posY - 40) {

            ship.fuel = 100;
            fuelBomb.isUsed = true;
            log.debug("Ship refueled!");
        }
        //count every frame after fuel tank is used
        if (fuelBomb.isUsed == true) {
            fuelBomb.frameFuelCounter++;
        }
    }

    public void playerStatusModifier() {
        //If the ship is on the screen without fuel decrease health by one each second
        if (ship.fuel == 0) {
            //Takes 1 health every second
            ship.noFuelCounter = ship.noFuelCounter + 0.01666666666666666666666666666667;
            // if a second has passed reset the counter and decrease ship fuel by 1
            if (ship.noFuelCounter > 1) {
                ship.noFuelCounter = 0;
                if (ship.fuel == 0) {
                    ship.health -= 1;
                }
            }
        }

        //If the ship is on the screen decrease fuel by one each second
        if (ship.health > 0) {
            //Takes 1 fuel every second
            ship.fuelTimeCounter = ship.fuelTimeCounter + 0.01666666666666666666666666666667;
            // if a second has passed reset the counter and decrease ship fuel by 1
            if (ship.fuelTimeCounter > 1) {
                ship.fuelTimeCounter = 0;
                if (ship.fuel != 0)
                    ship.fuel -= 1;
            }

            //collision test for enemy
            //after 60 frames (1 second) ship can once again take damage
            if (frameDmgCounter % 60 == 0) {
                isDamaged = false;
            }
            // if the ship collides with enemy take off the enemy collision damage from player health and set the isdamaged variable to true so we can not take damage for a second
            for (int i = 0; i < enemies.size(); i++) {
                if (isDamaged == false &&
                        ship.posX < enemies.get(i).posX + 20 && ship.posX > enemies.get(i).posX - 20 &&
                        ship.posY < enemies.get(i).posY + 20 && ship.posY > enemies.get(i).posY - 20) {
                    ship.health -= enemies.get(i).collisionDmg;
                    if (ship.health < 0)
                        ship.health = 0;
                    isDamaged = true;
                    log.debug("Damage Taken!");
                }
            }
            //count every frame after damage is taken
            if (isDamaged == true) {
                frameDmgCounter++;
            }
        }
    }


}
