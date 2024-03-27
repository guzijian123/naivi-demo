import type { AxiosRequestConfig } from "axios";
import type { IUserLoginParams } from "../../interface/user/requestParam";
import type { IUserLoginResponse } from "../../interface/user/responseData";
import request from "../../request";

const prefix = "/user";

/**
 * @method 用户登录
 */
export async function userLogin(
  data: IUserLoginParams,
  config?: AxiosRequestConfig<IUserLoginParams>
) {
  return await request.post<IUserLoginResponse>(
    `${prefix}/login`,
    data,
    config
  );
}
/**
 * @method 发送验证码
 */
export function sendCode(email: string) {
  return request.get<Response>(`${prefix}/sendCode`, { params: { email } });
}
