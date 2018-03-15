package me.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.spacegame.SpaceGame;
import me.spacegame.animations.Explosion;
import me.spacegame.gameobjects.Background;
import me.spacegame.gameobjects.Comet;
import me.spacegame.gameobjects.Enemy;
import me.spacegame.gameobjects.EnemyRocket;
import me.spacegame.gameobjects.Meteor;
import me.spacegame.gameobjects.Player;
import me.spacegame.gameobjects.Rocket;
import me.spacegame.powerups.PowerUp;
import me.spacegame.powerups.PowerUpClear;
import me.spacegame.powerups.PowerUpComet;
import me.spacegame.powerups.PowerUpControl;
import me.spacegame.powerups.PowerUpHealth;
import me.spacegame.powerups.PowerUpHelper;
import me.spacegame.powerups.PowerUpLaser;
import me.spacegame.powerups.PowerUpObject;
import me.spacegame.powerups.PowerUpPacMan;
import me.spacegame.powerups.PowerUpRapidFire;
import me.spacegame.ui.HealthBar;
import me.spacegame.ui.menu.MainMenu;
import me.spacegame.ui.menu.Menu;
import me.spacegame.util.Scale;

/**
 * Created by Felix on 09-May-17.
 */

public class GameScreen implements Screen, InputProcessor, GestureDetector.GestureListener {


    private Menu menu;
    private static final int SHAKETIME = 150;

    public SpaceGame game;

    private SpriteBatch batch = new SpriteBatch();
    private Stage stage = new Stage();


    private Music backgroudMusic;
    private OrthographicCamera camera;

    public List<Meteor> meteors = new ArrayList<Meteor>();
    public List<Rocket> rockets = new ArrayList<Rocket>();
    public List<Explosion> explosions = new ArrayList<Explosion>();
    public List<Enemy> enemies = new ArrayList<Enemy>();
    private List<PowerUp> activePowerUps = new ArrayList<PowerUp>();
    private List<PowerUpObject> powerUpObjects = new ArrayList<PowerUpObject>();
    public List<Comet> comets = new ArrayList<Comet>();
    private Background background;

    public static Random random = new Random();

    public PowerUp currentPowerUp;
    private Touchpad touchpad;

    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadskin;
    private Drawable touchknob;
    private Drawable touchbackground;
    public Player player;
    public Preferences preferences = Gdx.app.getPreferences("sghighscore");
    private Enemy enemy0;
    private Enemy enemy1;
    public HealthBar healthBar;
    private int powerUpDropRate = 12; // 1 : x  where x..value default:12

    private InputMultiplexer inputMultiplexer;
    private long shakeCamTimer = 0;
    private Explosion ex;

    public boolean paused = false;

    //richtiger Pfusch!! (OOP)
    private double enemy0SpawnTimer;
    private double enemy1SpawnTimer;
    private double enemy0Spawner;
    private double enemy1Spawner;
    private double meteorSpawnTimer;
    private double meteorSpawner;
    private double enemy0SpawnerSubtract;
    private double enemy0SpawnSubtractTimer;
    private double enemy0SpawnerSubtractValue;

    public GameScreen(SpaceGame game) {
        this.game = game;
        background = new Background(this);
        backgroudMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/backgroundmusic.mp3"));

        float wernertime = System.nanoTime();

        batch = new SpriteBatch();
        stage = new Stage();


        System.out.println("Gamescreen load time (Batch and Stage): " + (System.nanoTime() - wernertime)/1000000000);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SpaceGame.VIEWPORTWIDTH, SpaceGame.VIEWPORTHEIGHT);

        ex = new Explosion((int) Scale.getScaledSizeX(300), (int) Scale.getScaledSizeY(300), (int) Scale.getScaledSizeX(230), (int) Scale.getScaledSizeY(230), this);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(inputMultiplexer);


        touchpadskin = new Skin();
        touchpadStyle = new Touchpad.TouchpadStyle();

