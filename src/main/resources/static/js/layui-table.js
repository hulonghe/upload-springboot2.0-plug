/**
 * @param {Object} table
 * @param {Object} data
 * layui表格渲染封装
 * @param tableEvent
 */

function onLoadTable(table, data, tableEvent) {
    if (!data.url) {
        parent.alert('加载失败!');
        return;
    }

    if (!parent.getAK()) {
        parent.location.href = '/err-msg.html?errMsg=未登录&url=/login.html&redirect=/admin/index.html';
        return;
    }

    let where = data.where ? data.where : {};

    parent.tableId = table.render({
        elem: '#test',
        url: getReqPath(data.url),
        method: 'GET',
        where: where,
        headers: {
            access_token: parent.getAK(),
        },
        parseData: function (res) { //res 即为原始返回的数据
            res = analysisPageData(res);
            return {
                "code": res.code, //解析接口状态
                "msg": res.msg, //解析提示文本
                "count": res.count, //解析数据长度
                "data": res.data //解析数据列表
            };
        },
        request: {
            pageName: 'page', //页码的参数名称，默认：page
            limitName: 'size', //每页数据量的参数名，默认：limit
        },
        response: {
            statusName: 'code', //规定数据状态的字段名称，默认：code
            statusCode: 0, //规定成功的状态码，默认：0
            msgName: 'msg', //规定状态信息的字段名称，默认：msg
            countName: 'count', //规定数据总数的字段名称，默认：count
            dataName: 'data', //规定数据列表的字段名称，默认：data
        },
        toolbar: '#toolbarDemo', //开启头部工具栏，并为其绑定左侧模板
        defaultToolbar: ['filter', 'exports', 'print'], //该参数可自由配置头部工具栏右侧的图标按钮
        title: '数据表', //定义 table 的大标题（在文件导出等地方会用到）layui 2.4.0 新增
        cols: data.cols, //设置表头。值是一个二维数组。方法渲染方式必填
        page: true, //开启分页（默认：false） 注：从 layui 2.2.0 开始，支持传入一个对象，里面可包含 laypage 组件所有支持的参数（jump、elem除外）
        limit: 15,
        limits: [15, 50, 100, 500, 1000, 10000],
        loading: true, // 是否显示加载条（默认：true）。如果设置 false，则在切换分页时，不会出现加载条。该参数只适用于 url 参数开启的方式
        // autoSort: false,
        height: data.height, //高度最大化减去差值
        done: function (res, curr, count) {
            //如果是异步请求数据方式，res即为你接口返回的信息。
            console.log('表格渲染结束 -> ' + curr + " -> " + count);
        }
    });


    if (tableEvent && tableEvent.toolbar) {
        table.on('toolbar(test)', function (obj) {
            tableEvent.toolbar(obj)
        });
    }

    if (tableEvent && tableEvent.tool) {
        table.on('tool(test)', function (obj) {
            tableEvent.tool(obj)
        });
    }
}

/**
 * @param {Object} form
 * @param {Object} laydate
 * @param {Object} dom
 * @param {Object} type
 * 初始化范围搜索日期
 */
function initFormDate(laydate, dom, type) {
    let format = '';
    if (typeof (type) == 'undefined' || type === 'date') {
        format = 'yyyy-MM-dd';
        type = 'date'
    } else if (type === 'year') {
        format = 'yyyy';
    } else if (type === 'month') {
        format = 'MM';
    } else if (type === 'time') {
        format = 'hh:mm:ss';
    }

    let value = parent.getDateFormat(format);
    parent.$('#startDate').val(value);
    parent.$('#endDate').val(value);
    setTimeout(function () {
        laydate.render({
            elem: dom,
            range: '~', // 开启左右面板范围选择
            type: type,
            value: '',
            min: '1990-01-01 00:00:00',
            max: parent.getDateFormat('yyyy-MM-dd hh:mm:ss')
        });
    }, 500);
}

/**
 * 日期选择器初始化
 * @param laydate
 * @param dom
 * @param type
 * @param res
 */
function initFormDate2(laydate, dom, type, {res = undefined} = {}) {
    let format = '';
    if (typeof (type) == 'undefined' || type === 'date') {
        format = 'yyyy-MM-dd';
        type = 'date'
    } else if (type === 'year') {
        format = 'yyyy';
    } else if (type === 'month') {
        format = 'MM';
    } else if (type === 'time') {
        format = 'hh:mm:ss';
    }

    let value = parent.getDateFormat(format);
    parent.$('#startDate').val(value);
    parent.$('#endDate').val(value);
    laydate.render({
        elem: dom,
        type: type,
        value: '',
        min: '1990-01-01 00:00:00',
        max: parent.getDateFormat('yyyy-MM-dd hh:mm:ss'),
        done: function (value) {
            if (typeof (res) != "undefined") {
                res(value);
            }
        }
    });
}

