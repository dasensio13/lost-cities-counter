package es.exploradores.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MarcadorActivity extends Activity {

	private static final String TAG = "MarcadorActivity";

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

        Button calcularButton = (Button) findViewById(R.id.calcular);
        calcularButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				calcularTodo();
			}
		});
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
						EditText edit = (EditText) findViewById(getIdEdit(jugador, ronda, color));
						edit.setText(valor.toString());
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
					valores[jugador][ronda][color] = getIntegerValueOfEdit(getIdEdit(jugador, ronda, color));
				}
			}
		}
		return valores;
	}

    private int getIdEdit(int jugador, int ronda, int color) {
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
		pintarGanador(R.string.jugador1);
	}

	private void pintarGanadorJugador2() {
		pintarGanador(R.string.jugador2);
	}

	private void pintarEmpate() {
		pintarGanador(R.string.empate);
	}

	private void pintarGanador(int idTexto) {
		TextView ganador = (TextView)findViewById(R.id.resultado);
		ganador.setText(idTexto);
	}

	private int getTotalJugador1() {
		return sumarValorRondas(R.id.j1r1, R.id.j1r2, R.id.j1r3);
	}

	private int getTotalJugador2() {
		return sumarValorRondas(R.id.j2r1, R.id.j2r2, R.id.j2r3);
	}

	private int sumarValorColores(int id1, int id2, int id3, int id4, int id5) {
		Integer iTotal1 = getIntegerValueOfEdit(id1);
		Integer iTotal2 = getIntegerValueOfEdit(id2);
		Integer iTotal3 = getIntegerValueOfEdit(id3);
		Integer iTotal4 = getIntegerValueOfEdit(id4);
		Integer iTotal5 = getIntegerValueOfEdit(id5);
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
			return Integer.valueOf(view.getText().toString());
		} catch (Exception e) {
			Log.w(TAG, "Error al obtener el valor del campo con id " + id);
			return 0;
		}
	}

	private Integer getIntegerValueOfEdit(int id) {
		EditText edit = (EditText)findViewById(id);
		try {
			return Integer.valueOf(edit.getText().toString());
		} catch (Exception e) {
			Log.w(TAG, "Error al obtener el valor del campo con id " + id);
			return 0;
		}
	}
}