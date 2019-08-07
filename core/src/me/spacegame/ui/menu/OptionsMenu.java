package me.spacegame.ui.menu;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

/**
 * Created by Felix on 19-Jun-17.
 */
//not used weil pfusch
public class OptionsMenu extends MenuTemplate {

    private ImageButton backButton;
    private long timers[] = new long[1];

    public OptionsMenu(MenuManager menuManager)
    {
        super(menuManager);
    }

    @Override
    public void create() {
        //backButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/backbtn.png")))));
        backButton.setPosition(900, 500);
        stage.addActor(backButton);
    }


    @Override
    public void draw() {
        stage.act();
        stage.draw();
        if(backButton.isPressed())
        {
            if(System.currentTimeMillis() - timers[0] > 1000)
            {
                //menuManager.getGameScreen().settingsbtn.setDisabled(false);
                menuManager.currentMenu = menuManager.screens.get("main").activate();
                timers[0] = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
