//Added For Assessment 3
package com.rear_admirals.york_pirates.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.PirateGame;
import com.rear_admirals.york_pirates.base.BaseScreen;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class VictoryScreen extends BaseScreen {
    private boolean hasWon;
    private int i;
    private Label scoreboardLabel,pointsLabel;
    private File scoreboard;

    public VictoryScreen(PirateGame main, Boolean hasWon) {
        super(main);
        //Stores if the User has Won or Lost
        this.hasWon = hasWon;

        scoreboard = new File("scoreboard.txt");

        //Table used to store labels to align and position them relatively
        Table uiTable = new Table();
        uiTable.setFillParent(true);
        uiTable.setPosition(0,0);
        uiTable.align(Align.center);
        //Creates Win and Lose messages based on if the Player has Won or Lost
        if (hasWon) {
            Label Victory = new Label("You Win!", main.getSkin());
            uiTable.add(Victory);
            //A4: output the score to a file for the scoreboard.
            try{
                if(scoreboard.exists()==false){
                    System.out.println("We had to make a new file.");
                    scoreboard.createNewFile();
                }
                PrintWriter out = new PrintWriter(new FileWriter(scoreboard, true));
                out.append(main.getPlayer().getPoints() + "\n");
                out.close();
            }catch(IOException e){
                System.out.println("COULD NOT WRITE SCOREBOARD!!");
            }
                i=0;
        } else {
            Label Loss = new Label("You Lose :(", main.getSkin());
            uiTable.add(Loss);
            Label Message = new Label("Your Gold will be Halved and Points Reset", main.getSkin());
            uiTable.row();
            uiTable.add(Message);
            i=0;
        }

        //Creates Label displaying the players score on the Win/Loss Screen
        pointsLabel = new Label("Score: " + Integer.toString(main.getPlayer().getPoints()), main.getSkin());
        uiTable.row();
        uiTable.add(pointsLabel);

        //Show the scoreboard for all winners.
        scoreboardLabel = new Label("Scoreboard:",main.getSkin());
        uiTable.row();
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
        i++;

        //i and If statement used to allow the screen to Update to the Victory Screen
        //before sleeping so the Screen is displayed for a set amount of time
        if (i == 2){
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
            }

            //If the player has won the game closes, If they have lost they are reset to the Sailing Screen
            if (hasWon) {
                pirateGame.setScreen(new MainMenu(pirateGame));
            } else {
                pirateGame.setScreen(pirateGame.getSailingScene());
            }
        }
    }
}
//End Added