package es.perdidos.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MarcadorActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button calcularButton = (Button) findViewById(R.id.calcular);
        calcularButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MarcadorActivity.this, "Calcular", Toast.LENGTH_SHORT).show();
			}
		});
    }
}