package com.example.nzp.pokedex;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    SeekBar seekBarAtk, seekBarDef, seekBarHP;
    TextView minAtkText, minDefText, minHPText;
    CheckBox[] checkBoxes;
    CheckBox checkAll;
    Button saveButton;

    int[] checkBoxIds = {
            R.id.bugCheckBox, R.id.darkCheckBox, R.id.dragonCheckBox, R.id.electricCheckBox,
            R.id.fairyCheckBox, R.id.fightingCheckBox, R.id.fireCheckBox, R.id.flyingCheckBox,
            R.id.ghostCheckBox, R.id.grassCheckBox, R.id.groundCheckBox, R.id.iceCheckBox,
            R.id.normalCheckBox, R.id.poisonCheckBox, R.id.psychicCheckBox, R.id.rockCheckBox,
            R.id.steelCheckBox, R.id.waterCheckBox
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //Initialize ALL the views, what fun
        minAtkText = (TextView) (findViewById(R.id.minAtkText));
        minDefText = (TextView) (findViewById(R.id.minDefText));
        minHPText = (TextView) (findViewById(R.id.minHPText));
        seekBarAtk = (SeekBar) (findViewById(R.id.seekBarAtk));
        seekBarDef = (SeekBar) (findViewById(R.id.seekBarDef));
        seekBarHP = (SeekBar) (findViewById(R.id.seekBarHP));

        //Ready the SeekBars
        seekBarAtk.setMax(Pokemon.HIGHEST_ATK);
        seekBarDef.setMax(Pokemon.HIGHEST_DEF);
        seekBarHP.setMax(Pokemon.HIGHEST_HP);
        seekBarAtk.setOnSeekBarChangeListener(this);
        seekBarDef.setOnSeekBarChangeListener(this);
        seekBarHP.setOnSeekBarChangeListener(this);

        //Initialize the checkboxes
        checkBoxes = new CheckBox[Pokemon.NUM_TYPES];
        for (int i = 0; i < checkBoxes.length; i += 1) {
            checkBoxes[i] = (CheckBox) (findViewById(checkBoxIds[i]));
        }

        //Check all and save button
        checkAll = (CheckBox) (findViewById(R.id.checkAll));
        saveButton = (Button) (findViewById(R.id.saveButton));
        checkAll.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private void changeCheckBoxes(boolean toWhat) {
        for (CheckBox c : checkBoxes) {
            c.setChecked(toWhat);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Discard this filter?")
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        discard();
                    }
                })
                .setNegativeButton("Cancel", null).show();
    }

    private void discard() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void save() {
        Intent intent = new Intent();

        //Put stats
        intent.putExtra("atk", seekBarAtk.getProgress());
        intent.putExtra("def", seekBarDef.getProgress());
        intent.putExtra("hp", seekBarHP.getProgress());

        //Put types (only if they are checked)
        ArrayList<String> allowedTypes = new ArrayList<>();
        for (int i = 0; i < checkBoxes.length; i += 1) {
            if (checkBoxes[i].isChecked()) {
                allowedTypes.add(Pokemon.TYPES[i]);
            }
        }
        intent.putStringArrayListExtra("types", allowedTypes);

        //Finish intent
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.seekBarAtk:
                minAtkText.setText(String.format(getString(R.string.min_atk), i));
                break;
            case R.id.seekBarDef:
                minDefText.setText(String.format(getString(R.string.min_def), i));
                break;
            case R.id.seekBarHP:
                minHPText.setText(String.format(getString(R.string.min_hp), i));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkAll:
                changeCheckBoxes(checkAll.isChecked());
                break;
            case R.id.saveButton:
                save();
        }
    }
}