        touchpadskin.add("touchBackground", game.getTexture("touchBackground"));
        touchpadskin.add("touchKnob", game.getTexture("touchKnob"));

        touchbackground = touchpadskin.getDrawable("touchBackground");
        touchknob = touchpadskin.getDrawable("touchKnob");


        touchpadStyle.background = touchbackground;
        touchpadStyle.knob = touchknob;

        touchpad = new Touchpad(0, touchpadStyle);
        touchpad.setBounds(Scale.getScaledSizeX(90), Scale.getScaledSizeX(90), Scale.getScaledSizeX(400), Scale.getScaledSizeX(400));


        stage.addActor(touchpad);

        for (int i = 0; i < 4; i++) {
            meteors.add(new Meteor(this));
        }


        enemy0SpawnTimer = System.currentTimeMillis();
        enemy0SpawnSubtractTimer = System.currentTimeMillis();
        enemy1SpawnTimer = System.currentTimeMillis();
        meteorSpawnTimer = System.currentTimeMillis();

        //Timer, when a new Enemy0 will spawn
        enemy0Spawner = 9000;
        //Timer, when the enemy0Spawner gets lower
        enemy0SpawnerSubtract = 10000;
        //Value, which is subtstracted form enemy0Spawner after enemy0SpawnerSubtract+enemy0Spawner
        enemy0SpawnerSubtractValue = 500;
        //Timer, when enemy1 will spawn
        enemy1Spawner = 19000;
        //Timer, when new meteor will spawn
        meteorSpawner = 15000;


        player = new Player(this);

        currentPowerUp = null;
        if(game.username.equalsIgnoreCase("RNDL"))
        {
            currentPowerUp = new PowerUpComet(player, this);
        }

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        System.out.println("Gamescreen load time (Before Healthbar): " + (System.nanoTime() - wernertime)/1000000000);

        healthBar = new HealthBar(this);
        System.out.println("Gamescreen load time (After Healthbar): " + (System.nanoTime() - wernertime)/1000000000);

        long timepeta = System.currentTimeMillis();
        menu = new Menu(this);

        System.out.println("S" + (System.currentTimeMillis() - timepeta));

        //Pause Button
        //pausebtnup = game.getTexture("settingsButton");
        //pausebtndown = game.getTexture("settingsButton");

        //pausebtnupdrawable= new SpriteDrawable();
        //pausebtndowndrawable = new SpriteDrawable();

        //pausebtnupdrawable.setSprite(new Sprite(pausebtnup));
        //pausebtndowndrawable.setSprite(new Sprite(pausebtndown));

        /* settingsbutton buggy */
        //settingsbtn = new ImageButton(pausebtnupdrawable, pausebtndowndrawable);

        //settingsbtn.setPosition(camera.viewportWidth - 150, camera.viewportHeight - 150);
        //stage.addActor(settingsbtn);

        healthBar.setPowerupCooldown(200);
        System.out.println("GameScreen load time (Complete Constructor): " + (System.nanoTime() - wernertime)/1000000000);

