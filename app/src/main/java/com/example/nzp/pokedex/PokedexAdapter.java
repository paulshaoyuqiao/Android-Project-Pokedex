package com.example.nzp.pokedex;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by nzp on 9/19/17.
 *
 * PokedexAdapter adapts the Pokemon data from Pokedex.java and makes it easy to display and filter
 * in a RecyclerView.
 */

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.CustomViewHolder> implements Filterable {


    public static final ArrayList<Pokemon> ALL_POKEMON = new Pokedex().getPokemon();

    private Context context;
    private ArrayList<Pokemon> filteredPokemon;
    private ArrayList<Pokemon> truePokemon;
    private DisplayStyle displayStyle;

    public PokedexAdapter(Context context, ArrayList<Pokemon> filteredPokemon, ArrayList<Pokemon> truePokemon, DisplayStyle displayStyle) {
        this.context = context;
        this.filteredPokemon = filteredPokemon == null ? new ArrayList<>(ALL_POKEMON) : filteredPokemon;
        this.truePokemon = truePokemon == null ? new ArrayList<>(this.filteredPokemon) : truePokemon;
        this.displayStyle = displayStyle;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = (displayStyle == DisplayStyle.LIST) ? R.layout.row_view : R.layout.grid_view;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final Pokemon pokemon = truePokemon.get(position);
        String filename = "http://assets.pokemon.com/assets/cms2/img/pokedex/full/" + pokemon.number + ".png";
        Picasso.with(context).load(filename).into(holder.listImageView);
        holder.listTextView.setText(pokemon.name + " #" + pokemon.number);
        holder.listParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PokemonDisplay.class);
                intent.putExtra("number", pokemon.number);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return truePokemon.size();
    }

    @Override
    public Filter getFilter() {
        return new PokedexSearchFilter(this, filteredPokemon);
    }

    public void filterPokemon(PokedexFilter filter) {
        filteredPokemon = new ArrayList<>();
        for (Pokemon pokemon : ALL_POKEMON) {
            int mAtk = Integer.valueOf(pokemon.attack);
            int mDef = Integer.valueOf(pokemon.defense);
            int mHP = Integer.valueOf(pokemon.hp);
            if (mAtk >= filter.getMinAtk() && mDef >= filter.getMinDef() && mHP >= filter.getMinHP()) {
                for (int i = 0; i < pokemon.type.length; i += 1) {
                    if (filter.getAllowedTypes().contains(pokemon.type[i])) {
                        filteredPokemon.add(pokemon);
                        break;
                    }
                }
            }
        }
        setData(filteredPokemon);
    }

    public ArrayList<Pokemon> getFilteredPokemon() {
        return filteredPokemon;
    }

    public ArrayList<Pokemon> getTruePokemon() {
        return truePokemon;
    }

    public int getRandomPokemon(int n) {
        ArrayList<Pokemon> randomPokemon = new ArrayList<>(filteredPokemon);
        Collections.shuffle(randomPokemon);
        if (n >= randomPokemon.size()) {
            n = randomPokemon.size();
        }
        setData(new ArrayList<>(randomPokemon.subList(0, n)));
        return n;
    }

    public void setData(ArrayList<Pokemon> newData) {
        truePokemon = newData;
        notifyDataSetChanged();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        View listParentView;
        ImageView listImageView;
        TextView listTextView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            listParentView = itemView.findViewById(R.id.listParentView);
            listImageView = itemView.findViewById(R.id.listImageView);
            listTextView = itemView.findViewById(R.id.listTextView);

        }
    }
}
