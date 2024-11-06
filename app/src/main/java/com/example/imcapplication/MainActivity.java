package com.example.imcapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText edAltura, edPeso;
    private RadioGroup radioGroup;
    private RadioButton selectedRadioButton;
    private TextView imcText, situacaoText;
    private Button btnCalcular, btnLimpar, btnPesoIdeal;
    private String pesoIdealText, radioButtonSelectedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recebimento dos dados
        edAltura = findViewById(R.id.edAltura);
        edPeso = findViewById(R.id.edPeso);
        imcText = findViewById(R.id.imc);
        situacaoText = findViewById(R.id.situacao);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnLimpar = findViewById(R.id.btnLimpar);
        btnPesoIdeal = findViewById(R.id.btnPesoIdeal);
        radioGroup = findViewById(R.id.radiogroup);

        //Listener dos botões
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1)
                {
                    selectedRadioButton = findViewById(selectedId);
                    radioButtonSelectedText = selectedRadioButton.getText().toString();
                    Log.d("MainActivity", "Selecionado: " + radioButtonSelectedText);
                }
                else
                {
                    showToast(getString(R.string.gender_error));
                    return;
                }

                calcular(view);
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                limpar(view);
            }
        });

        btnPesoIdeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                pesoIdeal(view);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle bld)
    {
        super.onSaveInstanceState(bld);
    }

    //MÉTODOS PARA ACTIVITY
    public void calcular(View view) {
        //Recebimento dos dados
        double altura = 0, peso=0, imc;

        try
        {
            altura = Double.parseDouble(edAltura.getText().toString());
            peso = Double.parseDouble(edPeso.getText().toString());
        }
        catch (NumberFormatException e)
        {
            Log.e("MainActivity", "Erro ao converter para double: " + e.getMessage());
            return;
        }

        //Verificando se os campos estão preenchidos
        if (altura <= 0)
        {
            showToast(getString(R.string.altura_error));
            return;
        }
        if (peso <= 0)
        {
            showToast(getString(R.string.peso_error));
            return;
        }

        //Cálculo do IMC
        imc = peso / Math.pow(altura,2);

        //Verificando qual o gênero selecionado, mostra o IMC, mostra a situação
        verificarImcPorGenero(imc);

        //Calcula o peso ideal por gênero e altura
        calcularPesoIdeal(altura);

        btnPesoIdeal.setVisibility(View.VISIBLE);
    }

    public void limpar(View view) {
        imcText.setText("");
        situacaoText.setText("");
        edAltura.setText("");
        edPeso.setText("");
        radioGroup.clearCheck();
        btnPesoIdeal.setVisibility(View.INVISIBLE);
        showToast(getString(R.string.campos_limpos));
        findViewById(R.id.edAltura).requestFocus();
    }

    public void pesoIdeal(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dlog_title);
        builder.setMessage(pesoIdealText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //MÉTODOS PARA MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //MÉTODOS AUXILIARES
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        return;
    }

    public void verificarImcPorGenero(double imc) {
        if (radioButtonSelectedText.equals("Masculino")||radioButtonSelectedText.equals("Male"))
        {
            if (imc < 20.7)
            {
                situacaoText.setText(R.string.imc_abaixo_peso);
            }
            else if (imc < 26.4)
            {
                situacaoText.setText(R.string.imc_peso_ideal);
            }
            else if (imc < 27.8)
            {
                situacaoText.setText(R.string.imc_pouco_acima_peso);
            }
            else if (imc < 31.1)
            {
                situacaoText.setText(R.string.imc_acima_peso);
            }
            else
            {
                situacaoText.setText(R.string.imc_obesidade);
            }

            imcText.setText(String.format("IMC: %.2f", imc));
        }

        if (radioButtonSelectedText.equals("Feminino")||radioButtonSelectedText.equals("Female"))
        {
            if (imc < 19.1)
            {
                situacaoText.setText(R.string.imc_abaixo_peso);
            }
            else if (imc < 25.8)
            {
                situacaoText.setText(R.string.imc_peso_ideal);
            }
            else if (imc < 27.3)
            {
                situacaoText.setText(R.string.imc_pouco_acima_peso);
            }
            else if (imc < 32.3)
            {
                situacaoText.setText(R.string.imc_acima_peso);
            }
            else
            {
                situacaoText.setText(R.string.imc_obesidade);
            }

            imcText.setText(String.format("IMC: %.2f", imc));
        }
    }

    public void calcularPesoIdeal(double altura) {
        double imcMascMin = 20.7, imcMascMax = 26.4;
        double imcFemMin = 19.1, imcFemMax = 25.8;
        double pesoMin, pesoMax;

        if (radioButtonSelectedText.equals("Masculino")||radioButtonSelectedText.equals("Male")){
            pesoMin = imcMascMin * Math.pow(altura,2);
            pesoMax = imcMascMax * Math.pow(altura,2);

            pesoIdealText = getString(R.string.peso_ideal_text, pesoMin, pesoMax);
        }

        if (radioButtonSelectedText.equals("Feminino")||radioButtonSelectedText.equals("Female")){
            pesoMin = imcFemMin * Math.pow(altura,2);
            pesoMax = imcFemMax * Math.pow(altura,2);

            pesoIdealText = getString(R.string.peso_ideal_text, pesoMin, pesoMax);
        }
    }
}