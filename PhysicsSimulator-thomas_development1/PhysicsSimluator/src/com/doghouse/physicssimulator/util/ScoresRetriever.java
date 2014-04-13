package com.doghouse.physicssimulator.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.doghouse.physicssimluator.model.LevelScores;

import android.os.AsyncTask;
import android.text.format.DateUtils;

public class ScoresRetriever {
	
	public static final String API_ENDPOINT = "http://psuedo-surface-server.herokuapp.com";
	public static final String API_PASSWORD = "TODO PASSWORD";
	
	public interface ScoresListener{
		public void onScoresRetrieved(ArrayList<LevelScores> scores_list);
	}
	
	private ScoresTask task;
	private ScoresListener listener;
	
	private ArrayList<LevelScores> scores;

	public ScoresRetriever(ScoresListener listener){
		task = new ScoresTask();
		this.listener = listener;
	}
	
	public void startFetchingScores(){
		task.execute(new Void[]{});
	}
	
	
	
	
	private class ScoresTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
		StringBuffer sb = new StringBuffer("");	
		try{
	    	HttpParams httpParams = new BasicHttpParams();
	    	int some_reasonable_timeout = (int) (20 * DateUtils.SECOND_IN_MILLIS);
	    	HttpConnectionParams.setConnectionTimeout(httpParams, some_reasonable_timeout);
	    	HttpConnectionParams.setSoTimeout(httpParams, some_reasonable_timeout);
	    	
	        HttpClient client = new DefaultHttpClient(httpParams);
	        HttpGet request = new HttpGet();
	        //Log.d(TAG, run_url);
			request.setURI(new URI(API_ENDPOINT+"/GetAll"));
			Header authenticationHeader = new BasicHeader("PSS_authenticate", API_PASSWORD);
			request.addHeader(authenticationHeader);
	        HttpResponse response = client.execute(request);
	
	        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        
	        String line = "";
	        String NL = System.getProperty("line.separator");
	        while ((line = in.readLine()) != null) {
	            sb.append(line + NL);
	        }
	        in.close();
        
		}
		catch(Exception e){
			e.printStackTrace();
		}
        try {
			JSONObject json = new JSONObject(sb.toString());
			
			scores = new ArrayList<LevelScores>();
			
			JSONArray scoresArray = json.getJSONArray(LevelScores.LEVEL_SCORES);
			for(int i = 0; i < scoresArray.length(); i++){
				scores.add(new LevelScores(scoresArray.getJSONObject(i))); // this json object contains a level id and an array of scores
			}
			
		} catch (JSONException e) {e.printStackTrace();}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			listener.onScoresRetrieved(scores);
			
		}
	}
}
