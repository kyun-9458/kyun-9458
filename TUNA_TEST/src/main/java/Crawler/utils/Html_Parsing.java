package Crawler.utils;

import java.io.IOException;

import groovy.transform.ToString;
import groovy.transform.builder.Builder;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
@ToString
@Builder
@Getter
 class KoreaStatus {
    private String country;
    private int diffFromPrevDay;
    private int total;
    private int death;
    private double incidence;
    private int inspection;
}
*/

public class Html_Parsing {
    public static void main(String[] args) {
        //String URL = "http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=13";

        String URL = "https://gooddata.go.kr/qtyeval/USR/V_EVL_002.do?DBSEQNO=21282&EMLSEQ=343&AGENCYCD=3510500";
        try{
            Connection conn = Jsoup.connect(URL);

//HTML 파싱
            Document html = conn.get();
//요소탐색
            //Attribute 탐색
            System.out.println("[Attribute 탐색]");
            Elements fileblocks = html.getElementsByClass("fileblock");
            for( Element fileblock : fileblocks) {
                Elements files = fileblock.getElementsByTag("a");
                for( Element elm : files) {
                    String text = elm.text();
                    String href = elm.attr("href");

                    System.out.println(text+" > "+href);
                }
            }

            //CSS Selector 탐색
            System.out.println("\n[CSS Selector 탐색");
            Elements files = html.select(".fileblock a");
            for( Element elm : files) {
                String text = elm.text();
                String href = elm.attr("href");

                System.out.println(text+" > "+href);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

