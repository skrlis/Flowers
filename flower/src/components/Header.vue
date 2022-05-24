<template>
    <div style="line-height:60px;display:flex">
        <div style="flex:1; font-size:28px">
        <span :class="collapseBtnClass" style="cursor:pointer" @click="collapse"></span>

        <el-breadcrumb separator="/" style="display:inline-block;margin-left:10px;font-size:19px">
          <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
          <el-breadcrumb-item >{{currentPathName}}</el-breadcrumb-item>

          
        </el-breadcrumb>
        </div>
       <el-dropdown style="width:150px;cursor:pointer;text-align:right">
         <div style="display:inline-block">
           <img :src="manager.avatarUrl" alt=""
           style="width:30px;border-radius:50%;position:relative;top:10px;right:5px">
        <span>{{manager.nikename}}</span><i class="el-icon-arrow-down ml-5"></i>
         </div>
         
        <el-dropdown-menu slot="dropdown" style="width:100px;cursor:pointer;text-align:center">
          <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <router-link to="/password">修改密码</router-link>
        </el-dropdown-item>
          <el-dropdown-item style="font-size:14px;padding:5px 0">
            <router-link to="/person">个人信息</router-link>
            </el-dropdown-item>
          <el-dropdown-item  style="font-size:14px;padding:5px 0">
            <span style="text-decoration:none" @click="logout">退出</span>
            </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
       
        
     
     </div>
</template>

<script>
export default {
    name:"Header",
    props:{
        collapseBtnClass:String,
        manager:Object
    },
  
   computed: {
    currentPathName () {
      return this.$store.state.currentPathName;//需要监听的数据
    }
  },
   data() {
     return {
     }
   },
   created(){
   },
  methods: {
    collapse(){
      this.$emit("asideCollapse")
    },
    logout(){
      this.$store.commit("logout")
      this.$message.success("退出成功")
    },
    }
    }
</script>

<style scoped>
a {
  text-decoration: none;
  color: black;
}
 
.router-link-active {
  text-decoration: none;
}
</style>