export interface IAdvertisementParams {
  /** 广告位id */
  id?: string | null;
  /** 广告位名称 */
  title?: string;
  /** 广告位描述 */
  content?: string | null;
  /** 广告位状态 */
  status?: number | null;
  /** 排序 */
  sort?: number | null;
  pageNow?: number | null;
  pageSize?: number | null;
}