        game.getSound("startgamesound").play(game.soundVolume);
        backgroudMusic.setLooping(true);
        backgroudMusic.setVolume(game.soundVolume);
        backgroudMusic.play();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update();
        draw();
    }

    public void draw()
    {

        batch.begin();

        background.draw(batch);



        for(Enemy e : enemies)
        {
            e.draw(batch);
        }

        for (Meteor m : meteors) {
            m.draw(batch);
        }

        for (Rocket r : rockets) {
            r.draw(batch);
        }

        for(Explosion e : explosions)
        {
            e.draw(batch);
        }

        //Render PowerUps

        for(PowerUp p : activePowerUps)
        {
            p.draw(batch);
        }

        for(PowerUpObject p : powerUpObjects)
        {
            p.draw(batch);
        }

        player.draw(batch);
        healthBar.draw(batch);
        batch.end();

        stage.draw();
        menu.draw();

        healthBar.draw();
    }

    public void update()
    {
        player.score+=1;

        //update
        if (System.currentTimeMillis() - shakeCamTimer < SHAKETIME)
        {
            float dx = (float) Math.sin(System.currentTimeMillis());
            float dy = (float) Math.sin(2 * System.currentTimeMillis());

            camera.position.set(SpaceGame.VIEWPORTWIDTH/2 + dx * 10 , SpaceGame.VIEWPORTHEIGHT/2 + dy * 10 , 0);
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            healthBar.setProjectionMatrix(camera.combined);
        }
        camera.position.set(SpaceGame.VIEWPORTWIDTH/2, SpaceGame.VIEWPORTHEIGHT/2, 0);

        player.updatePosition(touchpad);

        //Spawn Enemy0 after Seconds
        if((System.currentTimeMillis()-enemy0SpawnTimer)>enemy0Spawner)
        {
            enemies.add(new Enemy(0, this));
            enemy0SpawnTimer=System.currentTimeMillis();
        }

        //Spawn Enemy1 after Seconds
        outerloop:
        if((System.currentTimeMillis()-enemy1SpawnTimer)>enemy1Spawner)
        {
            for(int i = 0; i<enemies.size(); i++)
            {
                if(enemies.get(i).type==1)
                {
                    enemy1SpawnTimer=System.currentTimeMillis();
                    break outerloop;
                }
            }
            enemies.add(new Enemy(1, player, this));
            enemy1SpawnTimer=System.currentTimeMillis();
        }

        //Spawn new Meteors over time
        if((System.currentTimeMillis()-meteorSpawnTimer)>meteorSpawner)
        {
            meteors.add(new Meteor(this));
            meteorSpawnTimer=System.currentTimeMillis();
        }

        //Spawn Enemy0 more frequently
        if((System.currentTimeMillis()-enemy0SpawnSubtractTimer)>enemy0SpawnerSubtract+enemy0Spawner)
        {
            if(enemy0Spawner>1000)
            {
                enemy0Spawner-=enemy0SpawnerSubtractValue;
                enemy0SpawnSubtractTimer=System.currentTimeMillis();
            }
        }


        //Remove offscreen enemies
        for(int i = 0; i<enemies.size(); i++)
        {
            if(enemies.get(i).type==0)
            {
                if(enemies.get(i).enemyX<=-enemies.get(i).enemyWidth)
                {
                    enemies.remove(enemies.get(i));
                }
            }
        }

        //Meteor - Enemy Collision
        outerloop:
        for (int i = 0; i < meteors.size(); i++)
        {
            for(int j = 0; j<enemies.size(); j++)
            {
                if (Intersector.overlaps(meteors.get(i).box, enemies.get(j).box) && enemies.get(j).type==1) {
                    enemies.get(j).health -= 50;
                    explosions.add(new Explosion((int) enemies.get(j).enemyX, (int) (enemies.get(j).enemyY), (int) enemies.get(j).enemyWidth, (int) enemies.get(j).enemyHeight, this));
                    if (enemies.get(j).health <= 0) {
                        enemies.remove(enemies.get(j));
                        game.getSound("explosion1sound").play(game.soundVolume);
                        break outerloop;
                    }
                }
            }
        }

        //Meteor  -  Player Collision
        for (int i = 0; i < meteors.size(); i++) {
            if (Intersector.overlaps(meteors.get(i).box, player.box)) {
                // System.out.println(System.currentTimeMillis() - meteors.get(i).lastTimeHit);
                if((System.currentTimeMillis() - meteors.get(i).lastTimeHit) > 1200)
                {
                    meteors.get(i).lastTimeHit = System.currentTimeMillis();
                    damagePlayer(meteors.get(i).damage);
                }
            }

            // Remove offscreen meteors
            if (meteors.get(i).x < -meteors.get(i).radius) {
                if(!meteors.get(i).divided  || (meteors.get(i).speedy==0 && meteors.get(i).divided))
                {
                    meteors.add(new Meteor(this));
                }
                meteors.remove(i);

            }
        }

        //Remove offscreen rockets
        for (int i = 0; i < rockets.size(); i++) {
            if (rockets.get(i).x >= SpaceGame.VIEWPORTWIDTH) {
                rockets.remove(rockets.get(i));
            }
        }


        //Rocket  -  Meteor collision
        outerloop:
        for (int i = 0; i < rockets.size(); i++) {
            for (int j = 0; j < meteors.size(); j++) {
                if (Intersector.overlaps(meteors.get(j).box, rockets.get(i).box)) {
                    meteors.get(j).health -= rockets.get(i).damage;
                    rockets.remove(rockets.get(i));
                    meteors.get(j).updateTexture();
                    explosions.add(new Explosion((int) meteors.get(j).x - 70, (int) (meteors.get(j).y - 20), (int) meteors.get(j).radius*2, (int) meteors.get(j).radius*2, this));

                    //Spawn Powerup after destroy
                    if (meteors.get(j).health <= 0) {
                        if(random.nextInt(powerUpDropRate)==0)
                        {
                            powerUpObjects.add(new PowerUpObject(meteors.get(j), this));
                        }

                        //Pfusch, aber mit Methode gehts nit
                        if(meteors.get(j).divided)
                        {
                            Meteor m1 = new Meteor(this);
                            m1.x = meteors.get(j).x;
                            m1.y = meteors.get(j).y;
                            m1.radius = meteors.get(j).radius;
                            m1.divided = false;
                            m1.speedy = meteors.get(j).speed;
                            m1.texture = meteors.get(j).texture;
                            m1.meteorsprite = new Sprite(meteors.get(j).meteorsprite);
                            meteors.add(m1);
                            Meteor m2 = new Meteor(this);
                            m2.x = meteors.get(j).x;
                            m2.y = meteors.get(j).y;
                            m2.divided = true;
                            m2.speedy = -meteors.get(j).speed;
                            m2.radius = meteors.get(j).radius;
                            m2.texture = meteors.get(j).texture;
                            m2.meteorsprite = new Sprite(meteors.get(j).meteorsprite);
                            meteors.add(m2);
                        }
                        else
                        {
                            meteors.add(new Meteor(this));
                        }
                        meteors.remove(j);
                        game.getSound("explosion1sound").play(game.soundVolume);
                        player.score+=100;
                    }
                    break outerloop;
                }
            }
        }

        //Rocket   -  Enemy Collision
        outerloop:
        for (int i = 0; i < rockets.size(); i++) {
            for (int j = 0; j<enemies.size(); j++) {
                if (Intersector.overlaps(rockets.get(i).box, enemies.get(j).box)) {
                    rockets.remove(rockets.get(i));
                    enemies.get(j).health -= 50;
                    explosions.add(new Explosion((int) enemies.get(j).enemyX - 70, (int) (enemies.get(j).enemyY - 20), (int) enemies.get(j).enemyWidth, enemies.get(j).enemyHeight, this));
                    if (enemies.get(j).health <= 0) {
                        enemies.remove(enemies.get(j));
                        game.getSound("explosion1sound").play(game.soundVolume);
                        player.score+=500;
                    }
                    break outerloop;

                }
            }
        }

        //EnemyRockets  -  Player/Meteor collision
        //Enemy - Player collision
        for(Enemy e : enemies)
        {
            for(EnemyRocket er : e.getRockets())
            {
                if(Intersector.overlaps(er.box, player.box) )
                {
                    //only hit player once
                    if(!er.hasHitPlayer)
                    {
                        er.hasHitPlayer = true;
                        damagePlayer(er.damage);
                    }
                }
                for (int j = 0; j < meteors.size(); j++)
                {
                    if (Intersector.overlaps(meteors.get(j).box, er.box))
                    {
                        explosions.add(new Explosion((int) meteors.get(j).x - 70, (int) (meteors.get(j).y - 20), (int) meteors.get(j).radius*2, (int) meteors.get(j).radius*2, this));
                        meteors.remove(j);
                        game.getSound("explosion1sound").play(game.soundVolume);
                        meteors.add(new Meteor(this));
                    }
                }
            }
            if(Intersector.overlaps(e.box, player.box))
            {
                if((System.currentTimeMillis() - e.lastTimeHit) > 1200)
                {
                    e.lastTimeHit = System.currentTimeMillis();
                    damagePlayer(e.damage);
                }
            }
        }

        //Enemy Rocket - Enemy Collision
        outerloop:
        for(int i = 0; i<enemies.size(); i++)
        {
            for(int l = 0; l<enemies.size(); l++)
            {
                for(int h = 0; h<enemies.get(l).getRockets().size(); h++)
                {
                    if(Intersector.overlaps(enemies.get(i).box, enemies.get(l).getRockets().get(h).box))
                    {
                        explosions.add(new Explosion((int) enemies.get(i).enemyX - 70, (int) (enemies.get(i).enemyY - 20), (int) enemies.get(i).enemyWidth, (int) enemies.get(i).enemyHeight, this));
                        enemies.remove(i);
                        game.getSound("explosion1sound").play(game.soundVolume);
                        break outerloop;
                    }
                }
            }
        }

        //Player - PowerUpObject Collision
        outerloop:
        for(int i = 0; i<powerUpObjects.size(); i++)
        {
            if((Intersector.overlaps(powerUpObjects.get(i).box, player.box)) && currentPowerUp==null)
            {
                System.out.println(powerUpObjects.get(i).box.toString() + " : "+ player.box.toString());
                powerUpObjects.remove(powerUpObjects.get(i));


                switch(random.nextInt(8))
                {
                    case 7:
                        currentPowerUp = new PowerUpLaser(player, this);
                        break outerloop;
                    case 6:
                        currentPowerUp = new PowerUpComet(player, this);
                        break outerloop;
                    case 5:
                        currentPowerUp = new PowerUpPacMan(player, this);
                        break outerloop;
                    case 4:
                        currentPowerUp = new PowerUpHelper(player, this);
                        break outerloop;
                    case 3:
                        currentPowerUp = new PowerUpClear(player, this);
                        break outerloop;
                    case 2:
                        currentPowerUp = new PowerUpControl(player, this);
                        break outerloop;
                    case 1:
                        currentPowerUp = new PowerUpHealth(player, this);
                        break outerloop;
                    case 0:
                        currentPowerUp = new PowerUpRapidFire(player, this);
                        break outerloop;
                    default:
                        currentPowerUp = new PowerUpPacMan(player, this);
                        break outerloop;
                }

            }
        }

        background.update();

        for(Enemy e : enemies)
        {
            e.update();
        }
        for (Rocket r : rockets) {
            r.update();
        }
        for(int i = 0; i < explosions.size(); i++)
        {
            if(explosions.get(i).update())
            {
                explosions.remove(i);
            }
        }

        //update powerups
        //remove finished powerups
        outerloop:
        for(int i = 0; i<activePowerUps.size(); i++)
        {
            if(!activePowerUps.get(i).update())
            {
                activePowerUps.remove(activePowerUps.get(i));
                currentPowerUp=null;
                break outerloop;
            }

            PowerUp powerup = activePowerUps.get(i);
            //double timer = powerup.duration - powerup.timer;
            System.out.println(100-powerup.getCooldown());                                  //-=sdfjkl;sdahfjksdfksdahfkdsjajf;ksldajfklsdajfkl;sadjfklsdajfkl;sdajfklsdajfkl;asdjfklds;ajf;klsadjfklsdjfklsda
            //double perc = (timer / powerup.duration) * 100;
            //if(perc < 4) perc = -100;
            healthBar.setPowerupCooldown(powerup.getCooldown());

        }
        //healthBar.setPowerupCooldown(0);
        for (Meteor m : meteors) {
            m.update();
        }

        //update powerupobjects
        outerloop:
        for(int i = 0; i<powerUpObjects.size(); i++)
        {
            powerUpObjects.get(i).update();
            if(powerUpObjects.get(i).x+powerUpObjects.get(i).width<0)
            {
                powerUpObjects.remove(i);
                break outerloop;
            }
        }

        healthBar.update();
        stage.act();

        if(!player.dead)
        {
            healthBar.score = player.score + 1;
        }
    }


    public void damagePlayer(int damage)
    {
        //damage = 4;
        player.health -= damage;
        healthBar.setAbsuloteHealth(player.health);
        shakeCam();
        game.getSound("damagesound").play(game.soundVolume);
        if (healthBar.getHealth() <= 0.2) {
            gameOver();
        }
    }

    private void gameOver() {
        if(!player.dead)
        {
            backgroudMusic.stop();
            player.setVisible(false);
            explosions.add(new Explosion((int) player.x, (int) player.y, true));

            game.getSound("gameoversound").play(game.soundVolume);
            menu.currentMenu = menu.screens.get("gameover").activate();
            player.dead = true;
            healthBar.setAbsuloteHealth(0);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    public void activatePowerUp()
    {
        if(currentPowerUp!=null && activePowerUps.size()==0)
        {
            activePowerUps.add(currentPowerUp);
            currentPowerUp.start();
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX >= SpaceGame.VIEWPORTWIDTH / 2 && player.visible)
        {
            if(activePowerUps.size()>=1)
            {
                if(!(activePowerUps.get(0) instanceof PowerUpHelper))
                {
                    if(rockets.size() < 5)
                    {
                        rockets.add(new Rocket(player));
                        if(!(activePowerUps.get(0) instanceof PowerUpControl))
                        {
                            game.getSound("shot1sound").play(game.soundVolume);
                        }
                        else
                        {
                            game.getSound("shot2sound").play(game.soundVolume);
                        }
                    }
                }
                else
                {
                    rockets.add(new Rocket(player));
                    game.getSound("shot1sound").play(game.soundVolume);
                }

            }
            else
            {
                if(rockets.size() < 5)
                {
                    rockets.add(new Rocket(player));
                    game.getSound("shot1sound").play(game.soundVolume);
                }
            }
        }
        else
        {
            //float dx = (float) Math.pow(Math.abs(screenX-110), 2);
            //float dy = (float) Math.pow(Math.abs(screenY-880), 2);
            //System.out.println(screenX + " : "+screenY);
            if(screenX>Scale.getScaledSizeX(91) && screenX<Scale.getScaledSizeX(270) && screenY>Scale.getScaledSizeY(45) && screenY<Scale.getScaledSizeY(220))
            //if((dx+dy)>250)
            {
                activatePowerUp();
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        backgroudMusic.dispose();
        Meteor.dispose();
        Rocket.dispose();
        Explosion.dispose();
        for (int i = 0; i < rockets.size(); i++) {
            rockets.get(i).dispose();
        }
    }

    public void shakeCam()
    {
        if(!player.dead)
        {
            shakeCamTimer = System.currentTimeMillis();
            if(game.vibrationEnabled)
            {
                Gdx.input.vibrate(SHAKETIME);
            }
        }
    }

    public InputMultiplexer getInputMultiplexer()
    {
        return inputMultiplexer;
    }

    public SpaceGame getGame() { return game; }

    public Camera getCamera() { return camera; }

    public SpriteBatch getBatch() { return batch; }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        System.out.println((velocityX * velocityX + velocityY * velocityY) + ": " + Scale.getScaledSizeX(2000000));
        if((velocityX * velocityX + velocityY * velocityY) > Scale.getScaledSizeX(100000))
        {
            activatePowerUp();
        }
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
