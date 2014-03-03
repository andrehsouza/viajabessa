package br.com.viajabessa;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import br.com.viajabessa.adapter.ListPacotesAdapter;
import br.com.viajabessa.pojo.Pacote;
import br.com.viajabessa.util.Constants;
import br.com.viajabessa.util.JSONParser;
import br.com.viajabessa.util.ViajabessaImages;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ListPacotesFragment extends Fragment {
	
	 private View rootView;
	 private ArrayList<Pacote> listaDePacotes = new ArrayList<Pacote>();
	 private ImageLoader imageLoader;
	 private ListView listaPacote;
	 private ListPacotesAdapter pacoteAdapter;
	 private int ultimaPosicao;
	 private OnItemSelectedListener itemSelectedListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tela_lista_pacotes_fragment, container, false);		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		
		if (savedInstanceState != null) {
			listaDePacotes = savedInstanceState.getParcelableArrayList(Constants.LISTA);
			ultimaPosicao = savedInstanceState.getInt(Constants.POSICAO_LISTA);
			setDtalhes();
		} else {
			ultimaPosicao = 0;
			new BuscaPacotes().execute("");					
		}				
		
		imageLoader = ViajabessaImages.getImageLoader();	
		iniciaLista();
		iniciaAdapter();
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		if (activity instanceof OnItemSelectedListener) {
			itemSelectedListener = (OnItemSelectedListener) activity;
		}
		super.onAttach(activity);
	}
	
	private void iniciaLista() {	
		listaPacote = (ListView) rootView.findViewById(R.id.lista_pacotes);			
		listaPacote.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ultimaPosicao = position;
				itemSelectedListener.onItemSelected(listaDePacotes.get(ultimaPosicao));
			}
		});		
	}
	
	private void iniciaAdapter() {
		pacoteAdapter = new ListPacotesAdapter(getActivity(), listaDePacotes, imageLoader);
		listaPacote.setAdapter(pacoteAdapter);		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_lista_pacotes, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.atualizar:
			// TODO
			return true;
		default:
			return false;
		}
	}
		
	@Override
    public void onSaveInstanceState(Bundle outState) {
    	outState.putParcelableArrayList(Constants.LISTA, listaDePacotes);
    	outState.putInt(Constants.POSICAO_LISTA, ultimaPosicao);
	}
	
	@Override
	public void onDetach() {
		ViajabessaImages.fechaImageLoader();
		super.onDetach();
	}
	
	private class BuscaPacotes extends AsyncTask<String, Void, JSONObject> {
		
		private boolean erro = false;
		
		@Override
		protected JSONObject doInBackground(String...strings) {
			
			JSONObject jsonObj = null;

			try {

				//Quando a API existir, usará este método:
				//Os parametros são respectivamente: número da página (para uma futura paginação), versão, marca e modelo
				//JSONParser.getJSONFromUrl(String.format(Constants.API_BASE, ""+numeroPagina, ""+android.os.Build.VERSION.SDK_INT, android.os.Build.MANUFACTURER, android.os.Build.MODEL));
				
				//Como a API ainda não existe, lê-se de um arquivo
				jsonObj = JSONParser.loadJSONFromAsset(getActivity(), "pacotes.txt");
				

			} catch (JSONException e) {	
				System.out.println("ERRO JSON"+e);
				erro = true;
			} catch (IOException e) {
				System.out.println("ERRO IO");
				erro = true;
			} 
			return jsonObj;
		}		
		
		@Override
		protected void onPreExecute() {
			erro = false;
			super.onPreExecute();
		}		
		
		@Override
	    protected void onPostExecute(JSONObject jsonObj) {
			if (jsonObj != null) {
				try {
					//Se não houver erro no json
					if (!jsonObj.getBoolean(Constants.TAG_ERRO)) {
						JSONArray tituloFilmes = jsonObj.getJSONArray(Constants.TAG_PACOTES);
						addItensLista(tituloFilmes);
					} else {						
						erro = true;
					}
				} catch (JSONException e) {
					erro = true;
				}
			}
						
			if(erro) {
				Toast.makeText(getActivity(), R.string.texto_toast_erro, Toast.LENGTH_SHORT).show();
				listaDePacotes.clear();
			} else {
				setDtalhes();
			}
		}	
	}
	
	private void addItensLista(JSONArray pacotes) throws JSONException {
		
		for (int i = 0; i < pacotes.length(); i++) {
			JSONObject obj = pacotes.getJSONObject(i);
			
			if(obj != null) {
				
				Pacote pacote = new Pacote();
				
				pacote.setId(obj.getInt(Constants.TAG_ID));
				pacote.setNome(obj.getString(Constants.TAG_NOME));	
				pacote.setDescrica(obj.getString(Constants.TAG_DESCRICAO));
				pacote.setValor(obj.getInt(Constants.TAG_VALOR));
				pacote.setFoto(obj.getString(Constants.TAG_FOTO));
				
				listaDePacotes.add(pacote);
			}			
		}
	}
	
	public interface OnItemSelectedListener {
		public void onItemSelected(Pacote pacote);
	}
	
	private void setDtalhes() {
		if(((TelaPrincipalActivity) getActivity()).checkContainer2() && listaDePacotes.size() > 0) {
			itemSelectedListener.onItemSelected(listaDePacotes.get(ultimaPosicao));
		}
	}
}
