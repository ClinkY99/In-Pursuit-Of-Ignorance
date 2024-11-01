package com.mygdx.game.Menus.subMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.CPTGame;
import com.mygdx.game.Game_Elements.Puzzle_Elements.ImagePuzzleButton;
import com.mygdx.game.Game_Elements.SaveFile;
import com.mygdx.game.Levels.LevelOne.levelOneStackManager;
import com.mygdx.game.Multiplayer.MPHandle;
import com.mygdx.game.ui.transitions.transitionScreen;
import com.mygdx.game.ui.transitions.transitions;
import com.mygdx.game.ui.widgets.AutoFocusScrollpane;
import com.mygdx.game.ui.widgets.menus.MenuButton;
import com.mygdx.game.ui.widgets.menus.saveSelection;
import com.mygdx.game.Menus.MainMenu;

import java.io.IOException;

/**
 * controls logic and drawing of the host game screen
 */
public class HostGame implements Screen {
    final CPTGame game;

    private final Music music;
    private final Stage stage;
    private final Image background;
    ImagePuzzleButton confirmationButton;
    private final Array<Button> otherButtons;
    private ButtonGroup<saveSelection> buttonGroup;


    private final AutoFocusScrollpane scrollPane;

    /**
     * controls drawing and initialzing of the host game class
     * @param Game game data
     * @param menuMusic music
     * @throws IOException
     */
    public HostGame(CPTGame Game, Music menuMusic) throws IOException
    {
        this.game = Game;

        music = menuMusic;

        otherButtons = new Array<>();

        stage = new Stage(new FitViewport(1920,1080), game.batch);
        Gdx.input.setInputProcessor(stage);

        background = new Image(new Texture("Menu/Menu2.png"));
        background.setPosition(0, 0);
        background.setSize(1920, 1080);
        stage.addActor(background);

        Table table = new Table();
        MenuButton newGameButton = new MenuButton("New Game");
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new newSave(game,menuMusic));
            }
        });
        table.add(newGameButton).height(100).width(1920/4f);

        MenuButton joinGameButton = new MenuButton("JoinGame");
        joinGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new JoinGame(game,menuMusic));
            }
        });
        table.add(joinGameButton).height(100).width(1920/4f);

        table.setPosition(1920/3.25f,1080/8f*7);
        //table.debug();

        MenuButton backButton = new MenuButton("back", .8f);
        otherButtons.add(backButton);

        backButton.setPosition(1920/4.5f, 30);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game,music));
            }
        });

        Screen current = this;

        MenuButton hostGameButton = new MenuButton("Host Game", .8f);
        hostGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirmationButton = new ImagePuzzleButton(new Texture(Gdx.files.internal("Images/WarningScreen.png")),2);
                confirmationButton.setPosition(700,500);
                confirmationButton.setSize(800,240);
                confirmationButton.debug();
                confirmationButton.addListener(new ClickListener()  {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new transitionScreen(current, new transitions() {

                            @Override
                            public Screen load() throws IOException {
                                return new levelOneStackManager(game, null, new SaveFile("Sussy"),0,1);

                            }
                        }, game));
                    }
                });
                stage.addActor(confirmationButton);
            }
        });

        otherButtons.add(hostGameButton);

        hostGameButton.setPosition(1920/4.5f*3.1f, 30);

        scrollPane = new AutoFocusScrollpane(initSaves());

        if(Gdx.files.internal("Saves/").list().length == 0){
            scrollPane.setPosition(0,1080/2.5f);

        } else {
            scrollPane.setPosition(0,1080/4f);
            scrollPane.setHeight(1080/2f+25);
        }
        scrollPane.setWidth(1920/5f*3);
        scrollPane.setFadeScrollBars(false);
        //scrollPane.debug();

        stage.addActor(table);
        for (Button button: new Array.ArrayIterator<>(otherButtons)){
            stage.addActor(button);
        }
        stage.addActor(scrollPane);

    }

    /**
     * this method returns all saves files that are found within the save directory, returns a vertical group
     * @return Table of all save files found
     */
    private VerticalGroup initSaves() {
        VerticalGroup table = new VerticalGroup();

        FileHandle[] saves = Gdx.files.internal("Saves/").list();

        if (saves.length > 0) {
            Array<saveSelection> array = new Array<>();
            for (FileHandle save : saves) {
                JsonReader reader = new JsonReader();
                JsonValue jv = reader.parse(save);
                array.add(new saveSelection(new SaveFile(jv)));

            }
            sortSaves(0, array.size-1, array);

            buttonGroup = new ButtonGroup<>();
            for (saveSelection i : new Array.ArrayIterable<>(array)){
                buttonGroup.add(i);
                table.addActor(i);
            }

            buttonGroup.setMaxCheckCount(1);
            buttonGroup.setMinCheckCount(1);

            table.setPosition(0, 0);
        } else {
            FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Menu/tempus_sans_itc.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 24;

            Label label = new Label("No Save Files Available, Please create new save or join a game", new Label.LabelStyle(fontGen.generateFont(parameter), Color.BLACK));

            table.addActor(label);
        }
        return table;
    }

    /**
     * runs quick sort on array of saves to organize them
     * @param low
     * @param high
     * @param array
     */
    void sortSaves(int low, int high, Array<saveSelection> array){
        if(low < high){

            int pivot = partition(array, low, high);

            sortSaves(low, pivot-1, array);
            sortSaves(pivot+1, high, array);

        }
    }

    void Swap(Array<saveSelection> array, int indexA, int indexB){
        saveSelection temp = array.get(indexA);
        array.set(indexA, array.get(indexB));
        array.set(indexB, temp);
    }
    int partition(Array<saveSelection> array, int low, int high){
        long pivot = array.get(high).getSaveFile().getDateLPLong();
        int minIndex = low-1;
        System.out.println(minIndex);
        for(int i = low; i <= high; i++){
            if(array.get(i).getSaveFile().getDateLPLong()>pivot){
                minIndex++;
                Swap(array, i, minIndex);
            }
        }
        System.out.println(minIndex);
        Swap(array, minIndex+1, high);
        return minIndex+1;

    }

    @Override
    public void show() {
        music.play();
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            stage.getActors().removeValue(confirmationButton,true);
        }
        stage.act(delta);

        ScreenUtils.clear(Color.WHITE);


        stage.getViewport().getCamera().update();



        stage.draw();


    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        music.pause();
    }

    @Override
    public void dispose() {
        stage.dispose();
        otherButtons.clear();
    }
}
