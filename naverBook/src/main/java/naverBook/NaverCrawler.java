package naverBook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;




/** * ���̹� ���α� ũ�Ѹ� * * @author steel * @since 2021.02.24 */
public class NaverCrawler { // ���̽� URL
//	final String baseUrl = "https://openapi.naver.com/v1/search/blog.json?query=";
//	final String baseUrl = "https://openapi.naver.com/v1/search/book_adv.xml?query=";
	final String baseUrl = "https://openapi.naver.com/v1/search/book.json?query=";
	
	
	public String search(String clientId, String secret, String _url) {
		HttpURLConnection con = null;
		String result = "";
		try {
			URL url = new URL(baseUrl + _url);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", secret);
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK)
				result = readBody(con.getInputStream());
			else
				result = readBody(con.getErrorStream());
		} catch (Exception e) {
			System.out.println("���� ���� : " + e);
		} finally {
			con.disconnect();
		}
		return result;
	}

	/** * ����� �д´� * * @param body * @return */
	public String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);
		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();
			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}
			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API ������ �дµ� �����߽��ϴ�.", e);
		}
	}
	
	
	public Map<String, Object> getResult(String response, String[] fields){
		Map<String, Object> rtnObj = new HashMap<> ();
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject result = (JSONObject) parser.parse(response);
			
			rtnObj.put("total", (long)result.get("total"));
			
			JSONArray items = (JSONArray) result.get("items");
			
			List<Map<String,Object>> itemList = new ArrayList();
			
			for(int i=0; i<items.size(); i++) {
				JSONObject item = (JSONObject) items.get(i);
				
				Map<String,Object> itemMap = new HashMap<> ();
				
				for(String field:fields) {
					itemMap.put(field, item.get(field));
				}
				itemList.add(itemMap);
			}
			rtnObj.put("result", itemList);
		}catch(Exception e) {
			System.out.println("getResult error -> "+"�Ľ� ����, "+e.getMessage());
		}
		return rtnObj;
	}
}