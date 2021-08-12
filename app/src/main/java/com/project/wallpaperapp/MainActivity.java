package com.project.wallpaperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public List<ImageModel> imageModelList;
    public RecyclerView recyclerView;
    public ListAdapter adapter;
    public RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageModelList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        requestQueue = Volley.newRequestQueue(this);
        adapter = new ListAdapter(imageModelList, this);

        getData();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void getData(){
        String url = "https://api.pexels.com/v1/curated?per_page=80";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Data from API
                try {
                    JSONObject baseObject = new JSONObject(response);
                    JSONArray baseArray = baseObject.getJSONArray("photos");

                    for(int i = 0; i < baseArray.length(); i++)
                    {
                        JSONObject arrayObject = baseArray.getJSONObject(i);
                        JSONObject innerObject = arrayObject.getJSONObject("src");

                        String url = innerObject.getString("original");

                        ImageModel model = new ImageModel();
                        model.setSrc(url);

                        imageModelList.add(model);
                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error from the responce
//                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Authorization", "563492ad6f9170000100000156bab13244074bcfadbf149a83233880");

                return hashMap;
            }
        };

        requestQueue.add(request);
    }
}
