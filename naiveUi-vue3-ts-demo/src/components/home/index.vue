<template>
  <div class="home-container">
    <n-space>
      <n-button type="primary" @click="handleAdd">
        新增
      </n-button>
      <n-button type="warning" @click="function () { showDeleteModal = true }">
        批量删除
      </n-button>
    </n-space>
    <div class="home-tales">
      <n-data-table @update:checked-row-keys="handleCheck" :row-key="rowKey" :loading="tableLoading" :columns="columns"
        :data="data" :pagination="pagination" :bordered="false" />
    </div>
    <n-modal v-model:show="showModal" preset="card" :style="{ width: '600px' }" :title="title">
      <!-- <template #header>
        <div>{{ title }}</div>
      </template> -->
      <template #default>
        <n-form ref="formRef" :model="paramForm" :rules="rules" label-placement="left" :label-width="100"
          label-align="left" require-mark-placement="right-hanging" size="medium" :style="{
        maxWidth: '640px'
      }">
          <n-form-item label="标题" path="title">
            <n-input v-model:value="paramForm.title" placeholder="请输入" />
          </n-form-item>
          <n-form-item label="内容" path="content">
            <n-input v-model:value="paramForm.content" placeholder="请输入" type="textarea"
              :autosize="{ minRows: 3, maxRows: 5 }" />
          </n-form-item>
          <n-form-item label="排序" path="sort">
            <n-input v-model:value="paramForm.sort" placeholder="请输入" />
          </n-form-item>
        </n-form>
      </template>
      <template #action>
        <n-space reverse>
          <n-button type="primary" :loading="buttonLoading" @click="handleSubmit(paramForm)">
            确认
          </n-button>
          <n-button type="tertiary" @click="handleCancel">
            取消
          </n-button>

        </n-space>
      </template>
    </n-modal>
    <!-- 删除提示框 -->
    <n-modal v-model:show="showDeleteModal" preset="dialog" type="warning" title="提示" positive-text="确认"
      negative-text="取消" @positive-click="submitCallback" @negative-click="() => showDeleteModal = false">
    </n-modal>
  </div>
</template>

<script lang="ts" setup>
import { Delete16Regular, Edit16Regular } from '@vicons/fluent';
import { Icon } from '@vicons/utils';
import type { DataTableColumns } from 'naive-ui';
import { FormInst, NButton, NSpace, NSwitch, useMessage } from 'naive-ui';
import { h, onMounted, ref } from 'vue';
import { addAdvertisement, deleteAdvertisement, getAdvertisementById, getAdvertisementList, updateAdvertisement } from '../../apis/advertisement';
import type { IAdvertisementParams } from '../../interface/advertisement';
const message = useMessage()
const tableLoading = ref<boolean>(false)
const buttonLoading = ref<boolean>(false)
const showModal = ref<boolean>(false)
const title = ref<string>("新增广告")
const formRef = ref<FormInst | null>(null);
const _ids = ref<Array<string>>([])
const _id = ref<string>()
const showDeleteModal = ref<boolean>(false)
const paramForm = ref<IAdvertisementParams>({
  title: "",
  status: null,
  sort: null,
  content: null,
  id: null,
  pageNow: null,
  pageSize: null
})

const rules = {
  sort: {
    required: true,
    trigger: ['input'],
    message: '请输入数字排序',
    validator: (rule: any, val: string) => {
      console.log("rule", rule);
      const regex = /^(-|\+)?\d+(\.\d+)?$/

      return regex.test(val);
    }
  },
  content: {
    required: true,
    trigger: ['blur', 'input'],
    message: '请输入内容'
  },
  title: {
    required: true,
    trigger: ['blur', 'input'],
    message: '请输入标题'
  },
}

const rowKey = (row: IAdvertisementParams) => {
  return row.id
}

const handleCheck = (row: Array<string>) => {
  console.log(row);

  _ids.value = row
}

