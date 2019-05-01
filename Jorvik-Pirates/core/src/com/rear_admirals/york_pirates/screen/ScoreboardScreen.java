//Added For Assessment 3
package com.rear_admirals.york_pirates.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.PirateGame;
import com.rear_admirals.york_pirates.base.BaseScreen;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Handles the GUI displaying the high scores
 */
public class ScoreboardScreen extends BaseScreen {
    private Label scoreboardLabel;
    private File scoreboard;

    /**
     * ScoreboardScreen Constructor
     *
     * @param main - the game which this Screen is part of
     */
    public ScoreboardScreen(PirateGame main) {
        super(main);
        scoreboard = new File("scoreboard.txt");

        //Table used to store labels to align and position them relatively
        Table uiTable = new Table();
        uiTable.setFillParent(true);
        uiTable.setPosition(0,0);
        uiTable.align(Align.center);

        //Show the scoreboard for all winners.
        scoreboardLabel = new Label("Scoreboard:",main.getSkin());
        uiTable.row();
        uiTable.add(scoreboardLabel);


        String line = null;
        int j = 1;
        try{
            FileReader fileReader = new FileReader(scoreboard);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                Label scores = new Label("Player"+j+":  "+line,main.getSkin());
                uiTable.row();
                uiTable.add(scores);
                j++;
            }

            bufferedReader.close();
        }catch(IOException e){
            System.out.println("COULD NOT READ SCOREBOARD!!");
        }



        uiStage.addActor(uiTable);
            }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESCAPE");
            pirateGame.setScreen(new MainMenu(pirateGame));
        }
    }
}
//End Added