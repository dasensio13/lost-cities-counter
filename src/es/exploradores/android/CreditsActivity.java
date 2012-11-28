package es.exploradores.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CreditsActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        TextView helpText = (TextView) findViewById(R.id.TextView_HelpText);
        helpText.setText(getResources().getString(R.string.detalle_creditos));
    }
}
