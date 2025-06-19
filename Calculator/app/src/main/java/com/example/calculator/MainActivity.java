package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.calculator.databinding.ActivityMainBinding;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ScreenModel screenmodel;
    private HistoryModel historymodel;
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
    boolean dot = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        historymodel = new ViewModelProvider(this).get(HistoryModel.class);
        screenmodel = new ViewModelProvider(this).get(ScreenModel.class);
        screenmodel.getString().observe(this, string ->{
            binding.tvScreen.setText(string);
        });
        historymodel.getHistory().observe(this, strings ->{
            String temp ="";
            int n = strings.size();
            if(n==1){
                temp = strings.get(0);
            } else if (n > 1){
                temp = strings.get(n-2) +" " + strings.get(n-1);
            }
            binding.tvHistory.setText(temp);
        });
    }
    public void ButtonHandler(View v) {
        String btText = ((Button)v).getText().toString();
        String str = screenmodel.getString().getValue();
        char last = '0';
        if (!"".equals(str)){
            last = str.charAt(str.length() - 1);
        }
        switch (btText){
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                screenmodel.addString(btText);
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                dot = false;
                if(last == '+' || last == '-' || last == '*' || last == '/'){
                    screenmodel.removeLast();
                }
                screenmodel.addString(btText);
                break;
            case "DEL":
                if(last == '.'){
                    dot = false;
                }
                screenmodel.removeLast();
                break;
            case "=":
                if (last == '+' || last == '-' || last == '*' || last == '/'){
                    calculatorWarning("Lỗi: Dư toán tử " + last + "ở cuối.");
                }else if (str.length() >0 ){
                    dot = false;
                    solve();
                    screenmodel.clear();
                } else {
                    historymodel.addHistory("0");
                    screenmodel.clear();
                }
                break;
            case ".":
                if(!dot){
                    dot = true;
                    screenmodel.addString(btText);
                }else {
                    calculatorWarning("Đã tồn tại dấu thập phân");
                }
                break;
            case "c":
                screenmodel.clear();
                historymodel.clearHistory();
                dot = false;
                break;
        }
    }
    private void calculatorWarning (String s){
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
    private void solve(){
        String str = screenmodel.getString().getValue();
        try {
            historymodel.addHistory(str + "=" + engine.eval(str));
        } catch (ScriptException e) {
            calculatorWarning("Can't calculate");
        }
    }
}
