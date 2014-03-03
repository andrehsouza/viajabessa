package br.com.viajabessa.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Pacote implements Parcelable {
	
	private int id;
	private String nome;
	private int valor;
	private String foto;
	private String descrica;
	
	public Pacote() {
		 
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getValor() {
		return valor;
	}
	
	public void setValor(int valor) {
		this.valor = valor;
	}
	
	public String getFoto() {
		return foto;
	}
	
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public String getDescrica() {
		return descrica;
	}
	
	public void setDescrica(String descrica) {
		this.descrica = descrica;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getId());
		dest.writeString(getNome());
		dest.writeInt(getValor());
		dest.writeString(getFoto());
		dest.writeString(getDescrica());
	}
	
	
    public Pacote (Parcel in) {
    	setId(in.readInt());
    	setNome(in.readString());
    	setValor(in.readInt());
    	setFoto(in.readString());
    	setDescrica(in.readString());
    }
    
    public static final Parcelable.Creator<Pacote> CREATOR = new Parcelable.Creator<Pacote>() {
        public Pacote createFromParcel(Parcel in) {
            return new Pacote(in); 
        }

        public Pacote[] newArray(int size) {
            return new Pacote[size];
        }
    };
}