const createColumns = ({
  handleEdit: handleEdit,
  deleteConfirm: deleteConfirm
}: {
  handleEdit: (id?: string | null) => void,
  deleteConfirm: (id?: string | null) => void
}): DataTableColumns<IAdvertisementParams> => {
  return [
    {
      type: 'selection',
      align: 'center',
      multiple: true,
    },
    {
      title: '标题',
      key: 'title',
      maxWidth: 340,
      minWidth: 200,
      width: 340
    },
    {
      title: '内容',
      key: 'content',
      maxWidth: 340,
      minWidth: 200,
      width: 340
    },
    {
      title: '状态',
      key: 'status',
      maxWidth: 340,
      minWidth: 200,
      width: 340,
      render(row: IAdvertisementParams) {
        return h(NSwitch, {
          value: row.status ? true : false,
          onUpdateValue: (val: boolean) => {
            let param = row;
            param.status = val ? 1 : 0;
            updateAdvertisement(param).then(res => {
              console.log(res);
              message.success("操作成功")
              row.status = param.status
            })
          }
        }, {
          checked: () => {
            return "启用"
          },
          unchecked: () => {
            return "停用"
          }
        })
      }
    },
    {
      title: '排序',
      key: 'sort',
      maxWidth: 340,
      minWidth: 200,
      width: 340
    },
    {
      title: '操作',
      key: 'actions',
      titleAlign: 'right',
      align: 'right',
      render(row: IAdvertisementParams) {
        // 设置编辑按钮加上图标
        return h(
          NSpace, { reverse: true }, {
          default: () => [
            h(
              NButton,
              {
                iconPlacement: 'left',
                size: 'small',
                renderIcon: () => h(Icon, {
                  size: "16px"
                }, {
                  default: () => h(Delete16Regular)
                }),
                type: 'error',
                onClick: () => deleteConfirm(row.id)
              },
              { default: () => '删除' }
            ),
            h(
              NButton,
              {
                iconPlacement: 'left',
                size: 'small',
                renderIcon: () => h(Icon, {
                  size: "16px"
                }, {
                  default: () => h(Edit16Regular)
                }),
                type: 'primary',
                onClick: () => handleEdit(row.id)
              },
              { default: () => '编辑' }
            ),
          ]
        }
        )
      }
    }
  ]
}

const columns = createColumns({
  // 单删操作
  deleteConfirm(id?: string | null) {
    if (id) {
      _id.value = id;
      showDeleteModal.value = true;
    }
  }
  ,
  // 编辑操作
  handleEdit(id?: string | null) {
    // message.info(`Play ${row.title}`)
    console.log(id);
    if (id) {
      getAdvertisementById(id).then((res) => {
        paramForm.value = res.data.data
        showModal.value = true
        title.value = "修改广告"
      })
    }
  }
})
const data = ref<IAdvertisementParams[]>([])
const submitCallback = () => {
  handleDelete()
}
const pagination = {
  pageSize: 10
}
const handleCancel = () => {
  showModal.value = false
  reset()
}


const handleSubmit = (row: IAdvertisementParams) => {
  console.log(row);
  formRef.value?.validate().then((res) => {
    if (res) {
      buttonLoading.value = true
      if (row.id) {
        // 修改
        updateAdvertisement(row).then((res) => {
          console.log("修改成功", res);
          message.success("保存成功")
          reset();
          getList();
          showModal.value = false
          buttonLoading.value = false
        })
      } else {
        // 新增
        addAdvertisement(paramForm.value).then((res) => {
          console.log("新增成功", res);
          message.success("新增成功")
          getList();
          showModal.value = false
          buttonLoading.value = false
          reset()
        })
      }
      // 重置
      reset()
    }
  })
}
// 获取参数集合
const getList = (row?: IAdvertisementParams) => {
  tableLoading.value = true
  console.log(row);
  // TODO 获取列表
  getAdvertisementList().then((res) => {
    console.log(res);
    tableLoading.value = false
    data.value = res.data.data
  })
}
// 新增
const handleAdd = () => {
  showModal.value = true;
  title.value = "新增广告"
}
// 处理删除
const handleDelete = () => {
  let ids = _id.value ? [_id.value] : _ids.value;
  _id.value = ""
  deleteAdvertisement(ids).then((res) => {
    console.log("删除成功", res);
    message.success("删除成功")
    getList()
  })
}
const reset = () => {
  paramForm.value.title = ""
  paramForm.value.status = null
  paramForm.value.sort = null
  paramForm.value.content = null
  paramForm.value.id = null
}
onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
// 引入css样式
@import './home.scss';
</style>
