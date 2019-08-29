package br.com.bi.dw.dwbi;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DWBI extends AppCompatActivity {
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwbi);
    }

    public void acessobi(View view){
        Intent intent = new Intent(this, BI.class);
        startActivity(intent);
    }

    public void acessoarquivo(View view){
        Intent intent = new Intent(this, Arquivos.class);
        startActivity(intent);
    }

    public void teste(View view) {
        Uri uri = Uri.parse("https://drive.google.com/drive/folders/1fmqqtljyMauTlm0euwIyf_rDh4SgCBaN?usp=sharing");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intent);
    }

    public void binavegador(View view) {
        Uri uri = Uri.parse("http://vend.marchezanmetais.com.br:8180/dwbi/portal.xhtml");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intent);
    }

    public void atualiza(View view){
        Uri uri = Uri.parse("http://www.marchezanmetais.com.br/DWBI.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
