package com.example.nzp.pokedex;

/**
 *
 * An enum representing the possible styles to display the list of Pokemon, LIST and GRID.
 */

public enum DisplayStyle {

    LIST,
    GRID;

    /* Gets the other style. */
    public static DisplayStyle other(DisplayStyle thisStyle) {
        if (thisStyle == LIST) {
            return GRID;
        }
        return LIST;
    }

    /* Return string representation. */
    @Override
    public String toString() {
        if (this == LIST) {
            return "Grid";
        }
        return "List";
    }
}
