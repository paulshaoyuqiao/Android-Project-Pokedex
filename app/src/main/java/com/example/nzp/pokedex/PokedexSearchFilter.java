package com.example.nzp.pokedex;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Filters the Pokedex based on a CharSequence.
 */

public class PokedexSearchFilter extends Filter {

    private PokedexAdapter adapter;
    private ArrayList<Pokemon> all;
    private ArrayList<Pokemon> filtered;

    public PokedexSearchFilter(PokedexAdapter adapter, ArrayList<Pokemon> all) {
        this.adapter = adapter;
        this.all = all;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        String charString = charSequence.toString();
        if (charString.isEmpty()) {
            filtered = all;
        } else {
            //First pass gets most relevant pokemon
            ArrayList<Pokemon> filteredList = new ArrayList<>();
            for (Pokemon pokemon : all) {
                //Add if number or name is similar
                if (pokemon.name.toLowerCase().startsWith(charString.toLowerCase()) || pokemon.number.startsWith(charString)) {
                    filteredList.add(pokemon);
                }
            }
            filtered = filteredList;
        }

        FilterResults filterResults = new FilterResults();
        filterResults.values = filtered;
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        filtered = (ArrayList<Pokemon>) filterResults.values;
        adapter.setData(filtered);
    }
}
