package com.example.imcapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
                    showToast("Por favor, selecione um gênero!");
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
        }

        //Verificando se os campos estão preenchidos
        if (altura <= 0)
        {
            showToast("Altura inválida!");
            return;
        }
        if (peso <= 0)
        {
            showToast("Peso inválido!");
            return;
        }

        //Cálculo do IMC
        imc = peso / Math.pow(altura,2);

        //Verificando qual o gênero selecionado e mostrando o IMC e a situação
        calcularImcPorGenero(imc);
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
        showToast("Campos limpos!");
        findViewById(R.id.edAltura).requestFocus();
    }

    public void pesoIdeal(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Peso Ideal");
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
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void calcularImcPorGenero(double imc) {
        if (radioButtonSelectedText.equals("Masculino"))
        {
            if (imc < 20.7)
            {
                situacaoText.setText("Abaixo do peso");
            }
            else if (imc < 26.4)
            {
                situacaoText.setText("Peso ideal");
            }
            else if (imc < 27.8)
            {
                situacaoText.setText("Pouco acima do peso");
            }
            else if (imc < 31.1)
            {
                situacaoText.setText("Acima do peso");
            }
            else
            {
                situacaoText.setText("Obesidade");
            }

            imcText.setText(String.format("IMC: %.2f", imc));
        }

        if (radioButtonSelectedText.equals("Feminino"))
        {
            if (imc < 19.1)
            {
                situacaoText.setText("Abaixo do peso");
            }
            else if (imc < 25.8)
            {
                situacaoText.setText("Peso ideal");
            }
            else if (imc < 27.3)
            {
                situacaoText.setText("Pouco acima do peso");
            }
            else if (imc < 32.3)
            {
                situacaoText.setText("Acima do peso");
            }
            else
            {
                situacaoText.setText("Obesidade");
            }

            imcText.setText(String.format("IMC: %.2f", imc));
        }
    }

    public void calcularPesoIdeal(double altura) {
        double imcMascMin = 20.7, imcMascMax = 26.4;
        double imcFemMin = 19.1, imcFemMax = 25.8;
        double pesoMin, pesoMax;

        if (radioButtonSelectedText.equals("Masculino")){
            pesoMin = imcMascMin * Math.pow(altura,2);
            pesoMax = imcMascMax * Math.pow(altura,2);

            pesoIdealText = String.format("O peso ideal está entre %.2f kg à %.2f Kg",pesoMin,pesoMax);
        }

        if (radioButtonSelectedText.equals("Feminino")){
            pesoMin = imcFemMin * Math.pow(altura,2);
            pesoMax = imcFemMax * Math.pow(altura,2);

            pesoIdealText = String.format("O peso ideal está entre %.2f Kg à %.2f Kg",pesoMin,pesoMax);
        }
    }
}