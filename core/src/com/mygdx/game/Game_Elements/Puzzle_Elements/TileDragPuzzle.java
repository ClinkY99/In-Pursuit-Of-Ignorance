package com.mygdx.game.Game_Elements.Puzzle_Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Game_Elements.Object;
import com.mygdx.game.ui.ScreenStack;
import com.mygdx.game.ui.stackableScreen;

public class TileDragPuzzle extends ImagePuzzleButton{
    // the index of the real tiles for goals will be the goal it needs to be dragged to
    Array<DragDropObject> tilesToDrag;
    Array<DragDropGoal> goals;
    PuzzleTable goalTable;
    PuzzleTable tileTable;
    public boolean isComplete = false;
    DragDropScreen dragDropPuzzle;
    float displaceNum;
    TextureRegion region;
    ScreenStack stack;

    public TileDragPuzzle(Texture buttonTexture, Array<Texture> realTiles, Texture goalTexture, int numberOfGoals, int[][] orderedGoalCoordinates, ScreenStack stack){
        //this class makes a button that then pushes a screen onto the screen stack.
        //the screen that is pushed contains a drag drop puzzle, where you
        //drag objects onto other objects
        super(buttonTexture,2);
        this.stack = stack;
        dragDropPuzzle = new DragDropScreen(realTiles,goalTexture,numberOfGoals,orderedGoalCoordinates);

        region = new TextureRegion(buttonTexture);
        setBounds(region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            System.out.println("SUS");
            stack.push(dragDropPuzzle);
            }
        });


    }
    public void render(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public boolean areAllSolved() {
        for(int i = 0; i < goals.size;i++) {
            if (!goals.get(i).touchingCorrectTile) {
                return false;
            }
        }
        return true;
    }

    class DragDropScreen implements stackableScreen {
        Stage stage;
        ImagePuzzleButton screenBlocker;
        public DragDropScreen(Array<Texture> realTiles, Texture goalTexture, int numberOfGoals, int[][] goalCoordinates) {
            stage = new Stage(new FitViewport(1920,1080));
            Texture screenBlockerTexture = new Texture(Gdx.files.internal("Images/screenBlocker.png"));

            screenBlocker = new ImagePuzzleButton(screenBlockerTexture,0);
            screenBlocker.setPosition(0,0);
            screenBlocker.setSize(1928,1080);
            stage.addActor(screenBlocker);

            Texture backgroundThingTexture = new Texture(Gdx.files.internal("Images/tiles/Level1/Puzzles/Puzzle 1/PedastlePuzzleScreen.png"));
            ImagePuzzleButton backgroundThing = new ImagePuzzleButton(backgroundThingTexture,0);
            backgroundThing.setPosition(0,0);
            backgroundThing.setSize(1920,1080);
            stage.addActor(backgroundThing);

            tilesToDrag = new Array<>();
            goals = new Array<>();
            for (int j = 0; j < realTiles.size; j++) {
                DragDropObject realTile = new DragDropObject(realTiles.get(j));
                realTile.data = j;
                tilesToDrag.add(realTile);
            }
            tilesToDrag.shuffle();
            //Array of tiles to add to tile table

            for (int i = 0; i < numberOfGoals; i++) {
                DragDropGoal goalObject = new DragDropGoal(goalTexture);
                goalObject.data = i;
                goals.add(goalObject);
            }

            for (int i = 0; i <tilesToDrag.size;i++) {
                stage.addActor(tilesToDrag.get(i));
                tilesToDrag.get(i).setPosition(i<4 ? 150:1700, i==0||i==4? 600: tilesToDrag.get(i-1).getY()-150);
            }
            for (int i = 0; i <goals.size;i++) {
                stage.addActor(goals.get(i));
                // determines where goals are placed
                if (goalCoordinates.length == goals.size) {
                    goals.get(i).setPosition(goalCoordinates[i][0],goalCoordinates[i][1]);
                } else {
                    goals.get(i).setPosition(250 +(i*(goals.get(i).getWidth() +30)), 890);

                }
            }
        }
        public void show() {

        }

        @Override
        public void render(float delta, boolean top) {
            stage.act(delta);
            stage.draw();
            for (int i = 0; i < goals.size;i++) {
                for (int j = 0; j < tilesToDrag.size;j++) {
                    if (goals.get(i).bounds.overlaps(tilesToDrag.get(j).bounds)) {

                        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                            tilesToDrag.get(j).setPosition(goals.get(i).getX() + goals.get(i).getWidth() / 2 - tilesToDrag.get(i).getWidth() / 2, goals.get(i).getY() + goals.get(i).getHeight() / 2 - tilesToDrag.get(i).getHeight() / 2);
                        }
                        if (tilesToDrag.get(j).data == goals.get(i).data) {
                            goals.get(i).touchingCorrectTile = true;
                        }

                    } else {

                    }
                }
            }

            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                stack.remove(this);

            }
            if (areDone()) {
                for (int i = 0; i < tilesToDrag.size;i++) {
                    tilesToDrag.get(i).image = new Texture(Gdx.files.internal("Images/tiles/Level1/Puzzles/Puzzle 1/Tiles/Tile_00" + (tilesToDrag.get(i).data+1) + "_Active.png"));
                    tilesToDrag.get(i).inPlace = true;
                    isComplete = true;
                }
            }

        }
        boolean areDone() {
            for (int i = 0; i < goals.size;i++) {
               if  (!goals.get(i).touchingCorrectTile) {
                   return false;
               }
            }
            return true;
        }

        @Override
        public void resize(int width, int height) {
            stage.getViewport().update(width, height, true);
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
            stage.dispose();
        }

        @Override
        public void setStage(Stage stage) {

        }

        @Override
        public Stage getStage() {
            return stage;
        }
    }
}