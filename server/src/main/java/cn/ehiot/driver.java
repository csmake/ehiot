/*
 * 中华人民共和国 《物联网 生命体征感知设备数据接口规范》GB 草案，java服务器端接口示例代码
 * 北京赛思美科技术有限公司 保留所有权利。
 * 
 */
package cn.ehiot;

import cn.stdiot.JSONCall;
import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Singleton;
import org.bson.Document;

/**
 * 驱动函数
 *
 * @author stdiot.cn
 */
@Singleton
public class driver {

    ///会话记录表
    protected HashMap<String, Document> sessions = new java.util.HashMap<>();
    
    ///使用单实例模式
    protected static driver singleton;

    /**
     * 返回单个实例
     *
     * @return
     */
    public static driver Singleton() {
        try {
            if (singleton == null) {
                singleton = new driver();
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return singleton;
    }

    /**
     *构造函数
     */
    public driver() {
    }

    /**
     * 获取对某各设备访问的令牌，也可以使用OAuth2.0协议，由第三方权限服务器提供令牌。
     * @param context json调用上下文
     * @param option  参数选项
     * @return 
     * @throws Exception
     */
    public Document token(JSONCall context, Document option)throws Exception {
        return option.append("token", String.valueOf(System.currentTimeMillis())).append("refresh_token", java.util.UUID.randomUUID().toString()).append("expire_in", 7200).append("expire", System.currentTimeMillis() + 7200 * 1000);
    }

    /**
     * 打开会话
     * @param context json调用上下文
     * @param option  参数选项
     */
    public Document open(JSONCall context, Document option) throws Exception{
        //删除过期的会话
        String[] keys = (String[]) sessions.keySet().toArray(new String[0]);
        for (String key : keys) {
            Document doc = sessions.get(key);
            if (doc.getLong("expire") < System.currentTimeMillis()) {
                sessions.remove(key);
            }
        }
        //构造新的会话信息，并加入会话表
        String id = java.util.UUID.randomUUID().toString();
        Document cfg = new Document();
        if (option != null) {
            cfg.putAll(option);
        }
        cfg.append("version", "1.0");
        cfg.append("callbackurl", "");
        cfg.append("sid", id);//sid 是必须的。其它的信息厂商自己定义。
        cfg.append("expire_in", 7200).append("expire", System.currentTimeMillis() + 7200 * 1000);
        sessions.put(id, cfg);
        return cfg;
    }

    /**
     * 关闭会话
     *
     * @param context json调用上下文
     * @param option  参数选项
     * @return
     */
    public Document close(JSONCall context, Document option)throws Exception {
        String session = context.sid;
        if (sessions.containsKey(session)) {
            sessions.remove(session);
        } else {
            return new Document("errcode", "404").append("errmsg", "会话ID无效");
        }
        return new Document();
    }

    /**
     * 关闭系统
     *
     * @param context json调用上下文
     * @param option  参数选项
     * @return
     */
    public Document shutdown(JSONCall context, Document option) throws Exception {
        throw new cn.stdiot.InvokeException("400", "该功能暂不支持。");
    }

    /**
     * 重启系统
     *
     * @param context json调用上下文
     * @param option  参数选项
     * @return
     */
    public Document restart(JSONCall context, Document option) throws Exception {
        throw new cn.stdiot.InvokeException("400", "该功能暂不支持。");
    }

    /**
     * 重置系统，让所有配置恢复到出厂设置
     *
     * @param context json调用上下文
     * @param option  参数选项
     * @return
     */
    public Document reset(JSONCall context, Document option) throws Exception {
        throw new cn.stdiot.InvokeException("400", "该功能暂不支持。");
    }

    /**
     * 获取多个属性
     *
     * @param context 上下文
     * @param property 属性名称数组
     * @return {'属性名称':属性值}
     */
    public Document get(JSONCall context, String[] property) throws Exception {
        String session = context.sid;
        Document doc = sessions.get(session);
        StringBuilder sb = new StringBuilder();
        Document props = new Document();
        if (doc != null) {
            for (int i = 0; i < property.length; i++) {
                String key = property[i];
                if(key.isEmpty())continue;
                if ("systime".equals(key)) {//必须支持
                    props.append(key, System.currentTimeMillis());
                }else if ("value".equals(key)) {//必须支持
                    props.append(key, (int)(Math.random()*100));
                } else {
                    Object data = doc.get(key);
                    if (data != null) {
                        props.append(key, data);
                    } else {
                        Class<?> clazz = this.getClass();
                        try {
                            Field field = clazz.getDeclaredField(key);
                            if (field != null) {
                                Object value = field.get(this);
                                props.put(key, value);
                            }
                        } catch (Exception e) {
                            sb.append(String.format("%s 属性不存在", key));
                        }
                    }
                }
            }
        } else {
            return errorSession();
        }
        if (sb.length() > 0) {
            props.append("errCode", 404).append("errMessage", String.format("%s", sb.toString()));
        }
        return props;
    }

    /**
     * 设置属性
     *
     * @param context json调用上下文
     * @param {'属性名称':属性值}
     * @return
     */
    public Document set(JSONCall context, Document property) throws Exception {
        String session = context.sid;
        Document doc = sessions.get(session);
        StringBuilder sb = new StringBuilder();
        if (doc != null) {
            Set<String> keys = property.keySet();
            for (String key : keys) {
                Object value = property.get(key);
                if (doc.containsKey(key)) {
                    doc.replace(key, value);
                } else {
                    Field field = this.getClass().getDeclaredField(key);
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(this, value);
                    } else {
                        sb.append(String.format("%s 属性不存在", key));
                        continue;
                    }
                }
            }
            //发送change事件
            Document v = new Document("evt", "change").append("target", new Document("guid", doc.get("guid"))).append("args", property).append("sid", session).append("tid", tid--).append("time", System.currentTimeMillis());
            trigger(context, v);
        } else {
            return errorSession();
        }
        if (sb.length() > 0) {
            return new Document("errcode", "400").append("errmsg", sb.toString());
        }
        return new Document();
    }

    /**
     *触发事件消息
     * @param context json调用上下文
     * @param list 事件列表
     * @return
     * @throws Exception
     */
    public List<Document> trigger(JSONCall context, Document[] list) throws Exception{
        ArrayList<Document> rets = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            rets.add(trigger(context, list[i]));
        }
        return rets;
    }

