package action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.message.CSLogin1000;
import proto.message.HeartBeat1;
import proto.message.SCLogin1001;
import router.MsgTypeConstant;
import router.clazz.Module;
import router.clazz.RequestModule;
import session.GameIOSession;

@Module(100)
public class LoginAction {

    private static final Logger log = LoggerFactory.getLogger(LoginAction.class);

    @RequestModule(value = MsgTypeConstant.CS_Login, needOnline = true)
    public void login(GameIOSession session, CSLogin1000 req) {
        log.debug(req.getAccount() + "_" + req.getPassword());
        SCLogin1001.Builder res = SCLogin1001.newBuilder();
        res.setStatus(200);
        res.setMessage("登录成功");
        session.sendMsgAndFlush(res);
    }

    @RequestModule(value = MsgTypeConstant.Heart_Beat, needOnline = true)
    public void heartBeat(GameIOSession session, HeartBeat1 req) {
        System.out.println("收到心跳消息");
    }
}
