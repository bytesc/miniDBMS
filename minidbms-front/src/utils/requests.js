//axios 封装

import axios from "axios";
import {ElMessage} from "element-plus";

import NProgress from "nprogress";
import "nprogress/nprogress.css";


// 全局配置
const service = axios.create({
    baseURL:"/",
    timeout:10000,  //请求超时
})

service.interceptors.request.use(config => {
    NProgress.start()
    return config
})

// 响应拦截
service.interceptors.response.use(res=>{
        NProgress.done()
        // if (!res.headers['content-type'].includes('application/json')) {
        //     return res;
        // }
        // console.log(res)
        // res为获取的所有数据
        // console.log(res)
        // const {code,data,msg} = res.data
        // code,data,msg是后端返回json里的三个字段（后端随便约定）
        // code是后端随便约定的状态标识码（不是http状态码）
        // if (code === "200" || code === "201" ){
        //
        //     return data
        // }else if(code === "400"){
        //     // 后端返回失败
        //     ElMessage.error(msg)
        //     console.log(res.data)
        //     return data
        // }
        ElMessage.success("数据库响应成功")
        return res.data
    },
    error => {
        NProgress.done()
        ElMessage.error("网络连接超时")
        return Promise.reject(error);
    }
)

// 请求方法配置
function requestPack(options){
    if(options.method.toLowerCase() === "get"){
        options.params = options.data
    }
    // // 从localStorage中获取token
    // const token = localStorage.getItem('token')
    // const longToken = localStorage.getItem('long_token')
    // const name = localStorage.getItem('name')
    // // 将token添加到请求头中
    // options.headers = {...options.headers, 'token': token,'name':name,"long_token":longToken}
    return service({
        ...options,
        // headers: options.headers,  // 在这里设置headers
    })
}

// 给request添加request.post()和.get(),从而给request传参（这一步作用是封装）
["get","post"].forEach(item=>{
    requestPack[item] = (url, data, headers) =>{
        return requestPack({
            url,
            data,
            headers,
            method:item
        })
    }
})

// 导出才能用
export {requestPack}