package weather;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Date;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			
		
			String api = "c9cc73e510128205e0077b4435154c8a";
			String city = request.getParameter("city");
			String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + api;
			
			 URL url = new URL(apiUrl);
	         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	         connection.setRequestMethod("GET");
			
			InputStream inputStream = connection.getInputStream();
	        InputStreamReader reader = new InputStreamReader(inputStream);
	       // System.out.println(reader);
	        
	        Scanner scanner = new Scanner(reader);
	        StringBuilder responseContent = new StringBuilder();
	
	        while (scanner.hasNext()) {
	            responseContent.append(scanner.nextLine());
	        }
	        
	//        System.out.println(responseContent);
	        scanner.close();
			
	        Gson gson = new Gson();
	        JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
	        System.out.println(jsonObject);
	        
	        long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
	        String date = new Date(dateTimestamp).toString();
	        
	        //Temperature
	        double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
	        int temperatureCelsius = (int) (temperatureKelvin - 273.15);
	       
	        //Humidity
	        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
	        
	        //Wind Speed
	        double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
	        
	        //Weather Condition
	        String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
	        
	        
	        request.setAttribute("date", date);
	        request.setAttribute("city", city);
	        request.setAttribute("temperature", temperatureCelsius);
	        request.setAttribute("weatherCondition", weatherCondition); 
	        request.setAttribute("humidity", humidity);    
	        request.setAttribute("windSpeed", windSpeed);
	        request.setAttribute("weatherData", responseContent.toString());
	        
	        connection.disconnect();
	        request.getRequestDispatcher("index.jsp").forward(request, response);
	        
			doGet(request, response);
			
		} catch (Exception e) {
			// TODO: handle exception
			response.sendRedirect("error.jsp");
		}
	}

}
