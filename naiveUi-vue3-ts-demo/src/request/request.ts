import axios, {
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";
import { ConfigProviderProps, createDiscreteApi, lightTheme } from "naive-ui";
import { computed } from "vue";
import type IResponse from "../interface/response";
import cache from "../utils/cache";
const configProviderPropsRef = computed<ConfigProviderProps>(() => ({
  theme: lightTheme,
}));

const { message } = createDiscreteApi(["message"], {
  configProviderProps: configProviderPropsRef,
});
export class Request {
  private axiosInstance: AxiosInstance;
  constructor() {
    this.axiosInstance = axios.create({
      baseURL: import.meta.env.VITE_BASE_URL,
      timeout: 6000,
    });
    // 初始化拦截器
    this.initInterceptors();
  }

  public createAxios() {
    // 创建axios实例
    return this.axiosInstance;
  }

  // 初始化拦截器
  private initInterceptors() {
    /**
     * 请求拦截器
     * 每次请求前，如果存在token则在请求头中携带token
     */
    this.axiosInstance.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        // 是否需要防止数据重复提交
        const isRepeatSubmit = (config.headers || {}).repeatSubmit === false;
        // get请求映射params参数
        if (config.method === "get" && config.params) {
          let url = config.url + "?" + tansParams(config.params);
          url = url.slice(0, -1);
          config.params = {};
          config.url = url;
        }
        if (
          !isRepeatSubmit &&
          (config.method === "post" || config.method === "put")
        ) {
          const requestObj = {
            url: config.url,
            data:
              typeof config.data === "object"
                ? JSON.stringify(config.data)
                : config.data,
            time: new Date().getTime(),
          };
          const sessionObj = cache.session.getJSON("sessionObj");
          if (sessionObj === undefined || sessionObj === null || !sessionObj) {
            cache.session.setJSON("sessionObj", requestObj);
          } else {
            const s_url = sessionObj.url; // 请求地址
            const s_data = sessionObj.data; // 请求数据
            const s_time = sessionObj.time; // 请求时间
            const interval = 1000; // 间隔时间(ms)，小于此时间视为重复提交
            if (
              s_data === requestObj.data &&
              requestObj.time - s_time < interval &&
              s_url === requestObj.url
            ) {
              const message = "数据正在处理，请勿重复提交";
              console.warn(`[${s_url}]: ` + message);
              return Promise.reject(new Error(message));
            } else {
              cache.session.setJSON("sessionObj", requestObj);
            }
          }
        }
        // const token = Vue.ls.get(ACCESS_TOKEN)
        // if (token) {
        //     config.headers['Authorization'] = 'Bearer ' + token
        // }

        // 登录流程控制中，根据本地是否存在token判断用户的登录情况
        // 但是即使token存在，也有可能token是过期的，所以在每次的请求头中携带token
        // 后台根据携带的token判断用户的登录情况，并返回给我们对应的状态码
        // if (config.headers.isJwt) {
        const token = localStorage.getItem("ACCESS_TOKEN");
        if (token) {
          config.headers.Authorization = "Bearer " + token;
        }
        // }
        return config;
      },
      (error: any) => {
        console.log(error);
      }
    );

    // 响应拦截器
    this.axiosInstance.interceptors.response.use(
      // 请求成功
      (response: AxiosResponse) => {
        // if (res.headers.authorization) {
        //     localStorage.setItem('id_token', res.headers.authorization);
        // } else {
        //     if (res.data && res.data.token) {
        //         localStorage.setItem('id_token', res.data.token);
        //     }
        // }

        if (response.status === 200) {
          if (this.checkBackCode(response.data)) {
            return Promise.resolve(response);
          }
          return Promise.reject(response);
        } else {
          this.errorHandle(response);
          // return Promise.reject(response.data);
          return response;
        }
      },
      // 请求失败
      (error: any) => {
        const { response } = error;
        if (response) {
          // 请求已发出，但是不在2xx的范围
          this.errorHandle(response);
          return Promise.reject(response);
        } else {
          // 处理断网的情况
          // eg:请求超时或断网时，更新state的network状态
          // network状态在app.vue中控制着一个全局的断网提示组件的显示隐藏
          // 关于断网组件中的刷新重新获取数据，会在断网组件中说明
          message.warning("网络连接异常,请稍后再试!");
        }
      }
    );
  }

  /**
   * http握手错误
   * @param res 响应回调,根据不同响应进行不同操作
   */
  private errorHandle(res: any) {
    console.log("error", res);
    console.log(message);

    // 状态码判断
    switch (res.status) {
      case 401:
        break;
      case 403:
        break;
      case 404:
        message.warning("请求的资源不存在");
        break;
      default:
        message.warning("连接错误");
    }
  }
  private checkBackCode(data: IResponse) {
    if (data.code == 200) {
      return true;
    }
    message.warning(data.message);
    return false;
  }
}

/**
 * 参数处理
 * @param {*} params  参数
 */
function tansParams(params: any) {
  let result = "";
  for (const propName of Object.keys(params)) {
    const value = params[propName];
    var part = encodeURIComponent(propName) + "=";
    if (value !== null && value !== "" && typeof value !== "undefined") {
      if (typeof value === "object") {
        for (const key of Object.keys(value)) {
          if (
            value[key] !== null &&
            value[key] !== "" &&
            typeof value[key] !== "undefined"
          ) {
            let params = propName + "[" + key + "]";
            var subPart = encodeURIComponent(params) + "=";
            result += subPart + encodeURIComponent(value[key]) + "&";
          }
        }
      } else {
        result += part + encodeURIComponent(value) + "&";
      }
    }
  }
  return result;
}
