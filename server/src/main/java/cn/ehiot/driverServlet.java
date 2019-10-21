/**
 * Copyright (c) 2012-2019 CSMAKE, Inc. 
 *
 * Generate by csmake.com for java Mon Oct 21 20:37:46 CST 2019
 */
package cn.ehiot;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * The driverServlet interface.
 *
 * <p>Note: Generate by csmake for java .</p>
 *
 * {@link cn.ehiot.driver}
 * @author www.csmake.com
 */
@WebServlet(name = "cn.ehiot.driver", urlPatterns = {"/cn.ehiot.driver/*"})
public class driverServlet extends HttpServlet{
    public driverServlet(){
    }

    /**
    * Handles the HTTP <code>GET</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
    /**
    * Handles the HTTP <code>POST</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String url = request.getRequestURI().replace(request.getContextPath(), "");
            System.out.print(url);
            int i = url.lastIndexOf("/");
            String cmd = "";
            if (i > 1)
            {
                cmd = java.net.URLDecoder.decode(url.substring(i + 1), "UTF8");
                int dot = cmd.indexOf(".");
                if (dot != -1)
                {
                    cmd = cmd.substring(0, dot);
                }
            }
            switch (cmd)
            {
                default://json调用
                {
                    cn.stdiot.JSONCall call = JSON.parseObject(request.getInputStream(), cn.stdiot.JSONCall.class);
                    handleRequest(call, request, response);
                    break;
                }
            }
        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("{errcode:\"500\",errmsg:\"").append(ex.toString()).append("\",value:''}");
            byte[] utf8 = sb.toString().getBytes("UTF-8");
            response.setContentLength(utf8.length);
            try (javax.servlet.ServletOutputStream out = response.getOutputStream()) {
                out.write(utf8);
            }
        }
    }

    protected void handleRequest(cn.stdiot.JSONCall $, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        cn.stdiot.JSONReturn ret = call($);
        byte[] utf8 = JSON.toJSONString(ret).getBytes("UTF-8");
        response.setContentLength(utf8.length);
        try (javax.servlet.ServletOutputStream out = response.getOutputStream()) {
            out.write(utf8);
        }
    }

    private long sessionid= System.currentTimeMillis();

    protected String getSessionId(){
        return String.valueOf(sessionid++);
    }

    private cn.stdiot.JSONReturn call(cn.stdiot.JSONCall $)
    {
        String errcode = "200";
        Object retValue = null;
        String errmsg = "";
        cn.ehiot.driver proxy = cn.ehiot.driver.Singleton();
        try
        {
            switch ($.cmd){
                /** public methods */
                case "help" :{ 
                    cn.stdiot.JSONCall context = $;
                    String[] name = $.args.containsKey("name")? JSONObject.parseArray(((JSONArray)$.args.get("name")).toJSONString(), java.lang.String.class).toArray(new java.lang.String[0]):new java.lang.String[0];
                    retValue = proxy.help(context, name);
                    break;
                }
                case "shutdown" :{ 
                    cn.stdiot.JSONCall context = $;
                    org.bson.Document option = JSON.parseObject(JSON.toJSONString($.args.get("option")), org.bson.Document.class);
                    retValue = proxy.shutdown(context, option);
                    break;
                }
                case "get" :{ 
                    cn.stdiot.JSONCall context = $;
                    String[] property = $.args.containsKey("property")? JSONObject.parseArray(((JSONArray)$.args.get("property")).toJSONString(), java.lang.String.class).toArray(new java.lang.String[0]):new java.lang.String[0];
                    retValue = proxy.get(context, property);
                    break;
                }
                case "trigger" :{ 
                    cn.stdiot.JSONCall context = $;
                    org.bson.Document[] list = $.args.containsKey("list")? JSONObject.parseArray(((JSONArray)$.args.get("list")).toJSONString(), org.bson.Document.class).toArray(new org.bson.Document[0]):new org.bson.Document[0];
                    retValue = proxy.trigger(context, list);
                    break;
                }
                case "close" :{ 
                    cn.stdiot.JSONCall context = $;
                    org.bson.Document option = JSON.parseObject(JSON.toJSONString($.args.get("option")), org.bson.Document.class);
                    retValue = proxy.close(context, option);
                    break;
                }
                case "set" :{ 
                    cn.stdiot.JSONCall context = $;
                    org.bson.Document property = JSON.parseObject(JSON.toJSONString($.args.get("property")), org.bson.Document.class);
                    retValue = proxy.set(context, property);
                    break;
                }
                case "reset" :{ 
                    cn.stdiot.JSONCall context = $;
                    org.bson.Document option = JSON.parseObject(JSON.toJSONString($.args.get("option")), org.bson.Document.class);
                    retValue = proxy.reset(context, option);
                    break;
                }
                case "open" :{ 
                    cn.stdiot.JSONCall context = $;
                    org.bson.Document option = JSON.parseObject(JSON.toJSONString($.args.get("option")), org.bson.Document.class);
                    retValue = proxy.open(context, option);
                    break;
                }
                case "token" :{ 
                    cn.stdiot.JSONCall context = $;
                    org.bson.Document option = JSON.parseObject(JSON.toJSONString($.args.get("option")), org.bson.Document.class);
                    retValue = proxy.token(context, option);
                    break;
                }
                case "restart" :{ 
                    cn.stdiot.JSONCall context = $;
                    org.bson.Document option = JSON.parseObject(JSON.toJSONString($.args.get("option")), org.bson.Document.class);
                    retValue = proxy.restart(context, option);
                    break;
                }
                case "getEvent" :{ 
                    cn.stdiot.JSONCall context = $;
                    String[] type = $.args.containsKey("type")? JSONObject.parseArray(((JSONArray)$.args.get("type")).toJSONString(), java.lang.String.class).toArray(new java.lang.String[0]):new java.lang.String[0];
                    retValue = proxy.getEvent(context, type);
                    break;
                }
                default:{
                    break;
                }
            }
        }
        catch(Exception ex){
            if(ex instanceof cn.stdiot.InvokeException){
                cn.stdiot.InvokeException e = (cn.stdiot.InvokeException)ex;
                errcode=e.errcode;
                errmsg=e.getMessage();
            }else{
                errcode = "500";
                if($.cmd.startsWith("constructor")){
                    errmsg = ex.toString();
                }else{
                    errmsg = ex.toString();
                }
            }
        }
        cn.stdiot.JSONReturn ret= new cn.stdiot.JSONReturn($.tid, errcode, errmsg, retValue);
        return ret;
    }
}//end of class driver
