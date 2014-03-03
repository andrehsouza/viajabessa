package br.com.viajabessa.adapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import br.com.viajabessa.R;
import br.com.viajabessa.pojo.Pacote;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ListPacotesAdapter extends ArrayAdapter<Pacote> implements SectionIndexer  {

	private LayoutInflater inflater;
	private ArrayList<Pacote> listaPacotes;	
	private ImageLoader imageLoader;
	private Typeface tf;
	private SectionIndexer sectionIndexer;
	private List<String> sections;
	private DisplayImageOptions options;

	public ListPacotesAdapter(Context context, ArrayList<Pacote> lista, ImageLoader iml) {
		super(context, R.layout.estilo_lista_pacotes, lista);
		imageLoader = iml;
		opcoes();
		iniciaFonte();
		inflater = ((Activity) context).getLayoutInflater();
		listaPacotes = lista;
	}
	
	@Override
	public void clear() {
		listaPacotes.clear();	
		super.clear();
	}
	
	@Override
	public View getView(int posicao, View convertView, ViewGroup parent) {
		
		PacoteHolder pacoteHolder;

		if (convertView == null) {			
			
			convertView = inflater.inflate(R.layout.estilo_lista_pacotes, null);
			
			pacoteHolder = new PacoteHolder();	
			pacoteHolder.textNome = (TextView) convertView.findViewById(R.id.texto_nome);
			pacoteHolder.textNome.setTypeface(tf);
			pacoteHolder.textValor = (TextView) convertView.findViewById(R.id.text_valor);
			pacoteHolder.textValor.setTypeface(tf);		
			pacoteHolder.imgFoto = (ImageView) convertView.findViewById(R.id.img_foto);
			
			convertView.setTag(pacoteHolder);
			
		} else {
			pacoteHolder = (PacoteHolder) convertView.getTag();
		}

		pacoteHolder.textNome.setText( listaPacotes.get(posicao).getNome());	
		NumberFormat formatoDinheiro = NumberFormat.getCurrencyInstance();
		pacoteHolder.textValor.setText(formatoDinheiro.format(listaPacotes.get(posicao).getValor()));
		
		imageLoader.displayImage(listaPacotes.get(posicao).getFoto(), pacoteHolder.imgFoto, options);
	   	
		return convertView;
	}

	static class PacoteHolder {	
		TextView textNome;
		TextView textValor;
		ImageView imgFoto;
	}		
		
	@Override
	public int getCount() {
	    return listaPacotes.size();
	}	
	
	@Override
	public Pacote getItem(int position) {
		return listaPacotes.get(position);
	}
		
	@Override
	public int getPositionForSection(int section) {
		return getSectionIndexer().getPositionForSection(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		return getSectionIndexer().getSectionForPosition(position);
	}

	@Override
	public Object[] getSections() {
		return getSectionIndexer().getSections();
	}
	
	private SectionIndexer getSectionIndexer() {
		if (sectionIndexer == null) {
			sectionIndexer = createSectionIndexer(listaPacotes);
		}
		return sectionIndexer;
	}
	
	private SectionIndexer createSectionIndexer(List<Pacote> pacotes) {

		return createSectionIndexer(pacotes, new Function<Pacote, String>() {

			@Override
			public String apply(Pacote input) {
				// mostra primeira letra do titulo
				return input.getNome();
			}
		});
	}
	
	private SectionIndexer createSectionIndexer(List<Pacote> pacotes, Function<Pacote, String> sectionFunction) {
		
		sections = new ArrayList<String>();
		final List<Integer> sectionsToPositions = new ArrayList<Integer>();
		final List<Integer> positionsToSections = new ArrayList<Integer>();
		for (int i = 0; i < pacotes.size(); i++) {
			Pacote pacote = pacotes.get(i);
			String section = sectionFunction.apply(pacote);
			if (sections.isEmpty() || !sections.get(sections.size() - 1).equals(section)) {
				if(!sections.contains(section)) {
					// add a new section
					sections.add(section);
					// map section to position
					sectionsToPositions.add(i);
				}				
			}
			
			positionsToSections.add(sections.size() - 1);
		}
		
		final String[] sectionsArray = sections.toArray(new String[sections.size()]);
		
		return new SectionIndexer() {
			
			@Override
			public Object[] getSections() {
				return sectionsArray;
			}
			
			@Override
			public int getSectionForPosition(int position) {
				return positionsToSections.get(position);
			}
			
			@Override
			public int getPositionForSection(int section) {
				if(section >= sections.size()) {
					return sectionsToPositions.get(section-1);
				}
				return sectionsToPositions.get(section);
			}
		};
	}
	
	private void opcoes() {
		options = new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.img_carregamento)
//		.showImageForEmptyUri(R.drawable.img_carregamento)
		.cacheOnDisc(true)
		.cacheInMemory(true)
		.resetViewBeforeLoading(true)
		.displayer(new FadeInBitmapDisplayer(1000))
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
	private void iniciaFonte() {
		tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");		
	}

	public void refreshSections() {
		sectionIndexer = null;
		getSectionIndexer();
	}
	
	public interface Function<E, T> {
		public T apply(E input);
	}	
}