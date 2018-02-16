package com.example.nzp.pokedex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GRID_COLUMNS = 3;
    private static final int NUM_RANDOM_POKEMON = 20;

    //RecyclerView variables
    private RecyclerView recyclerView;
    private PokedexAdapter adapter;
    private DisplayStyle displayStyle = DisplayStyle.LIST;

    //Menu variables
    private Menu menu;
    private SearchView searchView;

    //Filter view variables
    private View filterInfoView;
    private TextView filterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        changeDisplayStyle(displayStyle);

        //Filtering stuff
        filterInfoView = findViewById(R.id.filterInfoView);
        filterText = (TextView) findViewById(R.id.filterText);
        filterInfoView.setVisibility(View.GONE);
        ImageView removeFilterImage = (ImageView) findViewById(R.id.removeFilterImage);
        removeFilterImage.setOnClickListener(this);

    }

    private void changeDisplayStyle(DisplayStyle style) {
        //Create and set the layout manager
        if (style == DisplayStyle.LIST) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (style == DisplayStyle.GRID){
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), GRID_COLUMNS));
        } else {
            throw new IllegalArgumentException("Invalid style.");
        }

        //Create and set the adapter
        if (adapter == null) {
            adapter = new PokedexAdapter(this, null, null, style);
        } else {
            adapter = new PokedexAdapter(this, adapter.getFilteredPokemon(), adapter.getTruePokemon(), style);
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.searchButton).getActionView();
        return true;
    }

    private void setSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void clearSearch() {
        searchView.setOnQueryTextListener(null);
        (menu.findItem(R.id.searchButton)).collapseActionView();
    }

    private void setFilterText(String text) {
        if (text == null) {
            filterInfoView.setVisibility(View.GONE);
            return;
        }
        filterText.setText(text);
        filterInfoView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.displayMenuButton).setTitle(String.format("%s %s", getString(R.string.display_as), displayStyle));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.displayMenuButton:
                displayStyle = DisplayStyle.other(displayStyle);
                changeDisplayStyle(displayStyle);
                return true;
            case R.id.randomButton:
                int numPokemon = adapter.getRandomPokemon(NUM_RANDOM_POKEMON);
                setFilterText("Showing " + numPokemon + " random pokemon.");
                clearSearch();
                break;
            case R.id.filterButton:
                startActivityForResult(new Intent(this, FilterActivity.class), 1);
                break;
            case R.id.searchButton:
                setSearch();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.removeFilterImage:
                PokedexFilter allFilter = new PokedexFilter();
                adapter.filterPokemon(allFilter);
                setFilterText(null);
                break;
        }
    }

    /* Receiving from filtering activity */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int atk = data.getIntExtra("atk", -1);
                int def = data.getIntExtra("def", -1);
                int hp = data.getIntExtra("hp", -1);
                ArrayList<String> allowedTypes = data.getStringArrayListExtra("types");
                PokedexFilter filter = new PokedexFilter(allowedTypes, atk, def, hp);
                adapter.filterPokemon(filter);
                setFilterText(filter.toString());
                clearSearch();
            }
        }
    }
}
