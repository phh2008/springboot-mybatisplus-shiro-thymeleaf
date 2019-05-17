package com.phh.utils;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by phh on 2017/8/26 0026.
 */
public class WebUtil {


    /**
     * 是否ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    /**
     * 响应json
     *
     * @param response
     */
    public static void writeJson(ServletResponse response, Object obj) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(JacksonUtils.writeAsString(obj));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 响应html
     *
     * @param response
     * @param obj
     */
    public static void writeHtml(ServletResponse response, Object obj) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();
            out.write(JacksonUtils.writeAsString(obj));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * read request.inputStream to string
     *
     * @param request
     * @return
     */
    public static String getStreamString(HttpServletRequest request) {
        InputStream stream = null;
        StringBuffer sb = new StringBuffer();
        try {
            String s;
            stream = request.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
        } catch (Exception e) {
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        }
        return sb.toString();
    }

    /**
     * write response
     *
     * @param response
     * @param content
     */
    public static void writeResponse(HttpServletResponse response, byte[] content) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(content);
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

}
