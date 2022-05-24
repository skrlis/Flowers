<template>
  <el-card style="width:500px;">
    <el-form label-width="90px" size="small">
<!-- 头像上传 -->
        <el-upload
          class="avatar-uploader"
          :action="'http://localhost:9090/file/upload'"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
      >
        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
    <el-form-item label="管理员名称" >
      <el-input v-model="form.adname" disabled autocomplete="off"></el-input>
    </el-form-item>
     <el-form-item label="密码" >
      <el-input v-model="form.adpwd" disabled autocomplete="off"></el-input>
    </el-form-item>
     <el-form-item label="昵称" >
      <el-input v-model="form.nikename" autocomplete="off"></el-input>
    </el-form-item>
     <el-form-item label="使用状态">
      <el-input disabled v-model="form.adisused" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
    </el-form-item>
  </el-form>
  </el-card>
</template>

<script>
export default {
    name:"Person",
    data() {
        return {
            form:{},
            manager:localStorage.getItem("manager") ? JSON.parse(localStorage.getItem("manager")) : {}
        }
    },
    created(){
     this.getManager().then(res =>{
       this.form=res
     })
      

    },
    methods: {
      async getManager(){
     
     return (await this.request.get("/manager/adname/" + this.manager.adname)).data
    
      
      },
    save(){
      this.request.post("/manager",this.form).then(res =>{
        if(res.code === '200'){
          this.$message.success("保存成功")
        //触发父级Manage.vue更新Manager的方法
        this.$emit("refreshManager")
        }else{
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(res){
      this.form.avatarUrl=res

    }
    }
}
</script>

<style>
.avatar-uploader{
   text-align:center;
   margin-bottom: 10px;
}
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 138px;
    height: 138px;
    line-height: 138px;
    text-align: center;
  }
  .avatar {
    width: 138px;
    height: 138px;
    display: block;
  }
</style>