/**
 * @param form
 * @param {Object} func
 * 初始化提交
 */
function initFormSubmit(form, func) {
    setTimeout(function () {
        form.on('submit(submit)', function (data) {
            func(data.field);
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
    }, 500);
}

/**
 * @param {Object} form
 * 重新渲染表单
 */
function initFormRender(form) {
    setTimeout(function () {
        form.render(); //更新全部
    }, 500);
}

function formClose() {
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index); //再执行关闭
}

/**
 * @param {Object} upload
 * @param {Object} func
 * 图片上传
 * @param dom
 */
function initUploadImg(upload, func, dom = '#uploadImg', multiple = true, data = {}) {
    let index;
    //多图片上传
    upload.render({
        elem: dom,
        url: parent.URL_SERVER_ROOT + '/upload/',
        headers: {
            access_token: parent.access_token
        },
        data: data,
        multiple: multiple,
        before: function (obj) {
            index = parent.loadding();
        },
        done: function (res) {
            parent.layer.close(index);
            console.log('上传完成：');
            console.log(res);
            func(res);
        },
        error: function (index, obj) {
            parent.layer.close(index);
            console.log(index, obj);
        }
    });
}

/**
 * 初始化请求参数,操作体
 * @param event
 * @param data
 * @param title
 */
function initParam(event, data, title = 'name') {
    let ids = '';
    let titles = '';
    for (let i = 0; i < data.length; i++) {
        if (event === 'add' || event === 'edit') {
            break;
        } else if ((event === 'setFlagTrue' && !data[i].flag)
            || (event === 'setFlagFalse' && data[i].flag)
            || (event === 'dels' && !data[i].del)) {
            ids += data[i].id + ',';
            titles += (i + 1) + '. ' + data[i][title] + '<br />';
        }
    }
    if (event === 'add' || event === 'edit' || event === 'setFlagTrue' || event === 'setFlagFalse' || event === 'dels') {
        if (event !== 'add' && event !== 'edit' && ids.length === 0) {
            if (data.length === 0) {
                parent.alert('未选择任何数据项');
            } else {
                parent.alert('选择的数据项已是该值');
            }
            return undefined;
        }
    }
    return {ids: ids.substring(0, ids.length - 1), titles: titles};
}

/**
 * 解析spring data rest 分页数据
 * @param res
 * @returns {*}
 */
function analysisPageData(res) {
    console.log("分页请求结果解析前 -> ");
    console.log(res);

    if (res && res._embedded && res._links && res.page) {
        let _embedded = res._embedded;
        let _links = res._links;
        let page = res.page;

        let data = [];
        let totalElements = page.totalElements;
        let code = 0;
        let msg = '成功';
        for (let key in _embedded) {
            data = _embedded[key];
            break;
        }

        res = {
            code: code,
            msg: msg,
            count: totalElements,
            data: data,
        };

    }

    console.log("分页请求结果解析后 -> ");
    console.log(res);
    return res;
}


/**
 * 更新数据
 * @param data
 * @param table
 * @param tableData
 */
function restPut(data, table, tableData) {
    reqPut(data.href, data.data, function (res) {                                  // 提交保存数据
        if (res && !res.code && res._links) {                           // 更新成功
            onLoadTable(table, tableData);                              // 重载表格
        } else {
            alert(res.msg);
        }
    });
}

/**
 * 获取数据
 * @param data          源请求路径
 * @param reqListUrl    需要替换的内容
 * @param temp          新的内容
 * @returns {null}
 */
function restGet(data, reqListUrl, temp, func) {
    if (data && data._links && data._links.self && data._links.self.href) {         // 数据有效
        let href = data._links.self.href;                                           // 获取自身请求路径
        href = href.replace(reqListUrl, temp);                                      // 还原路径
        reqGet(href, {}, function (res) {                                           // 获取数据
            if (res && !res.code && res._links) {                                   // 获取成功
                delete res._links;                                                  // 删除不必要的数据
                func({data: res, href: href});
            } else {
                alert(res.msg);
            }
        })
    } else {
        alert('路径错误');
    }
}

