/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davis.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author ADMIN
 */
public class Firebase {
    
    public void get(){
        try {
            // URL của Firebase Realtime Database
            String firebaseUrl = "https://your-project-id.firebaseio.com/data.json";

            // Tạo URL connection
            URL url = new URL(firebaseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Đọc dữ liệu từ response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // In ra dữ liệu nhận được
            System.out.println("Dữ liệu từ Firebase:");
            System.out.println(response.toString());

            // Đóng kết nối
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void post(){
        try {
            // URL của Firebase Realtime Database
            String firebaseUrl = "https://your-project-id.firebaseio.com/data.json";
            
            // Dữ liệu bạn muốn gửi lên Firebase
            String data = "{\"key1\": \"value1\", \"key2\": \"value2\"}";
            
            // Tạo URL connection
            URL url = new URL(firebaseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            // Ghi dữ liệu vào request body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = data.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Lấy response code
            int responseCode = conn.getResponseCode();
            
            // Kiểm tra kết quả
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Dữ liệu đã được gửi thành công lên Firebase.");
            } else {
                System.out.println("Có lỗi xảy ra khi gửi dữ liệu lên Firebase: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
