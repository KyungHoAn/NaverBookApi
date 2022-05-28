package naverBook;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


/** * 네이버 크롤러 메인 클래스 * * @author steel * @since 2021.02.24 */
public class NaverCrawlerMain {
	public static void main(String[] args) {
		String id = "GvFYZuoGJBi5xAs0T4__";
		String secret = "U2EMufcr3t";
		
		try {
			NaverCrawler crawler = new NaverCrawler();
			String url = URLEncoder.encode("9788994492032", "UTF-8");
			String response = crawler.search(id, secret, url);
			
			String[] fields = {"title","link","description","image","author","price","isbn"};
			Map<String, Object> result = crawler.getResult(response, fields);
			
			if(result.size() >0)
				System.out.println("total->"+result.get("total"));
			
			List<Map<String,Object>> items = (List<Map<String, Object>>) result.get("result");
			
			for(Map<String,Object> item : items) {
				System.out.println("===============================");
				for(String field : fields)
					System.out.println(field+"->"+item.get(field));
			}			
//			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
