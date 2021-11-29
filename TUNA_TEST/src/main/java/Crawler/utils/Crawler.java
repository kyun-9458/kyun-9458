package Crawler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawler {
    private static ExecutorService executorService = null;
    private static Connection conn = null;
    private static Properties options = null;

    //생성자()
    public Crawler() {
        //1. Thread Pool 설정
        executorService = Executors.newFixedThreadPool(Const.DEFAULT_THREADS);
    }

    //생성자(URL)
    public Crawler(String URL) {
        // 1. Thread Pool 설정
        executorService = Executors.newFixedThreadPool(Const.DEFAULT_THREADS);

        conn = Jsoup.connect(URL);
    }

    //생성자 (URL, props)
    public Crawler(String URL, Properties options) throws Exception {
        //1. Thread Pool 설정
        executorService = Executors.newFixedThreadPool(Const.DEFAULT_THREADS);

        conn = Jsoup.connect(URL);
        setOptions(options);
    }


    // Crawler 설정 함수 : Request 옵션을 설정하는 함수
    @SuppressWarnings("unchecked")
    public void setOptions(Properties _options) throws Exception {
        options = _options;

        // Download 폴더 설정, 경로에 파일이 없으면 생성하기
        final String downloads = options.getProperty("downloads", Const.DEFAULT_DOWNLOADS);
        File file = new File(downloads);
        if(!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }

        if(conn==null)  return;

        //headers 설정
        if(_options.get("headers")!= null) {
            conn.headers((Map<String, String>)_options.get("headers"));
        }
        //Header: Content-Type 설정
        conn.header("Content-Type", (String)_options.getProperty("Content-Type", Const.DEFAULT_CONTENT_TYPE));

        //User-Agent 설정
        conn.userAgent((String)_options.getProperty("User-Agent", Const.DEFAULT_USER_AGENT));

        //Connection Timeout 설정
        int timeout = Const.DEFAULT_TIMEOUT;
        if(_options.get("timeout") != null) {
            timeout = (Integer) _options.get("timeout");
        }
        conn.timeout(timeout);
    }

    // Request 함수 -> GET method로 Request를 던지는 함수, 다형성 사용 (오버로딩)
    // GET
    public Document get() throws IOException {
        return conn.get();
    }

    // GET with DATA
    public Document get(Map<String, String> data) throws IOException {
        return conn.data(data).get();
    }

    // Post
    public Document post() throws IOException {
        return conn.post();
    }

    // Post with DATA
    public Document post(Map<String, String> data) throws IOException {
        return conn.data(data).post();
    }

    // 개별 File Download 함수 : 파일을 개별로 다운로드, Thread로
    // Download File
    public Runnable download(final String URL, final String filename) {
        // Thread(Runnable) 객체 생성
        Runnable runnable = new Runnable() {
            public void run() {
                // 파일을 저장할 경로
                String downloads = options.getProperty("downloads", Const.DEFAULT_DOWNLOADS);

                // 현재 Thread의 이름
                String threadName = Thread.currentThread().getName();

                // Jsoup Connection 객체 생성, Content-Type의 종류 무시
                Connection conn = Jsoup.connect(URL).ignoreContentType(true);

                // 저장할 File 객체 생성
                File saveFile = null;

                // File에 데이터를 작성할 Stream 객체 생성
                FileOutputStream out = null;

                try {
                    System.out.println("[" + threadName + "][START] " + URL);

                    // Request의 결과 객체
                    Response response = conn.execute();

                    // 요청한 페이지의 Content-Type
                    String contentType = response.contentType();
                    System.out.println("[" + threadName + "][Content-Type] " + contentType);

                    saveFile = new File(downloads, filename);

                    out = new FileOutputStream(saveFile);
                    out.write(response.bodyAsBytes());
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 저장된 File 확인
                    if (saveFile.exists()) {
                        System.out.println("[" + threadName + "][SAVED] " + saveFile.getPath());
                    } else {
                        System.out.println("[" + threadName + "][SAVE FAILED ]" + saveFile.getPath());
                    }
                }
            }
        };

        executorService.execute(runnable);

        // executorService.submit(runnable);
        // execute: Runnable을 인자로 받으며 반환값이 없음, void.
        // submit: Runnable과 Callable을 인자로 받으며 반환값을 받을 수 있음, return Future.

        return runnable;
    }


    // 전체 File Download 함수수
   //Download Files
    public void downloads(List<Map<String, String>> download_list) {
        for(Map<String, String> download_info : download_list) {
            String URL = download_info.get("URL");
            String filename = download_info.get("filename");

            // 1. Download Thread 실행
            download(URL, filename);
        }

        // 2. Thread Pool 종료
        executorService.shutdown(); // Task Queue에 남아있는 Thread와 실행중인 Thread 처리된 뒤 종료

        try {
            // 3. 5분 후에도 종료가 되지 않으면 강제 종료
            if( !executorService.awaitTermination(5, TimeUnit.MINUTES)) {
                executorService.shutdown();
            }
            executorService = null;
        } catch (Exception e) {
            executorService.shutdownNow();
            executorService = null;
        } finally {
            if (executorService != null) {
                executorService.shutdownNow();
            }
        }
    }

}
