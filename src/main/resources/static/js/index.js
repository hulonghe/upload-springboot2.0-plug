window.onload = function () {
    layui.use(['jquery', 'layer'], function () {
        $ = layui.jquery;
        layer = layui.layer;
    });
}

function startVue() {
    return new Vue(vueData);
}

function alert(content, func) {
    layer.alert(content, {
        icon: 1,
        title: '系统提示'
    }, func);
}

function msg(content) {
    layer.msg(content);
}

function confirm(content, func) {
    let index = layer.confirm(content, {
        icon: 3,
        title: '系统提示'
    }, function (res) {
        func();
        layer.close(index);
    });
}

function prompt({formType = '0', value = '', title = '',} = {}, func) {
    layer.prompt({
        formType: formType,
        value: value,
        title: title,
        area: ['300px', '250px'] //自定义文本域宽高
    }, function (value, index, elem) {
        layer.close(index);
        func(value);
    });
}

function loadding(content = '加载中……') {
    var index = layer.msg(content, {
        icon: 16,
        time: false,
        shade: 0.01
    });
    return index;
}

function openUrl(url, title) {
    if (typeof (title) == 'undefined') {
        title = '其它网站'
    }
    layer.open({
        title: title,
        maxmin: true,
        type: 2,
        content: url + (url.indexOf('?') == -1 ? '?' : '&') + 'tst=' + new Date().getTime(),
        area: ['90%', '90%'],
        shadeClose: true
    });
}

function getDateFormat(fmt, date) {
    let dd;
    if (typeof (date) == 'undefined') {
        dd = dateFormat(new Date(), fmt);
    } else {
        dd = dateFormat(new Date(date), fmt);
    }
    return dd;
}

/**
 * 格式化日期
 * @param {Object} fmt
 */
function dateFormat(date, fmt) {
    var o = {
        "M+": date.getMonth() + 1, // 月份
        "d+": date.getDate(), // 日
        "h+": date.getHours(), // 小时
        "m+": date.getMinutes(), // 分
        "s+": date.getSeconds(), // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3), // 季度
        "S": date.getMilliseconds() // 毫秒
    };

    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k])
            .substr(("" + o[k]).length)));
    return fmt;
}

function reqGet(url, data, success, isLoadding = true) {
    let index;
    if (isLoadding) {
        index = loadding();
    }

    $.ajax({
        url: getReqPath(url),
        type: 'get',
        data: data,
        headers: {
            access_token: getAK()
        },
        success: function (res) {
            console.log('对->' + url + '的请求成功了:');
            console.log(res);
            success(res);
        },
        error: function (res) {
            console.log('对->' + url + '的请求失败了:');
            console.log(res);
            res = res.responseText;
            if (res) {
                res = JSON.parse(res);
                alert(res.msg ? res.msg : '服务繁忙!');
            } else {
                alert('服务繁忙!');
            }
        },
        complete: function (res) {
            reqFinish(index, url, isLoadding, res);
        }
    });
}

function reqPost(url, data, success, isLoadding = true) {
    let index;
    if (isLoadding) {
        index = loadding();
    }

    $.ajax({
        url: getReqPath(url),
        type: 'post',
        data: JSON.stringify(data),
        contentType: 'application/json',
        headers: {
            access_token: getAK()
        },
        success: function (res) {
            console.log('对->' + url + '的请求成功了:');
            console.log(res);
            success(res);
        },
        error: function (res) {
            console.log('对->' + url + '的请求失败了:');
            console.log(res);
            alert('服务繁忙!');
        },
        complete: function (res) {
            reqFinish(index, url, isLoadding, res);
        }
    });
}

function reqPut(url, data, success, isLoadding = true) {
    let index;
    if (isLoadding) {
        index = loadding();
    }

    $.ajax({
        url: getReqPath(url),
        type: 'put',
        data: JSON.stringify(data),
        contentType: 'application/json',
        headers: {
            access_token: getAK()
        },
        success: function (res) {
            console.log('对->' + url + '的请求成功了:');
            console.log(res);
            success(res);
        },
        error: function (res) {
            console.log('对->' + url + '的请求失败了:');
            console.log(res);
            alert('服务繁忙!');
        },
        complete: function (res) {
            reqFinish(index, url, isLoadding, res);
        }
    });
}

function reqDel(url, data, success, isLoadding = true) {
    let index;
    if (isLoadding) {
        index = loadding();
    }

    $.ajax({
        url: getReqPath(url),
        type: 'delete',
        data: data,
        headers: {
            access_token: getAK()
        },
        success: function (res) {
            console.log('对->' + url + '的请求成功了:');
            console.log(res);
            success(res);
        },
        error: function (res) {
            console.log('对->' + url + '的请求失败了:');
            console.log(res);
            alert('服务繁忙!');
        },
        complete: function (res) {
            reqFinish(index, url, isLoadding, res);
        }
    });
}

/**
 * 批量操作
 * @param content   确认提示文字
 * @param type      操作类型
 * @param url       请求接口
 * @param data      请求附加数据
 */
function betchOperation(content, type, url, data = {}) {
    confirm(content, (res) => {
        if (type && type == 'put') {
            reqPut(url, data, res => {
                if (res.code == 0) {
                    tableId.reload();
                    alert('已更新');
                } else {
                    alert(res.msg);
                }
            })
        } else if (type && type == 'del') {
            reqDel(url, data, res => {
                if (res.code == 0) {
                    tableId.reload();
                    alert('已删除');
                } else {
                    alert(res.msg);
                }
            })
        }
    });
}

/**
 * setCache
 * @param $
 * @param key
 * @param value
 */
function setCache(key, value) {
    window.localStorage.setItem(key, value);
}

/**
 * getCache
 * @param $
 * @param key
 * @param func
 */
