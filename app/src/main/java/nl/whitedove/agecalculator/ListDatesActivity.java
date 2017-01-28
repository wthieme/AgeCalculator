package nl.whitedove.agecalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListDatesActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dates_list_activity);

        FloatingActionButton fabTerug = (FloatingActionButton) findViewById(R.id.fabTerug);
        fabTerug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Terug();
            }
        });
        ToonData();
    }

    private void Terug() {
        Intent intent = new Intent(this, MainActivity.class);
        Helper.dgLijst = new ArrayList<>();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void ToonData() {
        TextView tvDateTime = (TextView) findViewById(R.id.tvDateTime);
        String sDate = "TODO";
        tvDateTime.setText(sDate);
        if (Helper.dgLijst == null || Helper.dgLijst.size() == 0) return;

        final ListView lvEigenMeldingen = (ListView) findViewById(R.id.lvDates);
        lvEigenMeldingen.setAdapter(new CustomListAdapterDates(this, Helper.dgLijst));

    }
}