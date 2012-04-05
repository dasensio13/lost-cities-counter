package es.exploradores.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MarcadorActivity extends OptionsMenuActivity {

	private static final String TAG = "MarcadorActivity";

	private static final int COSTE_EXPEDICION = 20;
	private static final int NUM_CARTAS_BONIFICACION = 8;
	private static final int BONIFICACION = 20;

	private static final String ID_NOMBRE_JUGADOR1 = "NOMBRE_JUGADOR_1";
	private static final String ID_NOMBRE_JUGADOR2 = "NOMBRE_JUGADOR_2";

	private static final int[][][] ids = {
								{
									{R.id.j1r1c1,R.id.j1r1c2,R.id.j1r1c3,R.id.j1r1c4,R.id.j1r1c5},
									{R.id.j1r2c1,R.id.j1r2c2,R.id.j1r2c3,R.id.j1r2c4,R.id.j1r2c5},
									{R.id.j1r3c1,R.id.j1r3c2,R.id.j1r3c3,R.id.j1r3c4,R.id.j1r3c5}
								},
								{
									{R.id.j2r1c1,R.id.j2r1c2,R.id.j2r1c3,R.id.j2r1c4,R.id.j2r1c5},
									{R.id.j2r2c1,R.id.j2r2c2,R.id.j2r2c3,R.id.j2r2c4,R.id.j2r2c5},
									{R.id.j2r3c1,R.id.j2r3c2,R.id.j2r3c3,R.id.j2r3c4,R.id.j2r3c5}
								}
							};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Integer[][][] valores = (Integer[][][]) getLastNonConfigurationInstance();
        if (valores!=null) {
    		ponerValores(valores);
    		calcularTodo();
        }

        setNombres();
    }

	public void setNombres() {
		SharedPreferences settings = getPreferences(0);
		setNombre(settings, ID_NOMBRE_JUGADOR1, R.string.jugador1, R.id.jugador1);
		setNombre(settings, ID_NOMBRE_JUGADOR2, R.string.jugador2, R.id.jugador2);
	}

	public void setNombre(SharedPreferences settings, String idPreferences, int idDefaultText, int idView) {
		String nombre = settings.getString(idPreferences, getResources().getString(idDefaultText));
        TextView text = (TextView)findViewById(idView);
        text.setText(nombre);
	}

    public void calcularTodo() {
    	calcularTotalJugador1();
		calcularTotalJugador2();
		calcularGanador();
    }

	private void ponerValores(Integer[][][] valores) {
		int jugador, ronda, color;
		for (jugador=0; jugador<2; jugador++) {
			for (ronda=0; ronda<3; ronda++) {
				for (color=0; color<5; color++) {
					Integer valor = valores[jugador][ronda][color];
					if (valor!=null && valor!=0 ) {
						TextView input = (TextView) findViewById(getIdInput(jugador, ronda, color));
						input.setText(valor.toString());
					}
				}
			}
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
	    return getValores();
	}

	private Integer[][][] getValores() {
		Integer[][][] valores = new Integer[2][3][5];
		for (int jugador=0; jugador<2; jugador++) {
			for (int ronda=0; ronda<3; ronda++) {
				for (int color=0; color<5; color++) {
					valores[jugador][ronda][color] = getIntegerValueOfText(getIdInput(jugador, ronda, color));
				}
			}
		}
		return valores;
	}

    private int getIdInput(int jugador, int ronda, int color) {
		return ids[jugador][ronda][color];
	}

	private void calcularGanador() {
		if (getTotalJugador1()>getTotalJugador2()) {
			pintarGanadorJugador1();
		} else if (getTotalJugador1()<getTotalJugador2()) {
			pintarGanadorJugador2();
		} else {
			pintarEmpate();
		}
	}

	private void calcularTotalJugador1() {
		calcularTotalRonda(R.id.j1r1, R.id.j1r1c1, R.id.j1r1c2, R.id.j1r1c3, R.id.j1r1c4, R.id.j1r1c5);
		calcularTotalRonda(R.id.j1r2, R.id.j1r2c1, R.id.j1r2c2, R.id.j1r2c3, R.id.j1r2c4, R.id.j1r2c5);
		calcularTotalRonda(R.id.j1r3, R.id.j1r3c1, R.id.j1r3c2, R.id.j1r3c3, R.id.j1r3c4, R.id.j1r3c5);
		calcularTotalJugador(R.id.totalj1, R.id.j1r1, R.id.j1r2, R.id.j1r3);
	}

	private void calcularTotalJugador2() {
		calcularTotalRonda(R.id.j2r1, R.id.j2r1c1, R.id.j2r1c2, R.id.j2r1c3, R.id.j2r1c4, R.id.j2r1c5);
		calcularTotalRonda(R.id.j2r2, R.id.j2r2c1, R.id.j2r2c2, R.id.j2r2c3, R.id.j2r2c4, R.id.j2r2c5);
		calcularTotalRonda(R.id.j2r3, R.id.j2r3c1, R.id.j2r3c2, R.id.j2r3c3, R.id.j2r3c4, R.id.j2r3c5);
		calcularTotalJugador(R.id.totalj2, R.id.j2r1, R.id.j2r2, R.id.j2r3);
	}

	private void calcularTotalRonda(int idTotal, int idColor1, int idColor2, int idColor3, int idColor4, int idColor5) {
		TextView totalRonda = (TextView)findViewById(idTotal);
		int total = sumarValorColores(idColor1, idColor2, idColor3, idColor4, idColor5);
		totalRonda.setText(String.valueOf(total));
	}

	private void calcularTotalJugador(int idTotal, int idRonda1, int idRonda2, int idRonda3) {
		TextView totalJugador = (TextView)findViewById(idTotal);
		int total = sumarValorRondas(idRonda1, idRonda2, idRonda3);
		totalJugador.setText(String.valueOf(total));
	}

	private void pintarGanadorJugador1() {
		SharedPreferences settings = getPreferences(0);
		String nombre = settings.getString(ID_NOMBRE_JUGADOR1, getResources().getString(R.string.jugador1));
		pintarGanador(getResources().getString(R.string.gana) + " " + nombre);
	}

	private void pintarGanadorJugador2() {
		SharedPreferences settings = getPreferences(0);
		String nombre = settings.getString(ID_NOMBRE_JUGADOR2, getResources().getString(R.string.jugador2));
		pintarGanador(getResources().getString(R.string.gana) + " " + nombre);
	}

	private void pintarEmpate() {
		pintarGanador(getResources().getText(R.string.empate).toString());
	}

	private void pintarGanador(String texto) {
		TextView ganador = (TextView)findViewById(R.id.resultado);
		ganador.setText(texto);
	}

	private int getTotalJugador1() {
		return sumarValorRondas(R.id.j1r1, R.id.j1r2, R.id.j1r3);
	}

	private int getTotalJugador2() {
		return sumarValorRondas(R.id.j2r1, R.id.j2r2, R.id.j2r3);
	}

	private int sumarValorColores(int id1, int id2, int id3, int id4, int id5) {
		Integer iTotal1 = getIntegerValueOfText(id1);
		Integer iTotal2 = getIntegerValueOfText(id2);
		Integer iTotal3 = getIntegerValueOfText(id3);
		Integer iTotal4 = getIntegerValueOfText(id4);
		Integer iTotal5 = getIntegerValueOfText(id5);
		return iTotal1.intValue() + iTotal2.intValue() + iTotal3.intValue() + iTotal4.intValue() + iTotal5.intValue();
	}

	private int sumarValorRondas(int id1, int id2, int id3) {
		Integer iTotal1 = getIntegerValueOfText(id1);
		Integer iTotal2 = getIntegerValueOfText(id2);
		Integer iTotal3 = getIntegerValueOfText(id3);
		return iTotal1.intValue() + iTotal2.intValue() + iTotal3.intValue();
	}

	private Integer getIntegerValueOfText(int id) {
		TextView view = (TextView)findViewById(id);
		try {
			if (view.getText().toString()!=null && view.getText().toString()!="") {
				return Integer.valueOf(view.getText().toString());
			} else {
				return 0;
			}
		} catch (Exception e) {
			Log.d(TAG, "Error al obtener el valor del campo con id " + id);
			return 0;
		}
	}

	public void calcularExpedicion(final View view) {
		Log.d(TAG, "calcularExpedicion");
		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.expedicion, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(MarcadorActivity.this);
		builder.setView(layout)
			.setTitle(R.string.expedicion)
			.setPositiveButton(R.string.aceptar,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								int total = totalExpedicion(dialog);
								((TextView)view).setText(String.valueOf(total));
								calcularTodo();
								dialog.cancel();
							}
						})
				.setNegativeButton(R.string.cancelar,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.create().show();
	}

	private int totalExpedicion(DialogInterface dialog) {
		AlertDialog alertDialog = (AlertDialog) dialog;
		CheckBox contrato1 = (CheckBox)alertDialog.findViewById(R.id.contrato1);
		CheckBox contrato2 = (CheckBox)alertDialog.findViewById(R.id.contrato2);
		CheckBox contrato3 = (CheckBox)alertDialog.findViewById(R.id.contrato3);
		CheckBox carta2 = (CheckBox)alertDialog.findViewById(R.id.carta2);
		CheckBox carta3 = (CheckBox)alertDialog.findViewById(R.id.carta3);
		CheckBox carta4 = (CheckBox)alertDialog.findViewById(R.id.carta4);
		CheckBox carta5 = (CheckBox)alertDialog.findViewById(R.id.carta5);
		CheckBox carta6 = (CheckBox)alertDialog.findViewById(R.id.carta6);
		CheckBox carta7 = (CheckBox)alertDialog.findViewById(R.id.carta7);
		CheckBox carta8 = (CheckBox)alertDialog.findViewById(R.id.carta8);
		CheckBox carta9 = (CheckBox)alertDialog.findViewById(R.id.carta9);
		CheckBox carta10 = (CheckBox)alertDialog.findViewById(R.id.carta10);
		CheckBox[] contratos = {contrato1, contrato2, contrato3};
		CheckBox[] cartas = {contrato1, contrato2, contrato3, carta2, carta3, carta4, carta5, carta6, carta7, carta8, carta9, carta10};
		CheckBox[] cartasPuntos = {carta2, carta3, carta4, carta5, carta6, carta7, carta8, carta9, carta10};
		int numContratosExpedicion = getNumChecked(contratos);
		int numCartasExpedicion = getNumChecked(cartas);
		int totalCartas = getTotalCartas(cartasPuntos);
		int total = calcularTotalExpedicion(numContratosExpedicion,	numCartasExpedicion, totalCartas);
		return total;
	}

	public int calcularTotalExpedicion(int numContratosExpedicion, int numCartasExpedicion, int totalCartas) {
		int total = 0;
		if (numCartasExpedicion>0) {
			total = totalCartas - COSTE_EXPEDICION;
			total = total * (numContratosExpedicion+1);
			if (numCartasExpedicion>=NUM_CARTAS_BONIFICACION) {
				total = total + BONIFICACION;
			}
		}
		return total;
	}

	private int getTotalCartas(CheckBox[] cartas) {
		int total = 0;
		for (int i=0; i<cartas.length; i++)
			if (cartas[i].isChecked()) {
				total += (i+2);
			}
		return total;
	}

	private int getNumChecked(CheckBox[] cartas) {
		int total = 0;
		for (int i=0; i<cartas.length; i++)
			if (cartas[i].isChecked()) {
				total++;
			}
		return total;
	}

	public void cambiarNombre(final View view) {
		Log.d(TAG, "cambiarNombre");
		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.nombre, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(MarcadorActivity.this);
		builder.setView(layout)
			.setTitle(R.string.nombre_titulo)
			.setPositiveButton(R.string.aceptar,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								saveNombre(dialog, view);
								dialog.cancel();
							}
						})
				.setNegativeButton(R.string.cancelar,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.create().show();
	}

	private void saveNombre(DialogInterface dialog, View view) {
		AlertDialog alertDialog = (AlertDialog) dialog;
		EditText edit = (EditText)alertDialog.findViewById(R.id.nombre);
		((TextView)view).setText(edit.getText());

		int idView = view.getId();
		switch (idView) {
		case R.id.jugador1:
			saveSharedNombre(ID_NOMBRE_JUGADOR1, edit.getText());
			break;
		case R.id.jugador2:
			saveSharedNombre(ID_NOMBRE_JUGADOR2, edit.getText());
			break;
		}
	}

	private void saveSharedNombre(String idNombreShared, Editable text) {
		SharedPreferences settings = getPreferences(0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(idNombreShared, text.toString());
		editor.commit();
	}
}