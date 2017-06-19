package me.spacegame.ui.menu;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.HashMap;

import me.spacegame.screens.GameScreen;

/**
 * Created by Felix on 19-Jun-17.
 */

public class Menu {

    public OrthographicCamera camera;

    private HashMap<String, TemplateMenu> screens;
    private TemplateMenu currentMenu;

    private GameScreen gameScreen;
    public Menu(GameScreen screen)
    {
        camera = new OrthographicCamera(1920, 1080);
        screens = new HashMap<String, TemplateMenu>();
        this.gameScreen = screen;

        //load all menuscreens
        screens.put("main", new MainMenu(this));
        screens.put("empty", new EmptyMenu(this));
        screens.put("options", new OptionsMenu(this));

        currentMenu = screens.get("empty");


        for(TemplateMenu menu : screens.values())
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
        for(TemplateMenu menu : screens.values())
        {
            menu.dispose();
        }
    }

    public GameScreen getGameScreen()
    {
        return gameScreen;
    }

}
