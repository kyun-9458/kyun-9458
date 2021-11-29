package Crawler.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;

import java.io.IOException;

public class Html_Parsing1 {
    public static void main(String[] args) {
        try {
            //String URL = "https://gooddata.go.kr/qtyeval/USR/V_EVL_002.do?DBSEQNO=21282&EMLSEQ=343&AGENCYCD=3510500";

            String URL  = "https://heodolf.tistory.com/18";
            Connection conn = Jsoup.connect(URL);

            Document html = conn.get();

            System.out.println(html.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
