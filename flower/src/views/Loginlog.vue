<template>
  <div>
      <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" style="margin-top:20px ;width: 100%">

        <el-table-column prop="id" label="编号" >
        </el-table-column>
         <el-table-column prop="adname" label="姓名"  >
        </el-table-column>
         <el-table-column prop="createTime" label="登录日期"    :formatter="dateFormat">
        </el-table-column>
       
      </el-table>
      <!-- 页码 -->
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
import moment from "moment"
export default {
     name:"Loginlog",
    data() {
        return {
          tableData: [],
          total:0,
            pageNum:1,
            pageSize:10,
        }
      },
      created(){
          this.load()
      },
  methods: {
          load(){
       this.request.get("/adminlogin/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        console.log(res.data.total)
      })},

      //格式化el-table中的日期
          dateFormat(row,column){
            var date = row[column.property];
            if(date === undefined){
                return ''
            }
            return moment(date).format("YYYY-MM-DD HH:mm:ss")

          },
          handleSizeChange(pageSize){
      this.pageSize=pageSize
      this.load()

    },
    handleCurrentChange(pageNum){
      this.pageNum=pageNum
      this.load()
    },
          
      },
    }

</script>

<style>

</style>