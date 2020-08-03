package com.nearsoft.javaschool.util;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class URLShortener {

    private static final String allowedString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final char[] allowedCharacters = allowedString.toCharArray();
    private final int base = allowedCharacters.length;

    public String getShortUrlWithLength(int urlLength){
        long salt = ZonedDateTime.now().toInstant().toEpochMilli()/(base*urlLength);
        Random random = new Random();
        List<Integer> reminders = new ArrayList<>();
        while (reminders.size() < urlLength){
            long counter = random.nextInt() & Integer.MAX_VALUE + salt;
            int reminder = 0;
            reminder = (int) (counter%62);
            reminders.add(reminder);
        }
        Collections.reverse(reminders);
        String aliasUrl = "";
        for (Integer base62Char: reminders) {
            aliasUrl += String.valueOf(allowedCharacters[base62Char]);
        }
        return aliasUrl;
    }

    public String getShortUrlDefault(String sUrl){
        return sUrl.replaceAll("[^a-zA-Z]|[AEIOUaeiou]","");
    }
}
