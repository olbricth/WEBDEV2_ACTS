package com.M.act1.security;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenStore {
    private final Set<String> activeTokens = ConcurrentHashMap.newKeySet();

    public void addToken(String token) {
        activeTokens.add(token);
    }

    public void removeToken(String token) {
        activeTokens.remove(token);
    }

    public boolean isValid(String token) {
        return activeTokens.contains(token);
    }
}
