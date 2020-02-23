package com.alfredposclient.xmpp;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.alfredbase.BaseApplication;
import com.alfredbase.javabean.model.PushMessage;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.NetUtil;
import com.alfredposclient.global.App;
import com.alfredposclient.push.PushListenerClient;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.json.JSONObject;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class XMPP implements ConnectionListener, PingFailedListener{
    private String user;
    private String pass;
    private String username;
    private String roomName;
    private static PushListener pushListener;
    private boolean canCheckAppOrder;

//    public static String HOST1 = "192.168.0.233";
    public static String HOST = "www.servedbyalfred.org";

    public static final int PORT = 5222;
    private static XMPP instance;
    private XMPPTCPConnection connection;
    private PingManager pingManager;
    private static String TAG = "XMPP";
    private Timer timer = new Timer();
    private int reloginTime = 10000;
    private int noNetWorkTime = 30000;
    private boolean canRunTask = true;
    private XMPPTCPConnectionConfiguration buildConfiguration() throws XmppStringprepException {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setHost(HOST);
        builder.setPort(PORT);
//        builder.setCompressionEnabled(false);
        builder.setDebuggerEnabled(true);
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        builder.setSendPresence(true);
        if (Build.VERSION.SDK_INT >= 14) {
            builder.setKeystoreType("AndroidCAStore");
            // config.setTruststorePassword(null);
            builder.setKeystorePath(null);
        } else {
            builder.setKeystoreType("BKS");
            String str = System.getProperty("javax.net.ssl.trustStore");
            if (str == null) {
                str = System.getProperty("java.home") + File.separator + "etc" + File.separator + "security"
                        + File.separator + "cacerts.bks";
            }
            builder.setKeystorePath(str);
        }
        DomainBareJid serviceName = JidCreate.domainBareFrom(HOST);
        builder.setXmppDomain(serviceName);


        return builder.build();
    }

//    private void bulidConfig(){
//        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
////                .setServiceName("machine")
////                .setUsernameAndPassword("admin", "admin")
//                .setCompressionEnabled(false)
//                .setHost(HOST)
//                .setPort(PORT)
//                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
//                .setSendPresence(true)
//                .build();
//    }

    private XMPPTCPConnection connect() throws XMPPException, SmackException, IOException, InterruptedException {
        Log.i(TAG, "Getting XMPP Connect");
        if ((this.connection != null) && (this.connection.isConnected())) {
            Log.i(TAG, "Returning already existing connection");
//            return this.connection;
            this.connection.disconnect();
        }
        Log.i(TAG, "Connection not found, creating new one");
        long l = System.currentTimeMillis();
        XMPPTCPConnectionConfiguration config = buildConfiguration();
        SmackConfiguration.DEBUG = true;

        if (connection == null) {
            this.connection = new XMPPTCPConnection(config);
            this.connection.addConnectionListener(this);
        }
        this.connection.connect();

//        Roster roster = Roster.getInstanceFor(connection);

//        if (!roster.isLoaded())
//            roster.reloadAndWait();
        Log.i(TAG, "Connection Properties: " + connection.getHost() + " " + connection.getServiceName());
        Log.i(TAG, "Time taken in first time connect: " + (System.currentTimeMillis() - l));
        Roster roster = Roster.getInstanceFor(connection);

        roster.addRosterListener(new RosterListener() {
            @Override
            public void entriesAdded(Collection<Jid> addresses) {
                Log.d("deb", "ug");
            }

            @Override
            public void entriesUpdated(Collection<Jid> addresses) {
                Log.d("deb", "ug");
            }

            @Override
            public void entriesDeleted(Collection<Jid> addresses) {
                Log.d("deb", "ug");
            }

            @Override
            public void presenceChanged(Presence presence) {
                Log.d("deb", "ug");
            }
        });
        return this.connection;
    }

    public static XMPP getInstance() {
        if (instance == null) {
            instance = new XMPP();
            if (BaseApplication.isOpenLog){
                HOST = "www.servedbyalfred.cn";  // 测试环境
            }else {
                if(BaseApplication.isZeeposDev){
                    HOST = "www.servedbyalfred.org";  // 正式环境
                }else if(BaseApplication.isCuscapiMYDev){
                    HOST = "www.servedbyalfred.org";  // 正式环境
                }
                else
                {
                    HOST = "www.servedbyalfred.org";  // 正式环境
                }
            }
            pushListener = new PushListenerClient(App.instance);
        }
        return instance;
    }

    public void close() {
        Log.i(TAG, "inside XMPP close method");
        try{
            if(timer != null){
                timer.cancel();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (this.connection != null) {
            this.connection.disconnect();
        }
        instance = null;
    }

//    public XMPPTCPConnection getConnection(Context context) {
//        Log.i(TAG, "Inside getConnection");
//        if ((this.connection == null) || (!this.connection.isConnected())) {
//            try {
//                this.connection = connect();
//                this.connection.login(AppSettings.getUser(context),
//                        AppSettings.getPassword(context));
//                Log.i(TAG, "inside XMPP getConnection method after login");
//            } catch (XMPPException localXMPPException) {
//            } catch (SmackException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        Log.i(TAG, "Inside getConnection - Returning connection");
//        return connection;
//    }

//    public Roster getRoster() throws XMPPException {
//        XMPPTCPConnection connection = null;
//        Roster roster = null;
//        try {
//            connection = connect();
//            roster = Roster.getInstanceFor(connection);
//        } catch (SmackException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return roster;
//    }


    public boolean isConnected() {
        return (this.connection != null) && (this.connection.isConnected());
    }
    public void login(final String user, final String pass, final String username, final String roomName){


//        if(App.isOpenLog){
//            this.user = "TEST" + user;
//            this.pass = "TEST" + pass;
//            this.username = "TEST" + username;
//            this.roomName = "TEST" + roomName;
//        }else {
            this.user = user;
            this.pass = pass;
            this.username = username;
            this.roomName = roomName;
//        }
        try {
            Log.i(TAG, "inside XMPP getlogin Method");
            long l = System.currentTimeMillis();
            XMPPTCPConnection connect = connect();
            if (connect.isAuthenticated()) {
                Log.i(TAG, "User already logged in");
                return;
            }
            register(connect);

            Log.i(TAG, "Time taken to connect: " + (System.currentTimeMillis() - l));

            l = System.currentTimeMillis();
            connect.login(this.user, this.pass);
            Log.i(TAG, "Time taken to login: " + (System.currentTimeMillis() - l));

            Log.i(TAG, "login step passed");

            Presence p = new Presence(Presence.Type.available);
            p.setMode(Presence.Mode.available);
            p.setPriority(24);
            p.setFrom(connect.getUser());
            p.setStatus(new StatusItem().toJSON());
//        p.setTo("");
            VCard ownVCard = new VCard();
            ownVCard.load(connect, null);
            ownVCard.setNickName(username);
            ownVCard.save(connect);

            pingManager = PingManager.getInstanceFor(connect);
            pingManager.setPingInterval(30);
            pingManager.registerPingFailedListener(this);
            connect.sendStanza(p);

            connect.addAsyncStanzaListener(new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
                    if ((packet instanceof Message)) {
                        String body  = ((Message) packet).getBody();
                        if(!TextUtils.isEmpty(body)) {
                            LogUtil.i(TAG, body);
                            {
                                try {
                                    JSONObject jsonObject = new JSONObject(body);
                                    PushMessage pushMessage = new PushMessage();
                                    if(jsonObject.has("push")){
                                        pushMessage.setMsg(jsonObject.getString("push"));
                                    }
                                    if(jsonObject.has("content")){
                                        pushMessage.setContent(jsonObject.getString("content"));
                                    }
                                    if(jsonObject.has("restId")){
                                        pushMessage.setRestId(jsonObject.getInt("restId"));
                                    }
                                    if(jsonObject.has("sendTime")){
                                        pushMessage.setSendTime(jsonObject.getLong("sendTime"));
                                    }
                                    if(jsonObject.has("revenueId")){
                                        pushMessage.setRevenueId(jsonObject.getInt("revenueId"));
                                    }
                                    if(jsonObject.has("businessStr")){
                                        pushMessage.setBusinessStr(jsonObject.getString("businessStr"));
                                    }

                                    if(!TextUtils.isEmpty(pushMessage.getMsg())){
                                        if (pushListener != null)
                                            pushListener.onPushMessageReceived(pushMessage, canCheckAppOrder);
                                        else
                                            LogUtil.d(TAG, "回调是空的");
                                    }
                                } catch (Exception e) {
                                    LogUtil.d(TAG, "设置监听出错");
                                    if (App.isOpenLog) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }

//                @Override
//                public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
//                    if ((packet instanceof Message)) {
//                        String body  = ((Message) packet).getBody();
//                        if(!TextUtils.isEmpty(body)) {
//                            LogUtil.i(TAG, body);
//                            {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(body);
//                                    PushMessage pushMessage = new PushMessage();
//                                    if(jsonObject.has("push")){
//                                        pushMessage.setMsg(jsonObject.getString("push"));
//                                    }
//                                    if(jsonObject.has("content")){
//                                        pushMessage.setContent(jsonObject.getString("content"));
//                                    }
//                                    if(jsonObject.has("restId")){
//                                        pushMessage.setRestId(jsonObject.getInt("restId"));
//                                    }
//                                    if(jsonObject.has("revenueId")){
//                                        pushMessage.setRevenueId(jsonObject.getInt("revenueId"));
//                                    }
//                                    if(jsonObject.has("businessStr")){
//                                        pushMessage.setBusinessStr(jsonObject.getString("businessStr"));
//                                    }
//                                    if(!TextUtils.isEmpty(pushMessage.getMsg())){
//                                        if (pushListener != null)
//                                            pushListener.onPushMessageReceived(pushMessage, canCheckAppOrder);
//                                        else
//                                            LogUtil.d(TAG, "回调是空的");
//                                    }
//                                } catch (Exception e) {
//                                    LogUtil.d(TAG, "设置监听出错");
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                }
            }, null);
            createMessageGroup(connection, roomName, username);
        } catch (Exception e){
            e.printStackTrace();
            LogUtil.e(TAG, "出错重连.....");
            if(canRunTask) {
                timer.schedule(new MyTimertask(), reloginTime);
                canRunTask = false;
            }
        }

    }

    private void reLogin(){
        login(user, pass, username, roomName);
    }

    public void register(XMPPTCPConnection connection) throws XMPPException, SmackException.NoResponseException, SmackException.NotConnectedException {
        Log.i(TAG, "inside XMPP register method, " + user + " : " + pass);
        long l = System.currentTimeMillis();
        try {
            AccountManager accountManager = AccountManager.getInstance(connection);
            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.createAccount(Localpart.from(user), pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Time taken to register: " + (System.currentTimeMillis() - l));
    }

    public void createMessageGroup(XMPPTCPConnection connection, String roomName, String userName) throws XmppStringprepException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, InterruptedException, SmackException.NoResponseException, MultiUserChatException.NotAMucServiceException, MultiUserChatException.MissingMucCreationAcknowledgeException, MultiUserChatException.MucAlreadyJoinedException {
        MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(connection);
        String name = roomName;
//        if(App.isOpenLog){
//            name = "Test" + name;
//        }
        EntityBareJid room = JidCreate.entityBareFrom(name + "@alfred." + HOST);
        RoomInfo roomInfo = null;
        try{
            roomInfo = multiUserChatManager.getRoomInfo(room);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(roomInfo == null){
            MultiUserChat multiUserChat = multiUserChatManager.getMultiUserChat(room);
            multiUserChat.create(Resourcepart.from(userName));
            Form form = multiUserChat.getConfigurationForm().createAnswerForm();
            form.setAnswer("muc#roomconfig_publicroom", true);
            form.setAnswer("muc#roomconfig_roomname", roomName); //                        form.setAnswer("muc#roomconfig_roomowners", userDate.getUserId().toString() + "@" + Config.ChatValues.SERVICE_NAME);
            form.setAnswer("muc#roomconfig_persistentroom", true);
            List<String> cast_values = new ArrayList<String>();
            cast_values.add("moderator");
            cast_values.add("participant");
            cast_values.add("visitor");
            form.setAnswer("muc#roomconfig_presencebroadcast", cast_values);
            multiUserChat.sendConfigurationForm(form);
            multiUserChat.join(Resourcepart.from(roomName));
        }else{
            MultiUserChat multiUserChat = multiUserChatManager.getMultiUserChat(room);
            multiUserChat.join(Resourcepart.from(roomName));
        }
    }

    public void disConnect(){
        if(this.connection != null && this.connection.isConnected()){
            this.connection.disconnect();
            this.connection.removeConnectionListener(this);
            this.connection = null;
        }
        if(this.pingManager != null){
            this.pingManager.unregisterPingFailedListener(this);
        }
    }

    @Override
    public void connected(XMPPConnection connection) {
            LogUtil.log("XMPP Connected");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {

    }

    @Override
    public void connectionClosed() {
        LogUtil.e(TAG, "connectionClosed重连.....");
        if(canRunTask) {
            timer.schedule(new MyTimertask(), reloginTime);
            canRunTask = false;
        }
        LogUtil.i(TAG, "connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        LogUtil.i(TAG, "connectionClosedOnError" + e.getMessage());
        LogUtil.e(TAG, "connectionClosedOnError重连.....");
        if(canRunTask) {
            timer.schedule(new MyTimertask(), reloginTime);
            canRunTask = false;
        }
    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }

    @Override
    public void pingFailed() {
        reLogin();
    }

    public void setPushListener(PushListener pushListener) {
        this.pushListener = pushListener;
    }

    public interface PushListener {
        public void onPushMessageReceived(PushMessage msg, boolean canCheckAppOrder);

    }

    public void setCanCheckAppOrder(boolean canCheckAppOrder) {
        this.canCheckAppOrder = canCheckAppOrder;
    }

    public void onNetWorkConnect(){
        try{
            if(timer != null){
                timer.cancel();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        if (this.connection != null) {
//            this.connection.disconnect();
//        }
        timer = new Timer();
        timer.schedule(new MyTimertask(),1000);
    }


    class MyTimertask extends TimerTask {

        @Override
        public void run() {
            canRunTask = true;
            LogUtil.e(TAG, ".....正在尝试连接");
            if (NetUtil.isNetworkAvailable(App.instance)) {
                LogUtil.e(TAG, ".....正在连接中");
                noNetWorkTime = 30*1000;
                reLogin();
            }else{
                LogUtil.e(TAG, "xmpp  当前没有网络,等待30s,1m,2m,3m,4m,5m,10m.最少10分钟一次,减少次数");
                if(canRunTask) {
                    timer.schedule(new MyTimertask(), noNetWorkTime);
                    if(noNetWorkTime == 30*1000){
                        noNetWorkTime += noNetWorkTime;
                    }else if(noNetWorkTime < 5*60*1000){
                        noNetWorkTime += 60*1000;
                    }else{
                        noNetWorkTime = 10*60*1000;
                    }
                    canRunTask = false;
                }
            }

        }

    }
}