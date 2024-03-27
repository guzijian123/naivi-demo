import type { AxiosInstance } from "axios";
import { Request } from "./request";

const request: AxiosInstance = new Request().createAxios();
export default request;
