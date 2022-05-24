<template>
  <div class="wrapper">
    <div
      style="
        margin: 200px auto;
        background-color: #fff;
        width: 350px;
        height: 300px;
        padding: 20px;
        border-radius: 10px;
      "
    >
      <div style="margin: 20px 0; text-align: center; font-size: 24px">
        <b>登 录</b>
      </div>
      <el-form :model="manager" :rules="rules" ref="managerForm">
        <el-form-item prop="adname">
          <el-input
            size="medium"
            style="margin: 10px 0"
            prefix-icon="el-icon-user"
            v-model="manager.adname"
          ></el-input>
        </el-form-item>
        <el-form-item prop="adpwd">
          <el-input
            size="medium"
            style="margin: 10px 0"
            prefix-icon="el-icon-lock"
            show-password
            v-model="manager.adpwd"
          ></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button
            style="width: 310px"
            type="primary"
            size="small"
            autocomplete="off"
            @click="login"
            >登录</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { setRoutes } from "@/router";
export default {
  name: "Login",
  data() {
    return {
      manager: {},
      rules: {
        adname: [
          //与prop="adname"对应
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 2, max: 5, message: "长度在 2 到 5 个字符", trigger: "blur" },
        ],
        adpwd: [
          //与prop="adpwd"对应
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 5,
            max: 13,
            message: "长度在 5 到 13 个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },

  methods: {
    login() {
      this.$refs["managerForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request.post("/manager/login", this.manager).then((res) => {
            if (res.code === "200") {
              localStorage.setItem("manager", JSON.stringify(res.data)); //存储用户信息到浏览器
              localStorage.setItem("menus", JSON.stringify(res.data.menus)); //存储菜单信息到浏览器
              //动态设置当前用户的路由
              setRoutes();
              this.$router.push("/");
              this.$message.success("登录成功");
              //添加到登录日志
              this.request.post(
                "/adminlogin",
                (this.manager = {
                  adname: this.manager.adname,
                })
              );
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
    },
  },
};
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(
    to bottom right,
    #fc466b,
    #3f5efb
  ); /*从左上到右下渐变*/
  overflow: hidden; /*超出的部分隐藏掉*/
}
</style>