package me.spacegame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.math.Vector3;

import me.spacegame.SpaceGame;

/**
 * Created by Felix on 19-May-17.
 */

public class HealthBar {

    private Texture mainTexture;
    private FileHandle healthbarFrag;
    private FileHandle healthbarVert;

    private SpriteBatch healthBatch;

    private ShaderProgram healthbarProgram;


    //Questionmark
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance instance;
    private ModelLoader loader = new ObjLoader();
    public FrameBuffer fbo;
    public Environment environment;

    //public Mesh healthBar = new Mesh();




    public HealthBar()
    {
        healthbarFrag = Gdx.files.internal("shader/ui/healthbar.frag");
        healthbarVert = Gdx.files.internal("shader/ui/healthbar.vert");
        mainTexture = new Texture(Gdx.files.internal("ui/healthbarmain.png"));

        healthbarProgram = new ShaderProgram(healthbarVert, healthbarFrag);
        System.out.println(healthbarProgram.isCompiled());

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



    }

    public void draw(float delta, SpriteBatch batch)
    {

        healthbarProgram.begin();
        healthbarProgram.setUniformf("health", 300);
        healthbarProgram.setUniformf("origin", 200, 200, 0, 0);
        healthbarProgram.end();

        //render to fbo
        fbo.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, 200, 200);

        instance.transform.rotate(new Vector3(0, 1, 0), 1.8f);
        modelBatch.begin(cam);
        modelBatch.render(instance);
        modelBatch.end();
        fbo.end();

        //render other

        healthBatch.begin();
        healthBatch.draw(fbo.getColorBufferTexture(), 87f, 833f);
        healthBatch.draw(mainTexture, 62, 740);
        healthBatch.end();

    }

    public void dispose()
    {
        model.dispose();
        modelBatch.dispose();
        healthbarProgram.dispose();
        fbo.dispose();

    }

}
