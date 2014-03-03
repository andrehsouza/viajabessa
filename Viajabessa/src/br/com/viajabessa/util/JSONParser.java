package br.com.viajabessa.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.http.AndroidHttpClient;

public class JSONParser {
	
	private static AndroidHttpClient client;
	private static HttpGet post;
	
	public static void cancela() {
		client.getConnectionManager().shutdown();
		client.close();
	    post.abort();		
	}
	
	public static JSONObject getJSONFromUrl(String url) throws IOException, JSONException {
		JSONObject jsonObj = null;

		post = new HttpGet(url);

		client = AndroidHttpClient.newInstance("DATA");
		HttpResponse response = client.execute(post);

		HttpEntity httpEntity = response.getEntity();
		String result = EntityUtils.toString(httpEntity);

		client.close();
		jsonObj = new JSONObject(result);

		return jsonObj;
	}
	
	//Só para testes, poder ler do aquivo
	public static JSONObject loadJSONFromAsset(Context c, String nomeArquivo) throws IOException, JSONException {
		String json = null;
		JSONObject jsonObj = null;
		
		InputStream is = c.getAssets().open(nomeArquivo);

		int size = is.available();

		byte[] buffer = new byte[size];

		is.read(buffer);
		is.close();

		json = new String(buffer, "Cp1252");
		jsonObj = new JSONObject(json);
		
		return jsonObj;
	}
}
