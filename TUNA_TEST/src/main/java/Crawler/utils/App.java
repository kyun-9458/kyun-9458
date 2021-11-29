package Crawler.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import groovy.transform.PropertyOptions;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Crawler.utils.Crawler;

public class App {
    public static void main(String[] args) {
        try {
            //run("https://heodolf.tistory.com/18");
            run("https://gooddata.go.kr/qtyeval/USR/V_EVL_002.do?DBSEQNO=5761&EMLSEQ=343&AGENCYCD=1730000");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Crawler 실행
    public static void run(String URL) throws Exception {

        // 1. Crawler 옵션 설정
        Properties options = new Properties();
        options.put("Content-Type", "application/html;charset=UTF-8");
        //options.put("downloads", Const.DEFAULT_DOWNLOADS);
        options.put("downloads", "C:\\Users\\kimsm\\Desktop\\TUNA\\collect_dir");
        options.put("timeout", 30*1000);

        // 2. Crawler 생성
        Crawler crawler = new Crawler(URL, options);

        // 3. HTML 파싱
        Document html = crawler.get();

        // 4. <a> 태그 추출.
        Elements files = html.select(".fileblock a");

        // 5. File Download 정보 추출
        List<Map<String, String>> download_list = new ArrayList<Map<String, String>>();
        Map<String, String> download_info = null;
        for(Element file : files) {
            download_info = new HashMap<String, String>();

            String href = file.attr("href");
            String filename = file.text();

            if(filename != null) {
                filename = filename.split(" ")[0];
            }

            download_info.put("filename", filename);
            download_info.put("URL", href);

            download_list.add(download_info);

            // Download File
            // crawler.download( href, filename);
        }

        // 6. Download Files
        crawler.downloads(download_list);
    }
}
