import { AxiosRequestConfig } from "axios";
import {
  IAdvertisementParams,
  IAdvertisementResponse,
} from "../../interface/advertisement";
import request from "../../request";

const prefix = "/advertisement";

export async function addAdvertisement(
  data: IAdvertisementParams,
  config?: AxiosRequestConfig<IAdvertisementParams>
) {
  return await request.post<IAdvertisementResponse>(
    `${prefix}/add`,
    data,
    config
  );
}

export async function getAdvertisementList(params?: IAdvertisementParams) {
  return await request.get<IAdvertisementResponse>(`${prefix}/list`, {
    params,
  });
}

export async function deleteAdvertisement(
  params: string[],
  config?: AxiosRequestConfig<IAdvertisementParams>
) {
  return await request.delete<IAdvertisementResponse>(
    `${prefix}/delete/` + params,
    config
  );
}

export async function updateAdvertisement(
  data: IAdvertisementParams,
  config?: AxiosRequestConfig<IAdvertisementParams>
) {
  return await request.put<IAdvertisementResponse>(
    `${prefix}/update`,
    data,
    config
  );
}

export async function getAdvertisementById(
  id: string,
  config?: AxiosRequestConfig<IAdvertisementParams>
) {
  return await request.get<IAdvertisementResponse>(`${prefix}/${id}`, config);
}
