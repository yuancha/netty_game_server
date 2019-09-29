package router;

import com.google.protobuf.MessageLite;
import util.ProtoScanUtil;

import java.util.HashMap;
import java.util.Map;

public class MessageMapper {
    private Map<Integer, MessageLite> typeMsgMap = new HashMap<>();
    private Map<Class, Integer> msgTypeMap = new HashMap<>();

    public MessageMapper(String packPath) {
        initMsg(packPath);
    }

    public void initMsg(String packPath) {
        typeMsgMap = ProtoScanUtil.scan(packPath);
        for (Map.Entry<Integer, MessageLite> entry : typeMsgMap.entrySet()) {
            msgTypeMap.put(entry.getValue().getClass(), entry.getKey());
        }
    }

    public MessageLite getMsgByType(int msgType) {
        return typeMsgMap.get(msgType);
    }

    public int getTypeByMsg(MessageLite msg) {
        return msgTypeMap.get(msg.getClass());
    }
}
