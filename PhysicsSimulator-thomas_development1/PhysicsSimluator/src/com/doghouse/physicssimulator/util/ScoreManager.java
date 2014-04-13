package com.doghouse.physicssimulator.util;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.doghouse.physicssimluator.model.LevelScores;
import com.doghouse.physicssimluator.model.Score;

import android.os.AsyncTask;
import android.text.format.DateUtils;

public class ScoreManager {
	
	private static final String API_ENDPOINT = ScoresRetriever.API_ENDPOINT;
	
//	public interface SubmissionListener{
//		public void onScoreSubmissionResult(boolean is_high_score,ScoreManager manager);
//	}
	
//	private SubmissionListener listener;
	
//	private int high_score_id = -1;
	
	
//	public ScoreManager(SubmissionListener listener){
//			this.listener = listener;
//	}
	
	
	// the following sends the score to the database to see if its a high score. If it is a high score it stores the high score id 
	// and notifies the user SubmssionListener that a high score was recorded and asks for a name
	
//	public void submitLevelScore(int level_id,int stars, long time){
//		new ScoreTask().execute(level_id,stars,time);
//	}
//	
//	private JSONObject sendLevelScoreToDb(int level_id, int stars, long time) throws JSONException{
//		
//		JSONObject json = new JSONObject();
//		json.put(LevelScores.LEVEL_ID, level_id);
//		json.put(LevelScores.STARS, stars);
//		json.put(LevelScores.TIME, time);
//		
//		
//		StringBuffer sb = new StringBuffer("");	
//		try{
//	    	HttpParams httpParams = new BasicHttpParams();
//	    	int some_reasonable_timeout = (int) (20 * DateUtils.SECOND_IN_MILLIS);
//	    	HttpConnectionParams.setConnectionTimeout(httpParams, some_reasonable_timeout);
//	    	HttpConnectionParams.setSoTimeout(httpParams, some_reasonable_timeout);
//	    	
//	        HttpClient client = new DefaultHttpClient(httpParams);
//	        HttpPost request = new HttpPost();
//	        StringEntity entity = new StringEntity(json.toString(), HTTP.UTF_8);
//	        entity.setContentType("application/json");
//	        request.setEntity(entity);
//
//			request.setURI(new URI(API_ENDPOINT+"/SubmitScore"));
//			Header authenticationHeader = new BasicHeader("PSS_authenticate", ScoresRetriever.API_PASSWORD);
//			request.addHeader(authenticationHeader);
//	        HttpResponse response = client.execute(request);
//	
//	        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//	        
//	        String line = "";
//	        String NL = System.getProperty("line.separator");
//	        while ((line = in.readLine()) != null) {
//	            sb.append(line + NL);
//	        }
//	        in.close();
//	        
//	        return new JSONObject(sb.toString());
//	        
////	        if(json.getBoolean("is_high_score")){
////	        	listener.onScoreSubmissionResult(true);
////	        }
//        
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return null;
//	}
//	
//	private class ScoreTask extends AsyncTask<Object,Object,Object>{
//
//
//		@Override
//		protected Object doInBackground(Object... params) {
//			
//			Integer level_id = (Integer) params[0];
//			Integer stars = (Integer) params[1];
//			Long time = (Long) params[2];
//			
//			JSONObject json = null;
//			try {
//				json = sendLevelScoreToDb(level_id, stars, time);
//			} catch (JSONException e) {e.printStackTrace();}
//			
//			return json;
//		}
//		
//		@Override
//		protected void onPostExecute(Object result) {
//			super.onPostExecute(result);
//			
//			JSONObject json = (JSONObject) result;
//			
//	    try {
//			if(json.getBoolean("is_high_score")){
//				high_score_id = json.getInt("id");
//				listener.onScoreSubmissionResult(true,ScoreManager.this);
//			}else{
//				high_score_id = -1;
//			}
//		} catch (JSONException e) {e.printStackTrace();}
//			
//			
//		}
//	}
	
	
	
	// the following deals in sending the name associated with the high score id to the database
	
	public void submitHighScoreName(Score score){
		new NameSubmissionTask().execute(score);
	}
	
	private class NameSubmissionTask extends AsyncTask<Object,Object,Object>{

		@Override
		protected Object doInBackground(Object... params) {
			
			try {
				sendNameToDb((Score)params[0]);
			} catch (JSONException e) {e.printStackTrace();}

			
			return null;
		}
		
	}
	
	private void sendNameToDb(Score score) throws JSONException{
		
		JSONObject json = new JSONObject();
		json.put(LevelScores.LEVEL_ID, score.getLevel());
		json.put(LevelScores.STARS, score.getStars());
		json.put(LevelScores.TIME, score.getTime());
		json.put(LevelScores.NAME, score.getName());
		
		
		try{
	    	HttpParams httpParams = new BasicHttpParams();
	    	int some_reasonable_timeout = (int) (20 * DateUtils.SECOND_IN_MILLIS);
	    	HttpConnectionParams.setConnectionTimeout(httpParams, some_reasonable_timeout);
	    	HttpConnectionParams.setSoTimeout(httpParams, some_reasonable_timeout);
	    	
	        HttpClient client = new DefaultHttpClient(httpParams);
	        HttpPost request = new HttpPost();
	        StringEntity entity = new StringEntity(json.toString(), HTTP.UTF_8);
	        entity.setContentType("application/json");
	        request.setEntity(entity);

			request.setURI(new URI(API_ENDPOINT+"/SubmitScore"));
			Header authenticationHeader = new BasicHeader("PSS_authenticate", ScoresRetriever.API_PASSWORD);
			request.addHeader(authenticationHeader);
	        client.execute(request);
	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
