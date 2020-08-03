package com.nearsoft.javaschool.url;

import com.nearsoft.javaschool.util.URLShortener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private URLShortener urlShortener;
    
    @Value("${api.url.rules.mapping}")
    private String[] urlRulesList;

    @Value("${api.url.normal.prefix}")
    private String urlNormalPrefix;

    @Value("${api.url.secure.prefix}")
    private String urlSecurePrefix;


    public String getUrl(String alias){
        String longUrl = "/error";
        Optional<Url> getUrl = urlRepository.findByAlias(alias);
        if(getUrl.isPresent()){
            String url = getUrl.get().getUrl();
            if (!url.contains(urlNormalPrefix) || !url.contains(urlSecurePrefix)){
                url = urlNormalPrefix + url;
            }
            longUrl = url;
        }
        return longUrl;
    }

    public Url addUrl(Url url) {
        if(url != null && url.getUrl() != null && !url.getUrl().trim().isEmpty()){
            String sUrl = url.getUrl();
            Url urlToSave = this.getUrlToSave(sUrl);
            Optional<Url> getUrl = urlRepository.findByUrl(sUrl);
            if(getUrl.isPresent()){
                return getUrl.get();
            } else {
                return urlRepository.save(urlToSave);
            }
        }else {
            return null;
        }
    }

    private Url getUrlToSave(String sUrl){
        String alias = null;
        boolean defaultShortUrl = true;
        Map<String, String> urlRulesMap = this.ListToMap();
        for (Map.Entry<String,String> urlRule: urlRulesMap.entrySet()) {
            if(sUrl.contains(urlRule.getKey())){
                alias = urlShortener.getShortUrlWithLength(Integer.parseInt(urlRule.getValue()));
                defaultShortUrl = false;
            }
        }
        if(defaultShortUrl){
            alias = urlShortener.getShortUrlDefault(sUrl);
        }
        return new Url(sUrl,alias);
    }

    private Map<String, String> ListToMap(){
        Map<String, String> urlRulesMap = Stream.iterate(
                Arrays.asList(urlRulesList), l -> l.subList(2, l.size()))
                .limit(urlRulesList.length / 2)
                .collect(Collectors.toMap(
                        l -> l.get(0).replaceFirst("-+", ""),
                        l -> l.get(1))
                );
        return urlRulesMap;
    }
}
