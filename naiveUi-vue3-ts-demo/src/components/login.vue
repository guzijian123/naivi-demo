<template>
  <div class="container">
    <div class="login-box">
      <div class="login-title">
        naviUi示例
      </div>
      <div class="login-username">
        <n-space vertical>
          <n-input v-model:value="loginParams.email" placeholder="账号" :allow-input="allowInput" />
        </n-space>
      </div>
      <div class="login-password">
        <n-space vertical>
          <n-input v-model:value="loginParams.password" type="password" show-password-on="mousedown" placeholder="密码" />
        </n-space>
      </div>
      <div class="login-submit">
        <n-button type="primary" @click="handleClick">
          登录
        </n-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useMessage } from "naive-ui";
import { ref } from 'vue';
import { userLogin } from '../apis/user';
// 引入route
import { useRouter } from 'vue-router';
import type { IUserLoginParams } from './../interface/user/requestParam';
const router = useRouter();
const uMessage = useMessage();
const loginParams = ref<IUserLoginParams>(
  {
    email: '',
    password: '',
    code: ''
  }
);
const prompt = ref<string>('');
// 校验输入邮箱是否合法
const allowInput = (value: string) => {
  let result = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/.test(value);
  console.log(result);
  if (!result) {
    prompt.value = '请输入正确的邮箱';
  } else {
    prompt.value = '';
  }
  return true;
};
const handleClick = () => {
  if (loginParams.value.email && loginParams.value.password) {
    userLogin(loginParams.value).then((res) => {
      localStorage.setItem('token', res.data.data)
      uMessage.success("登录成功")
      router.push({ path: '/home' })
    })
  }
};

</script>

<style lang="scss" scoped>
@mixin flex {
  display: flex;
  justify-content: center;
  align-items: center;
}

.container {
  background-color: aquamarine;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;

  .login-box {
    width: 600px;
    height: 400px;
    display: flex;
    flex-direction: column;
    background-color: white;
    align-items: center;
    border-radius: 20px;
    padding: 20px;

    .login-title {
      margin-bottom: 20px;
      font-size: 40px;
    }

    .login-username {
      margin-bottom: 20px;
      width: 50%;
    }

    .login-password {
      margin-bottom: 20px;
      width: 50%;
    }

    .login-submit {
      margin-bottom: 20px;
      @include flex();
    }
  }
}
</style>
