package es.exploradores.android;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class OptionsMenuActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		menu.findItem(R.id.creditos).setIntent(new Intent(this, CreditsActivity.class));
        menu.findItem(R.id.instrucciones).setIntent(new Intent(this, InfoActivity.class));
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
       	startActivity(item.getIntent());
        return true;
    }
}