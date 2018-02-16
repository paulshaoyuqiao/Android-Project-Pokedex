package com.example.nzp.pokedex;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * The Pokemon class. Modified from the original from Pokedex.java.
 */

public class Pokemon {

    public static final String[] TYPES = {
            "Bug", "Dark", "Dragon", "Electric", "Fairy", "Fighting", "Fire", "Flying", "Ghost",
            "Grass", "Ground", "Ice", "Normal", "Poison", "Psychic", "Rock", "Steel", "Water"
    };
    public static final int NUM_TYPES = TYPES.length;
    public static final int HIGHEST_ATK = 180;
    public static final int HIGHEST_DEF = 230;
    public static final int HIGHEST_HP = 255;

    String name;
    String number;
    String attack;
    String defense;
    String hp;
    String species;
    String specialAttack;
    String specialDefense;
    String speed;
    String total;
    String[] type;

    public Pokemon(String name, JSONObject jsonData) {
        try {
            this.name = name;
            number = jsonData.getString("#").trim();
            attack = jsonData.getString("Attack").trim();
            defense = jsonData.getString("Defense").trim();
            hp = jsonData.getString("HP").trim();
            species = jsonData.getString("Species").trim();
            specialAttack = jsonData.getString("Sp. Atk").trim();
            specialDefense = jsonData.getString("Sp. Def").trim();
            speed = jsonData.getString("Speed").trim();
            total = jsonData.getString("Total").trim();


            //Additional code to support types
            String types = jsonData.getString("Type").trim();
            type = types.substring(1, types.length() - 1).split(","); //split into array of type strings
            for (int i = 0; i < type.length; i += 1) {
                type[i] = type[i].substring(1, type[i].length() - 1); //remove "" around each type string
                type[i] = type[i].substring(0, 1).toUpperCase() + type[i].substring(1).toLowerCase();
            }

        } catch (JSONException e) {
            Log.i("JSON error", "error parsing json data");
        }
    }

}