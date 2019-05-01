package com.rear_admirals.york_pirates.screen.combat;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.rear_admirals.york_pirates.screen.combat.attacks.Attack;

/**
 * Handles the different attack buttons in the combat screen
 * Child of LibGDX TextButton
 */
public class AttackButton extends TextButton {
    private String name;
    private String desc;
    private Attack attack;

    /**
     * AttackButton Constructor 1
     *
     * @param attack - the attack to be displayed
     * @param skin - the button skin
     */
    public AttackButton(Attack attack, Skin skin){
        super(attack.getName(), skin);
        this.attack = attack;
        this.name = attack.getName();
        this.desc = attack.getDesc();
    }

    /**
     * AttackButton Constructor 2
     *
     * @param attack - the attack to be displayed
     * @param skin - the button skin
     * @param type - type of button
     */
    public AttackButton(Attack attack, Skin skin, String type){
        super(attack.getName(), skin, type);
        this.attack = attack;
        this.name = attack.getName();
        this.desc = attack.getDesc();
    }

    public String getName() {
        return this.name;
    }
    public String getDesc() {
        return this.desc;
    }
    public Attack getAttack() { return this.attack; }
}