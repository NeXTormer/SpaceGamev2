package me.spacegame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.Timer;
import java.util.TimerTask;

import me.spacegame.SpaceGame;
import me.spacegame.powerups.PowerUp;
import me.spacegame.powerups.PowerUpObject;

/**
 * Created by Felix on 19-May-17.
 */

public class HealthBar {

    private Texture mainTexture;
    private Texture healthTexture;

    private FileHandle healthbarFrag;
    private FileHandle healthbarVert;

    private SpriteBatch healthBatch;
    private ShaderProgram healthbarProgram;

    private float health = 0;
    private float dHealth = 0;
    private long healthAnimationDeltaTime = 0;

    //Questionmark
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance instance;
    private ModelLoader loader = new ObjLoader();
    public FrameBuffer fbo;
    public Environment environment;

    private float defaultRotationSpeed = 1.9f;
    private float rotationSpeed = 1.9f;

    //Powerup Pfusch
    private Texture currentPowerUpTexture;

    private boolean increaseRotationSpeed = false;
    private boolean showPowerUpIcon = false;
    private boolean isPowerUpInPlay = false;
    private boolean isPowerUpTimerActive = false;

    public long powerUpCollectionTime;




    public HealthBar()
    {
        healthbarFrag = Gdx.files.internal("shader/ui/healthbar.frag");
        healthbarVert = Gdx.files.internal("shader/ui/healthbar.vert");
        mainTexture = new Texture(Gdx.files.internal("ui/healthbarmain.png"));
        healthTexture = new Texture(Gdx.files.internal("ui/health.png"));


        healthbarProgram = new ShaderProgram(healthbarVert, healthbarFrag);
        if (!healthbarProgram.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + healthbarProgram.getLog());


        healthBatch = new SpriteBatch();

        healthBatch.setShader(healthbarProgram);

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 200, 200, true);
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(100, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(14f, -2f, 0);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        model = loader.loadModel(Gdx.files.internal("ui/fragezeichen.obj"));

        instance = new ModelInstance(model);
        instance.transform.scl(4);
        instance.transform.rotate(0, 0, 1, 180);

        health = convertPercentToPixel(100);
    }

    public void draw(SpriteBatch batch)
    {
        if(showPowerUpIcon)
        {
            if(currentPowerUpTexture != null)
                batch.draw(currentPowerUpTexture, 87, 833);
        }
        if(isPowerUpInPlay && !showPowerUpIcon)
        {
            batch.draw(fbo.getColorBufferTexture(), 87f, 833f);
        }
        batch.draw(mainTexture, 62, 740);
    }


    public void draw()
    {
        healthAnimationDeltaTime++;
        updateHealth(healthAnimationDeltaTime);
        //render to fbo
        fbo.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, 200, 200);

        if(increaseRotationSpeed)
        {
            rotationSpeed += 0.06f;
            instance.transform.rotate(new Vector3(0, 1, 0), rotationSpeed);
        }
        else
        {
            instance.transform.rotate(new Vector3(0, 1, 0), defaultRotationSpeed);
        }

        modelBatch.begin(cam);
        modelBatch.render(instance);
        modelBatch.end();
        fbo.end();




        healthbarProgram.begin();
        healthbarProgram.setUniformf("health_t", health);
        healthbarProgram.end();

        healthBatch.begin();

        healthBatch.draw(healthTexture, 62, 740);
        healthBatch.end();


        if(isPowerUpTimerActive)
        {
            if(System.currentTimeMillis() - powerUpCollectionTime > 4000)
            {
                increaseRotationSpeed = false;
                rotationSpeed = defaultRotationSpeed;
                showPowerUpIcon = true;
                isPowerUpTimerActive = false;
            }
        }


    }

    public void setProjectionMatrix(Matrix4 pr_matrix)
    {
        healthBatch.setProjectionMatrix(pr_matrix);
    }

    public void updateHealth(float dt)
    {
        dt /= 100;
        if (dHealth > 0)
        {
            if(dt > 0.05f)
            {
                dHealth = 0;
            }
            else
            {
                float interpolated = Interpolation.linear.apply(0, dHealth, dt);
                this.health = this.health - interpolated;
            }
        }
    }

    public void setHealth(float health)
    {
        this.dHealth = Math.abs(this.health - convertPercentToPixel(health));
        healthAnimationDeltaTime = 0;
    }

    public void collectPowerUp(PowerUp powerUp)
    {
        powerUpCollectionTime = System.currentTimeMillis();
        increaseRotationSpeed = true;
        isPowerUpTimerActive = true;
        currentPowerUpTexture = new Texture(powerUp.texture.getTextureData());
    }

    public void reserPowerUp()
    {
        increaseRotationSpeed = false;
        rotationSpeed = defaultRotationSpeed;
        showPowerUpIcon = false;
        isPowerUpInPlay = true;
        currentPowerUpTexture = null;
        powerUpCollectionTime = Long.MIN_VALUE;
    }

    public void activatePowerUp()
    {
        isPowerUpInPlay = true;
    }




    public void dispose()
    {
        model.dispose();
        modelBatch.dispose();
        healthbarProgram.dispose();
        fbo.dispose();

    }

    private float convertPercentToPixel(float percent)
    {
        return 270 + ((percent) * 5.7f);
    }

}
