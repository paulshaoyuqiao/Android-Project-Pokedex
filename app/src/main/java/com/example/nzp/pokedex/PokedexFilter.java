package com.example.nzp.pokedex;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.nzp.pokedex.Pokemon.NUM_TYPES;

/**
 *
 * Holds information about what the user whats to filter (whether or not they want each type, plus
 * minimum ATK, DEF, and HP values.
 * Also provides a 30 character (or less) description of the filter to display.
 */

public class PokedexFilter {

    private ArrayList<String> allowedTypes;
    private int minAtk, minDef, minHP;

    /* Empty constructor filters nothing. */
    public PokedexFilter() {
        this(new ArrayList<>(Arrays.asList(Pokemon.TYPES)), 0, 0, 0);
    }

    public PokedexFilter(ArrayList<String> allowedTypes, int minAtk, int minDef, int minHP) {
        this.allowedTypes = allowedTypes;
        this.minAtk = minAtk;
        this.minDef = minDef;
        this.minHP = minHP;
    }

    public int getMinAtk() {
        return minAtk;
    }

    public int getMinDef() {
        return minDef;
    }

    public int getMinHP() {
        return minHP;
    }

    public ArrayList<String> getAllowedTypes() {
        return allowedTypes;
    }

    @Override
    public String toString() {
        //If no filter, there is no string representation
        StringBuilder builder = new StringBuilder();
        boolean atkFiltered = getMinAtk() > 0;
        boolean defFiltered = getMinDef() > 0;
        boolean hpFiltered = getMinHP() > 0;
        int numTypes = getAllowedTypes().size();
        int numFilteredStats = (atkFiltered ? 1 : 0) + (defFiltered ? 1 : 0) + (hpFiltered ? 1 : 0);

        //Type filters
        if (numTypes == 0) {
            builder.append("Showing no types");
        } else if (numTypes == 1) {
            String type = getAllowedTypes().get(0);
            builder.append("Showing ").append(type).append(" types");
        } else if (numTypes == 2 && numFilteredStats < 2) {
            builder.append("Showing ").append(getAllowedTypes().get(0))
                    .append(" and ").append(getAllowedTypes().get(1)).append(" types");
        } else if (numTypes == NUM_TYPES) {
            builder.append("Showing all types");
        } else {
            builder.append("Showing ").append(numTypes).append(" types");
        }


        //Handle ATK, DEF, and HP filters
        if (numFilteredStats == 3) { //all 3 are true
            builder.append(", filtered by 3 stats");
        } else if (numFilteredStats == 2) { //exactly 2 are true
            builder.append(", filtered by 2 stats");
        } else if (atkFiltered) {
            builder.append(", ATK ≥ ").append(getMinAtk());
        } else if (defFiltered) {
            builder.append(", DEF ≥ ").append(getMinDef());
        } else if (hpFiltered) {
            builder.append(", HP ≥ ").append(getMinHP());
        }

        builder.append(".");

        if (builder.toString().equals("Showing all types.")) {
            return null;
        }
        return builder.toString();
    }

}