function getCache(key) {
    let res = window.localStorage.getItem(key);
    return res;
}

/**
 * 尝试try一次
 * @param process
 * @param fail
 */
function tryGo(process, fail) {
    try {
        process();
    } catch (e) {
        console.log('踹出了毛病 -> ');
        console.log(e);
        fail(e);
    }
}

/**
 * 对象拷贝
 * @param from
 * @param to
 */
function copyProperties(from, to) {
    let res = to;
    tryGo(() => {
        for (let key in from) {
            res[key] = from[key];
        }
    }, (e) => {
        res = to;
    });
    return res;
}

/**
 *
 * @param data
 * @param flag
 */
function getDataFlag(data, flag = true) {
    let len = 0;
    for (let i = 0; i < data.length; i++) {
        if (data[i].flag == flag) {
            len++;
        }
    }
    return len
}

/**
 * 获取页面请求参数
 * @param param 页面url参数
 * @param name 参数名，不填写则返回JSON
 * @returns {*}
 */
function getUrlParam(param, name = undefined) {
    if (typeof (param) == 'undefined') {
        param = location.search
    }
    param = param.substr(1);

    if (typeof (name) == 'undefined' && param.length > 0) {
        let p = param.split('&');
        let res = {};
        for (let i = 0; i < p.length; i++) {
            if (p[i]) {
                let ppp = p[i].split('=');
                if (ppp.length == 2) {
                    res[ppp[0]] = decodeURI(ppp[1]);
                }
            }
        }
        return res;
    } else {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = param.match(reg);  //匹配目标参数
        if (r != null) return encodeURI(r[2]);
    }

    return null; //返回参数值
}

/**
 * 资源放行
 * @param url
 * @param excludeStr
 * @returns {boolean}
 */
function isFilterExcludeRequestArray(url, excludeStr) {
    for (let i = 0; i < excludeStr.length; i++) {
        if (isFilterExcludeRequest(url, excludeStr[i])) {
            return true;
        }
    }
    return false;
}

/**
 * 资源放行
 * @param url
 * @param ex
 * @returns {boolean}
 */
function isFilterExcludeRequest(url, ex) {
    /* 空数据校验 */
    if (!ex) {
        return false;
    }

    /* 相等则直接放行 */
    if (url == ex) {
        return true;
    }

    /* 前缀部分相等则放行 */
    let index = ex.indexOf("/**");
    if (index != -1 && url.length >= index) {
        if (url.substring(0, index) === ex.substring(0, index)) {
            return true;
        }
    }

    /* 后缀部分相等则放行 */
    index = ex.indexOf("**/");
    if (index != -1 && url.length >= (index + 3)) {
        index += 3;
        if (url.substring(index) === ex.substring(index)) {
            return true;
        }
    }

    return false;
}

/**
 * 请求资源结束
 * @param index
 * @param url
 * @param isLoadding
 * @param res
 */
function reqFinish(index, url, isLoadding, res) {
    putAK(res.getResponseHeader(HEADER_ACCESS_TOKEN_NAME), url);
    console.log('对->' + url + '的请求已结束!');
    if (isLoadding) {
        layer.close(index);
    }
}

/**
 * 更新AK信息
 * @param ak
 * @param url
 */
function putAK(ak, url) {
    /* 更新ak */
    if ((ACCESS_TOKEN_OPERATE || PAGE_LOGIN_AUTH == url || !isFilterExcludeRequestArray(url, AUTHRITY_FILTER_EXCLUDE)) && ak && url) {
        access_token = ak;
        setCache(HEADER_ACCESS_TOKEN_NAME, ak);
    }
}

/**
 * 获取AK信息
 * @returns {string}
 */
function getAK() {
    return access_token ? access_token : getCache(HEADER_ACCESS_TOKEN_NAME);
}

/**
 * 获取自愿请求完整路径
 * @param url
 * @returns {string}
 */
function getReqPath(url = '/') {
    if (!url) {
        throw new Error('请求路径错误 -> ' + url);
    }

    url = url.toString();

    if (url.indexOf('http:') === 0) {
        console.log('请求路径 -> ' + url);
        return url;
    }

    if (url.indexOf(URL_SERVER_ROOT + URL_SERVER_INTERFACE_PREFIX + URL_SERVER_INTERFACE_VERSION) > -1) {
        console.log('请求路径 -> ' + url);
        return url;
    }

    if (url.indexOf(URL_SERVER_ROOT + URL_SERVER_INTERFACE_PREFIX) > -1) {
        console.log('请求路径 -> ' + URL_SERVER_INTERFACE_VERSION + url);
        return URL_SERVER_INTERFACE_VERSION + url;
    }

    if (url.indexOf(URL_SERVER_ROOT) > -1) {
        console.log('请求路径 -> ' + URL_SERVER_INTERFACE_PREFIX + URL_SERVER_INTERFACE_VERSION + url);
        return URL_SERVER_INTERFACE_PREFIX + URL_SERVER_INTERFACE_VERSION + url;
    }

    console.log('请求路径 -> ' + URL_SERVER_ROOT + URL_SERVER_INTERFACE_PREFIX + URL_SERVER_INTERFACE_VERSION + url);
    return URL_SERVER_ROOT + URL_SERVER_INTERFACE_PREFIX + URL_SERVER_INTERFACE_VERSION + url;
}

/**
 * 页面请求，必要参数封装
 * @param url
 * @returns {string}
 */
function getTextReqPath(url = '/') {
    if (url.indexOf('?') != -1) {
        url += '&access_token=' + getAK() + '&now_time=' + (new Date().getTime());
    } else {
        url += '?access_token=' + getAK() + '&now_time=' + (new Date().getTime());
    }
    console.log('请求路径 -> ' + url);
    return url;
}