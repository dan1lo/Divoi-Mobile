package com.example.dennis.divoi.activitys;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.fragments.LinguasFragment;


//floatactionbutton


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, ImageButton.OnClickListener {

    private LinguasFragment linguasFragment;
    private String tipoUsuario = "Pesquisador";
    private boolean logado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        tipoUsuario = intent.getStringExtra("tipoUsuario");
        logado = intent.getBooleanExtra("logado", false);
        intent.putExtra("tipoUsuario",tipoUsuario);
        intent.putExtra("logado",logado);

        ImageButton button = (ImageButton) findViewById(R.id.button_cadastrarlingua);
        if(!logado || tipoUsuario.equals("Pesquisador"))
            button.setVisibility(View.GONE);


        if(savedInstanceState == null){
            linguasFragment = new LinguasFragment();
            linguasFragment.setArguments(intent.getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.container,linguasFragment).commit();
        }


        button.setOnClickListener(this);
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.container, new LinguasFragment(), LinguasFragment.TAG);
//        ft.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(logado) {
            switch (tipoUsuario) {
                case "Administrador":
                    getMenuInflater().inflate(R.menu.menu_adm, menu);
                    return true;

                case "Pesquisador":
                    getMenuInflater().inflate(R.menu.menu_divoi_deslogar, menu);
                    return true;

                case "Professor":
                    getMenuInflater().inflate(R.menu.menu_divoi_deslogar, menu);
                    return true;
            }
        }else{
            getMenuInflater().inflate(R.menu.menu_divoi, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("logado", false);
                startActivity(intent);
                break;
            case R.id.action_usuarios:
                intent = new Intent(this, ListaUsuarios.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
     //   linguasFragment.buscar(s);
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {

        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CadastroLinguaActivity.class);
        startActivity(intent);
    }
}
