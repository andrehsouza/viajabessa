package br.com.viajabessa;

import java.text.NumberFormat;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.viajabessa.pojo.Pacote;
import br.com.viajabessa.util.FlowTextHelper;
import br.com.viajabessa.util.ViajabessaImages;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class DetalhesPacotesFragment extends Fragment {
	
	 private View rootView;
	 private Pacote pacote;
	 private ImageView imgFoto, imgFavorito;
	 private TextView textoNome, textoDescricao;
	 private Button btComprar;
	 private ImageLoader imageLoader;
	 private DisplayImageOptions options;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tela_detalhes_pacotes_fragment, container, false);		
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Black.ttf");
		imgFoto = (ImageView) rootView.findViewById(R.id.img_pacote);
		imgFavorito = (ImageView) rootView.findViewById(R.id.img_favorito);
		textoNome = (TextView) rootView.findViewById(R.id.texto_nome_pacote);
		textoNome.setTypeface(tf);
		tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
		textoDescricao = (TextView) rootView.findViewById(R.id.texto_descricao_pacote);
		textoDescricao.setTypeface(tf);	
		btComprar = (Button) rootView.findViewById(R.id.bt_comprar);		
		opcoes();
		return rootView;
	}	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {	
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		if(textoNome != null && pacote != null) {
			textoNome.setText(pacote.getNome().toUpperCase());
			
			View map = rootView.findViewById(R.id.img_map);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			FlowTextHelper.tryFlowText(pacote.getDescrica(), map, textoDescricao, displaymetrics);
						
			imageLoader.displayImage(pacote.getFoto(), imgFoto, options);		
			NumberFormat formatoDinheiro = NumberFormat.getCurrencyInstance();
			btComprar.setText(getActivity().getString(R.string.texto_bt_comprar)+" "+formatoDinheiro.format(pacote.getValor()));
			
			imgFavorito.setTag("nao");
			imgFavorito.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(imgFavorito.getTag().equals("nao")) {
						imgFavorito.setImageResource(R.drawable.ic_favorito_on);
						imgFavorito.setTag("sim");
					} else {
						imgFavorito.setImageResource(R.drawable.ic_favorito_off);
						imgFavorito.setTag("nao");
					}
				}
			});
			
		}			
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_detalhes_pacotes, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.compartilhar:
			// TODO
			return true;
		default:
			return false;
		}
	}
		
	public void setPacote(Pacote pacote) {
		this.pacote = pacote;		
	}	
	
	private void opcoes() {
		imageLoader = ViajabessaImages.getImageLoader();
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(false)
		.resetViewBeforeLoading(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(1000))
		.build();
	}	
	
	@Override
	public void onDetach() {
		ViajabessaImages.fechaImageLoader();
		super.onDetach();
	}	
}
