package nl.whitedove.agecalculator;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

class PersonsDialog extends Dialog implements
        View.OnClickListener {

   DatabaseHelper mDH;

    PersonsDialog(Context ctx) {
        super(ctx);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.persons_dialog);
        Button btOk = (Button) findViewById(R.id.btOk);
        btOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CheckBox cb1 = (CheckBox) findViewById(R.id.cbShow1);
        CheckBox cb2 = (CheckBox) findViewById(R.id.cbShow2);
        CheckBox cb3 = (CheckBox) findViewById(R.id.cbShow3);
        CheckBox cb4 = (CheckBox) findViewById(R.id.cbShow4);
        CheckBox cb5 = (CheckBox) findViewById(R.id.cbShow5);

        ArrayList<Persoon> personen = mDH.GetAllePersonen();

        personen.get(0).setGetoond(cb1.isChecked());
        mDH.UpdatePersoon(personen.get(0));

        personen.get(1).setGetoond(cb2.isChecked());
        mDH.UpdatePersoon(personen.get(1));

        personen.get(2).setGetoond(cb3.isChecked());
        mDH.UpdatePersoon(personen.get(2));

        personen.get(3).setGetoond(cb4.isChecked());
        mDH.UpdatePersoon(personen.get(3));

        personen.get(4).setGetoond(cb5.isChecked());
        mDH.UpdatePersoon(personen.get(4));

        dismiss();
    }

    void SetCheckboxes(DatabaseHelper mdh) {

        CheckBox cb1 = (CheckBox) findViewById(R.id.cbShow1);
        CheckBox cb2 = (CheckBox) findViewById(R.id.cbShow2);
        CheckBox cb3 = (CheckBox) findViewById(R.id.cbShow3);
        CheckBox cb4 = (CheckBox) findViewById(R.id.cbShow4);
        CheckBox cb5 = (CheckBox) findViewById(R.id.cbShow5);

        mDH = mdh;
        ArrayList<Persoon> personen = mDH.GetAllePersonen();
        cb1.setText(personen.get(0).getNaam());
        cb1.setChecked(personen.get(0).getGetoond());

        cb2.setText(personen.get(1).getNaam());
        cb2.setChecked(personen.get(1).getGetoond());

        cb3.setText(personen.get(2).getNaam());
        cb3.setChecked(personen.get(2).getGetoond());

        cb4.setText(personen.get(3).getNaam());
        cb4.setChecked(personen.get(3).getGetoond());

        cb5.setText(personen.get(4).getNaam());
        cb5.setChecked(personen.get(4).getGetoond());
    }
}

