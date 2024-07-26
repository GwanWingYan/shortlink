package com.tomgrx.shortlink.core.service.impl;

import com.tomgrx.shortlink.core.service.UrlTitleService;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * URL标题接口实现层
 */
@Service
public class UrlTitleServiceImpl implements UrlTitleService {

    @SneakyThrows
    @Override
    public String getTitleByUrl(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.title();
        } catch (IOException e) {
            System.err.println("Error fetching web page title: " + e.getMessage());
            return "获取网站标题失败";
        }
    }
}
