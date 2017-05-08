package com.coalesce.http;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class CoHTTP {

	private static ListeningExecutorService executor;

    // HTTP GET request
    public static ListenableFuture<String> sendGet(String url, String userAgent) throws Exception {

		ListenableFuture<String> future = getExecutor().submit(() -> {

			StringBuffer response = new StringBuffer();

			try {
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				// optional default is GET
				con.setRequestMethod("GET");

				// add request header
				con.setRequestProperty("User-Agent", userAgent);

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return response.toString();
		});

		return future;
    }

    // HTTP POST request
    public static ListenableFuture<String> sendPost(String url, HashMap<String, String> arguments, String userAgent) throws Exception {

		ListenableFuture<String> future = getExecutor().submit(() -> {

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", userAgent);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "";
			StringBuilder sb = new StringBuilder();
			for (String str : arguments.keySet()) {
				if (sb.length() != 0)
					sb.append("&");
				sb.append(str + "=" + arguments.get(str));
			}
			urlParameters = sb.toString();

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// return result
			return response.toString();

		});

        return future;
    }

	private static ListeningExecutorService getExecutor(){
		if (executor == null){
			executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
		}

		return executor;
	}

}
