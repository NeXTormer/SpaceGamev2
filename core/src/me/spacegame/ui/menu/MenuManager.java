package me.spacegame.ui.menu;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.HashMap;

import me.spacegame.screens.GameScreen;

/**
 * Created by Felix on 19-Jun-17.
 */

public class MenuManager {

    public OrthographicCamera camera;

    public HashMap<String, MenuTemplate> screens;
    public MenuTemplate currentMenu;

    private GameScreen gameScreen;
    public MenuManager(GameScreen screen)
    {
        camera = new OrthographicCamera(1920, 1080);
        screens = new HashMap<String, MenuTemplate>();
        this.gameScreen = screen;

        //load all menuscreens
        //screens.put("main", new MainMenu(this));
        screens.put("empty", new EmptyMenu(this));
        //screens.put("options", new OptionsMenu(this));
        screens.put("gameover", new GameOverMenu(this));

        currentMenu = screens.get("empty").activate();

        for(MenuTemplate menu : screens.values())
        {
            menu.create();
        }
    }

    public void draw()
    {
        currentMenu.draw();
    }

    public void dispose()
    {
        for(MenuTemplate menu : screens.values())
        {
            menu.dispose();
        }
    }

    public GameScreen getGameScreen()
    {
        return gameScreen;
    }

}
