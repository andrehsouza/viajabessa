package br.com.viajabessa;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import br.com.viajabessa.adapter.NavDrawerListAdapter;
import br.com.viajabessa.navigationdrawer.NavDrawerItem;
import br.com.viajabessa.pojo.Pacote;
import br.com.viajabessa.util.Constants;
import br.com.viajabessa.util.ViajabessaImages;

public class TelaPrincipalActivity extends ActionBarActivity implements ListView.OnItemClickListener, ListPacotesFragment.OnItemSelectedListener {
	
	private ActionBar actionbar;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawer;
	private FragmentTransaction ft;
	private int itemSelecionado;
	private Fragment ultimoFragment;	
	private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter drawerAdapter;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_principal_activity);
		
		actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);		
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
            
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);        
		mDrawer = (ListView) findViewById(R.id.left_drawer);
		mDrawer.setOnItemClickListener(this);
		navDrawerItems = new ArrayList<NavDrawerItem>();		
		
		for(int i=0; i<navMenuTitles.length; i++) {
			 navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1), (i == 0 ? true:false)));
		}
		
		navMenuIcons.recycle();
		drawerAdapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawer.setAdapter(drawerAdapter);
		
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		    
		    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

				public void onDrawerClosed(View drawerView) {				
				}

				public void onDrawerOpened(View drawerView) {
				}
			};
			
			mDrawerLayout.setDrawerListener(mDrawerToggle);
			iniciaImageLoader();
	        
	        if (savedInstanceState != null) {
	        	itemSelecionado = savedInstanceState.getInt(Constants.POSICAO_DRAWER);
	        	ultimoFragment = getSupportFragmentManager().getFragment(savedInstanceState, Constants.ULTIMO_FRAGMENT);
	        } else {
	        	itemSelecionado = 0;
	        }
	        
	        setFragment(new ListPacotesFragment());
	    }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    outState.putInt(Constants.POSICAO_DRAWER, itemSelecionado);
	    getSupportFragmentManager().putFragment(outState, Constants.ULTIMO_FRAGMENT, ultimoFragment);
	    super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
		
	private void iniciaImageLoader() {
		if (!ViajabessaImages.isIniciado()) {
			ViajabessaImages.iniciaImageLoader(TelaPrincipalActivity.this);
		}
	}
	
	private void setFragment(Fragment fragment) {
		ultimoFragment = fragment;
		ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.layout_container, ultimoFragment);
		ft.commit();
	}	


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {	
		return super.onCreateOptionsMenu(menu); 
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawer)) {
				mDrawerLayout.closeDrawer(mDrawer);
			} else {				
				mDrawerLayout.openDrawer(mDrawer);
			}			
			return true;
		default:
	        return super.onOptionsItemSelected(item);
		}
	}
	
	private void selecionaItemDrawer(int posicao) {

		mDrawerLayout.closeDrawer(mDrawer);

		if (itemSelecionado == posicao) {			
			return; // Ja está marcado
		}
				
		switch (posicao) {
		case 0:
			setFragment(new ListPacotesFragment());			
			break;
		}	
		
		drawerAdapter.getItem(itemSelecionado).setSelected(false);
		drawerAdapter.getItem(posicao).setSelected(true);
		drawerAdapter.notifyDataSetInvalidated();
		itemSelecionado = posicao;
	}

	@Override
	public void onItemClick(AdapterView<?> array, View v, int posicao, long arg3) {
		selecionaItemDrawer(posicao);		
	}
	
	@Override
	public void onItemSelected(Pacote pacote) {		
		if(checkContainer2()) {
			DetalhesPacotesFragment detalhes = new DetalhesPacotesFragment();				
			ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.layout_container_2, detalhes);
			ft.commit();
			detalhes.setPacote(pacote);
		} else {
			Intent intent = new Intent(this, DetalhesPacotesActivity.class);
			intent.putExtra("pacote", pacote);
			startActivityForResult(intent, 0);
		}		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case 0:
				Pacote pacote = data.getExtras().getParcelable("pacote");
				onItemSelected(pacote);
				break;
			}
		}
	}	
	
	public boolean checkContainer2() {
		View container2 = findViewById(R.id.layout_container_2);
		return container2 == null ? false:true;
	}
}
