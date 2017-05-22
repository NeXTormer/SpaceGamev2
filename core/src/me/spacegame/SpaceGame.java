package me.spacegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;

import me.spacegame.screens.MainMenuScreen;

public class SpaceGame extends Game {

	public final static int VIEWPORTWIDTH = 1920;
	public final static int VIEWPORTHEIGHT = 1080;

	SpriteBatch batch;
	Texture img;

	//TEMP
	public PerspectiveCamera cam;
	public ModelBatch modelBatch;
	public Model model;
	public ModelInstance instance;
	private ModelLoader loader = new ObjLoader();
	public static FrameBuffer fbo;
	public static Environment environment;


	@Override
	public void create () {
		fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 200, 200, true);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		setScreen(new MainMenuScreen(this));
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		model = loader.loadModel(Gdx.files.internal("ui/fern.obj"));

		instance = new ModelInstance(model);
	}

	@Override
	public void render () {

		//render to fbo
		fbo.begin();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glViewport(0, 0, 200, 200);

		instance.transform.rotate(new Vector3(0, 1, 0), 1f);
		modelBatch.begin(cam);
		modelBatch.render(instance);
		modelBatch.end();
		fbo.end();

		//render to screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		getScreen().render(Gdx.graphics.getDeltaTime());

	}
	
	@Override
	public void dispose () {
		model.dispose();
		modelBatch.dispose();
	}
}
