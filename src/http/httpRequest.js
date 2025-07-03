import axios from "axios";

//后端java的http接口地址前缀
axios.defaults.baseURL = "http://localhost:8090";

/*
    axios发送get请求
    @param url '/api/resources'
    @param params{
            ID:1234
        }
 */

export function doGet(url,params){
    //get方法
    return axios.get(url,{ params})
        .then(response=> {
            if(response.data.code === 200){
                return response;
            }else{
                throw new Error(response.data.message);
            }
        })
        .catch(error=> {
            console.error("请求失败",error);
            throw error;
        });
}

export function doPost(url,params){
    return axios.post(url,params);
    //不同页面不同处理 then函数在页面中写
}

export function doPut(url,params){
    return axios.put(url,params);
}

export function doDelete(url,params){
    //get方法
    return axios.delete(url,{ params})
        .then(response=> {
            if(response.data.code === 200){
                return response;
            }else{
                throw new Error(response.data.message);
            }
        })
        .catch(error=> {
            console.error("请求失败",error);
            throw error;
        });
}