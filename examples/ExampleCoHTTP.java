package example;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class ExampleCoHTTP {

    public ExampleCoHTTP() {
        get();
        post();
    }

    public void get() {
        // Get
        ListenableFuture<String> responce = CoHTTP.sendGet("https://www.theartex.net/cloud/api/?sec=announcements", "Example App/1.0");
        responce.addListener(() -> {
            try {
                System.out.println(responce.get());
            }
            catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }, command -> command.run());
    }

    public void post() {
        // Create POST parameters
        HashMap<String, String> postParametets = new HashMap<>();
        postParametets.put("sec", "login");
        postParametets.put("username", username);
        postParametets.put("password", hashedPassword);

        // Create POST request
        ListenableFuture<String> out = CoHTTP.sendPost("https://www.theartex.net/cloud/api/", postParametets, "Example-CoHTTP/1.0");

        // Add a listener to wait for response
        out.addListener(() -> {
            try {
                // Get response with `out.get()`
                String response = out.get();
                // Do what you want with response
                // In this case the response is JSON and you can go from here
            }
            catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }, command -> command.run());
    }
}
