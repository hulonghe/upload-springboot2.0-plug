package com.hlh.util.rep;

import com.alibaba.fastjson.JSON;
import com.hlh.config.FinalPoolCfg;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求响应处理
 */

public class RepUtil {

    private static Map<Integer, String> msg = RepCode.msg;

    /**
     * 返回JSON格式的数据
     *
     * @param response
     * @param code
     * @throws IOException
     */
    public static void writerErrJson(HttpServletRequest request, HttpServletResponse response, int code) throws IOException {
        setCors(request, response);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        Map map = RepUtil.post(code);
        writer.write(JSON.toJSON(map).toString());
    }

    /**
     * 跨域支持
     *
     * @param response
     */
    public static void setCors(HttpServletRequest request, HttpServletResponse response) {
        // TODO 支持跨域访问
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader(
                "Access-Control-Allow-Headers", "Content-Type, " + FinalPoolCfg.HEADER_ACCESS_TOKEN_NAME);
        response.setHeader("Access-Control-Expose-Headers", "*");
    }

    /**
     * 单纯的为了生成一个空的Map
     *
     * @return
     */
    public static Map post() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    /**
     * 参数校验
     *
     * @param fieldError
     * @return
     */
    public static Map post(FieldError fieldError) {
        Map<String, Object> map = new HashMap<>();
        map.put("field", fieldError.getField());
        map.put("errMsg", fieldError.getDefaultMessage());
        return getMap(RepCode.CODE_VALIDATOR_ERR, fieldError.getDefaultMessage(), map);
    }

    /**
     * @param data 需要返回的数据
     * @Method post
     * @Author Laohu
     * @Description 封装响应数据
     * @Return java.util.Map
     * @Exception
     * @Date 2019/7/10 14:19
     * @Version 1.0
     */
    public static Map post(Object data) {
        Map<String, Object> map = new HashMap<>();
        /**
         * 未指明响应码，则默认根据响应数据是否为空，判断响应码
         */
        int code = RepCode.CODE_SUCCESS;
        if (data == null) {
            code = RepCode.CODE_NO_DATA;
        } else if (data instanceof String && ((String) data).length() == 0) {
            code = RepCode.CODE_NO_DATA;
        } else if (data instanceof List && ((List) data).size() == 0) {
            code = RepCode.CODE_NO_DATA;
        } else if (data instanceof Map && ((Map) data).keySet().size() == 0) {
            code = RepCode.CODE_NO_DATA;
        }
        return getMap(code, null, data);
    }

    /**
     * @param code 响应码
     * @param data 响应数据
     * @Method post
     * @Author Laohu
     * @Description 封装响应数据
     * @Return java.util.Map
     * @Exception
     * @Date 2019/7/10 14:25
     * @Version 1.0
     */
    public static Map post(int code, Object data) {
        return getMap(code, null, data);
    }

    /**
     * @param code 响应码
     * @Method post
     * @Author Laohu
     * @Description 封装响应数据
     * @Return java.util.Map
     * @Exception
     * @Date 2019/7/10 14:27
     * @Version 1.0
     */
    public static Map post(int code) {
        return getMap(code, null, null);
    }

    public static Map post(int code, String msg) {
        return getMap(code, msg, null);
    }

    public static Map post(int code, String msg, Object data) {
        return getMap(code, msg, data);
    }

    /**
     * 封装分页对象
     *
     * @param obj
     * @param count
     * @param <T>
     * @return
     */
    public static <T> Map post(List<T> obj, long count) {
        return RepUtil.post(obj, count, null);
    }

    /**
     * 封装分页对象, 附带图片
     *
     * @param obj
     * @param count
     * @param <T>
     * @return
     */
    public static <T> Map post(List<T> obj, long count, Object imgs) {
        int code = (null == obj || obj.size() == 0) ? RepCode.CODE_NO_DATA : RepCode.CODE_SUCCESS;
        Map<String, Object> map = new HashMap<>();
        map.put(RepCode.REP_CODE, code);
        map.put(RepCode.REP_MSG, msg.get(code));
        map.put(RepCode.REP_DATA, obj);
        map.put("count", count);

        if (imgs != null) {
            map.put("imgs", imgs);
        }

        return map;
    }

    private static Map getMap(int code, String msg0, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put(RepCode.REP_CODE, code);
        map.put(RepCode.REP_MSG, null == msg0 ? msg.get(code) : msg0);
        map.put(RepCode.REP_DATA, data);
        return map;
    }
}


