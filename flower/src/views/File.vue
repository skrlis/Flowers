<template>
  <div>
       <div style="margin:10px 0">
        <el-input  type="text" suffix-icon="el-icon-search" style="width:200px" placeholder="请输入名称" v-model="name"></el-input>
        <el-button type="primary" class="ml-5" @click="load"  >搜索</el-button>
        <el-button type="warning"  @click="reset"  >重置</el-button>

      </div>
 <div style="margin:10px 0">
          <el-upload
                  action="http://localhost:9090/file/upload" 
                 :show-file-list="false"
                  style="display:inline-block"
                  :on-success="handleFileUploadSuccess">
                  <el-button type="primary" class="ml-5">上传文件<i class="el-icon-top"></i></el-button>
          </el-upload>
         <el-popconfirm
                class="ml-5"
                confirm-button-text='确定'
                cancel-button-text='我再想想'
                icon="el-icon-info"
                icon-color="red"
                title="您确定批量删除这新数据吗？"
                @confirm="delBatch"
            >
         <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i></el-button>

                        </el-popconfirm>

                     
 </div>
       <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"  @selection-change="handleSelectionChange">

         <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="ID" width="80">
        </el-table-column>
        <el-table-column prop="name" label="文件名称">
        </el-table-column>
        <el-table-column prop="type" label="文件类型">
        </el-table-column>
        <el-table-column prop="size" label="文件大小（KB）">
        </el-table-column>
        <el-table-column label="下载">
            <template slot-scope="scope">
                <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
            </template>
        </el-table-column>
        <el-table-column label="启用">
            <template slot-scope="scope">
                <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
            </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-popconfirm
                class="ml-5"
                confirm-button-text='确定'
                cancel-button-text='我再想想'
                icon="el-icon-info"
                icon-color="red"
                title="您确定删除吗？"
                @confirm="handleDel(scope.row.id)"
            >
             <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
            </el-popconfirm>
           
          </template>
        </el-table-column>
      </el-table>
<!-- 分页组件 -->
      <div style="padding:10px 0">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
  </div>
</template>

<script>
export default {
    name:"File",
data() {
    return {
        tableData:[],
        name:'',
        total:0,
        pageNum:1,
        pageSize:5,
        multipleSelection:[],
        headerBg:'headerBg',
    }
},
created() {
    this.load()
},
methods: {
      load(){
       //请求分页查询数据
       this.request.get("/file/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {

        this.tableData = res.data.records
        this.total = res.data.total

      })
    
  
    },
    changeEnable(row){
        this.request.post("/file/update",row).then(res => {
            if(res.code === '200'){
                this.$message.success("操作成功")
            }
        })

    },
    //删除
      handleDel(id){
      this.request.delete("/file/" + id).then(res => {
          if(res.code === '200'){
          this.$message.success("删除成功")
           this.load()
        }else{
          this.$message.error("删除失败")
        }
      })},
    //批量选择
    handleSelectionChange(val){
      this.multipleSelection=val
    },
    //批量删除
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id) //[{},{},{}]=>[1,2,3]扁平化处理把一个对象数组变成纯id数组
       this.request.post("/file/del/batch",ids).then(res => {
          if(res.code === '200'){
          this.$message.success("批量删除成功")
           this.load()
        }else{
          this.$message.error("批量删除失败")
        }
      })
    },
     //重置
    reset(){
      this.name=""
      this.load()
    },
    handleSizeChange(pageSize){
      this.pageSize=pageSize
      this.load()

    },
    handleCurrentChange(pageNum){
      this.pageNum=pageNum
      this.load()
    },
    handleFileUploadSuccess(res){
        console.log(res)
         this.load()
    },
    download(url){
        window.open(url)
    }
  }
}

</script>

<style>
.headerBg{
    background:#eee !important;
  }
</style>