    protected Document trigger(JSONCall context, Document evt) throws Exception{
        String session = context.sid;
        Set<String> keys = sessions.keySet();
        String msg = JSON.toJSONString(evt);
        for (String key : keys) {
            Document doc = sessions.get(key);
            if (doc != null) {
                String url = doc.getString("callbackurl");
                if (url != null && url.length() > 0) {
                    try {
                        String text = post(url, msg);
                        cn.stdiot.JSONReturn ret = (cn.stdiot.JSONReturn) JSON.parseObject(text, cn.stdiot.JSONReturn.class);
                        if (!"200".equals(ret.errcode)) {
                            throw new Exception(ret.errmsg);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(driver.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {//不存在回调链接，存放起来
                    Document event = (Document) doc.get("evt", Document.class);
                    if (event == null) {
                        event = new Document();
                        doc.append("evt", event);
                    }
                    String type = evt.getString("evt");
                    List<Document> list = event.get(type, List.class);
                    if (list == null) {
                        list = new ArrayList<Document>();
                        event.append(type, list);
                    }
                    while (list.size() > 10) {//删除多余的
                        list.remove(0);
                    }
                    list.add(evt);
                }
            }
        } 
        return new Document();
    }

    /**
     * 获取帮助信息
     *
     * @param context json调用上下文
     * @param name 关键字，属性名，方法名,数组
     * @return
     */
    public List<Document> help(JSONCall context, String[] name) throws Exception{
        String session = context.sid;
        Document doc = sessions.get(session);
        ArrayList<Document> list = new ArrayList();
        if (doc != null) {
            for (int i = 0; i < name.length; i++) {
                String k = name[i];
                if (doc.containsKey(k)) {
                    Object data = doc.get(k);
                    if (data != null) {
                        list.add(new Document("name", k)
                                .append("type", data.getClass().getName().replace("java.lang.", ""))
                                .append("default", data.toString()).append("url", "http://www.stdiot.cn/help").append("example", "示例如下:")
                                .append("summary", String.format("%s 属性", k)));
                    }
                }
            }
        } else {
            list.add(errorSession());
        }
        return list;
    }

    /**
     * 获取事件信息
     *
     * @param context json调用上下文
     * @param type 选项
     * @return[{evt:'事件类型',...}]
     */
    public List<Document> getEvent(JSONCall context, String[] type)throws Exception {
        String session = context.sid;
        ArrayList<Document> all = new ArrayList<Document>();
        Document doc = sessions.get(session);
        if (doc != null) {
            Document evt = doc.get("evt", Document.class);
            if (evt != null) {
                if (type.length == 0) {
                    type = evt.keySet().toArray(new String[0]);
                }
                for (String t : type) {
                    List<Document> list = evt.get(t, List.class);
                    if (list != null) {
                        evt.remove(t);
                        all.addAll(list);
                    }
                }
            }
        } else {
            all.add(errorSession());
        }
        return all;
    }

    protected Document errorSession() {
        return new Document("errcode", "400").append("errmsg", "无效的会话");
    }
    protected int tid = Integer.MAX_VALUE;

    protected Document buildEvent(String sid, String evt, Document args, Document target) {
        if (tid <= 0) {
            tid = Integer.MAX_VALUE;
        }
        return new Document("sid", sid).append("evt", evt).append("args", args).append("target", target).append("time", System.currentTimeMillis()).append("tid", tid--);
    }
 
    private String post(String callbackurl, String text) throws MalformedURLException, IOException {
        java.net.URL url = new java.net.URL(callbackurl);
        java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) url.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setUseCaches(false);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Charset", "UTF-8");
        httpConn.connect();
        try (java.io.DataOutputStream dos = new java.io.DataOutputStream(httpConn.getOutputStream())) {
            dos.write(text.getBytes("utf8"));
            dos.flush();
        }
        int resultCode = httpConn.getResponseCode();
        if (java.net.HttpURLConnection.HTTP_OK == resultCode) {
            StringBuilder sb = new StringBuilder();
            String readLine;
            try (java.io.BufferedReader responseReader = new java.io.BufferedReader(new java.io.InputStreamReader(httpConn.getInputStream(), "UTF-8"))) {
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
            }
            return sb.toString();
        }
        return "";
    }
}
