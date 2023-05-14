import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Http2MultiProxyExample {
    public static void main(String[] args) throws IOException {
        // Đường dẫn tới file chứa danh sách proxy
        String proxyFilePath = "proxies.txt";

        // Đọc danh sách proxy từ file
        List<String> proxyList = Files.readAllLines(Paths.get(proxyFilePath));

        // Tạo danh sách các OkHttpClient với cấu hình proxy tương ứng
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        for (String proxyStr : proxyList) {
            String[] proxyParts = proxyStr.split(":");
            String proxyHost = proxyParts[0];
            int proxyPort = Integer.parseInt(proxyParts[1]);

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            clientBuilder.proxy(proxy);
        }

        OkHttpClient client = clientBuilder.build();

        // URL của trang web muốn truy cập
        String url = "https://muasource.vn/";

        // Tạo request để truy cập URL
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Thực hiện request và xử lý response
        for (String proxyStr : proxyList) {
            System.out.println("Proxy: " + proxyStr);
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("Response: " + response.body().string());
                } else {
                    System.out.println("Request failed: " + response.code() + " - " + response.message());
                }
            } catch (IOException e) {
                System.out.println("Request failed: " + e.getMessage());
            }
            System.out.println();
        }
    }
}
