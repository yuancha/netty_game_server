package session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineManager {
    private static Logger logger = LoggerFactory.getLogger(OnlineManager.class);
    private static final OnlineManager instance = new OnlineManager();
    public static OnlineManager getInstance() {
        return instance;
    }

    private Map<Integer, GameIOSession> sessMap = new ConcurrentHashMap<>();

    public Map<Integer, GameIOSession> getSessMap() {
        return sessMap;
    }

    public void setSessMap(Map<Integer, GameIOSession> sessMap) {
        this.sessMap = sessMap;
    }
}
