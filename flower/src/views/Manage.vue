<template>
  <div style="height:100%">
    <el-container style="min-height:100vh;">
  <el-aside :width="sideWidth + 'px'" style="min-height:100vh;overflow-x:hidden;box-shadow:2px 0 6px rgb(0 21 41 / 35%)"
  >
   <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" />
  </el-aside>
  
  <el-container>
    <el-header style="font-size: 12px;border-bottom:1px solid #ccc;line-height:60px;">
    <Header :collapseBtnClass="collapseBtnClass" @asideCollapse="collapse" :manager="manager"/>
    </el-header>
    
    <el-main>
      <!-- 表示当前页面的子路由会在router-view里面展示 -->
      <router-view  @refreshManager="getManager"/>
    </el-main>
  </el-container>
</el-container>
  </div>
</template>

<script>
import Aside from '@/components/Aside.vue'
import Header from '@/components/Header.vue'

export default {
  name: 'Manage',
  data() {
    return {
      collapseBtnClass:'el-icon-s-fold',
      isCollapse:false,
      sideWidth:200,
      logoTextShow:true,       
      manager:{}

          }
  },
  components:{
    Aside,Header
  },
 created() {
   //从后台获取最新的Manager数据
   this.getManager()
 },
  methods: {
    //点击收缩按钮触发
    collapse(){
      this.isCollapse=!this.isCollapse
      if(this.isCollapse){//收缩
        this.sideWidth=48
        this.collapseBtnClass='el-icon-s-unfold'
        this.logoTextShow=false
      }else{//展开
        this.sideWidth=200
        this.collapseBtnClass='el-icon-s-fold'
        this.logoTextShow=true
      }
    },
    getManager(){
      let adname= localStorage.getItem("manager") ? JSON.parse(localStorage.getItem("manager")).adname : ""
      //从后台获取Manager数据
      this.request.get("/manager/adname/" + adname).then(res =>{
      //重新赋值后台的最新Manager数据
      this.manager=res.data
      })
      
    }
   
   
  }}
</script>
<style>
 
</style>