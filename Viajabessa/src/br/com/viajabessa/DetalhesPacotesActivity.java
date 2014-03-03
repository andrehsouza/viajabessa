package br.com.viajabessa;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import br.com.viajabessa.pojo.Pacote;

public class DetalhesPacotesActivity extends ActionBarActivity {
	
	private DetalhesPacotesFragment telaDetalhes;
	private ActionBar actionBar;
	private Pacote pacote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_detalhes_activity);
		
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		telaDetalhes = (DetalhesPacotesFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);
		Bundle data = getIntent().getExtras();
		pacote = data.getParcelable("pacote");
		telaDetalhes.setPacote(pacote);		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:		
			finish();
			return true;
		default:
	        return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Intent intent = new Intent();
		intent.putExtra("pacote", pacote);				
		setResult(0, intent);
		finish();
		super.onConfigurationChanged(newConfig);   
	}
}
