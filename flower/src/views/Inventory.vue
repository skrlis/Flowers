<template>
  <div>
    <span>商品名称：</span>
    <el-input
      v-model="name"
      style="width: 300px"
      placeholder="请输入商品名称"
      clearable
    >
    </el-input>
    <el-button
      style="margin-left: 50px"
      type="primary"
      @click="load"
      icon="Search"
      >查询
    </el-button>
    <el-button type="warning" @click="reset">重置</el-button>

    <el-table
      :data="tableData"
      border
      stripe
      :header-cell-class-name="'headerBg'"
      style="margin-top: 20px; width: 100%"
    >
      <el-table-column prop="name" label="名称"> </el-table-column>
      <el-table-column prop="proFrom" label="供应商"> </el-table-column>
      <el-table-column prop="proStock" label="库存">
        <template slot-scope="scope">
          <span v-if="scope.row.proStock <= 5" style="color: red">{{
            scope.row.proStock
          }}</span>
          <span v-if="scope.row.proStock > 5" style="color: black">{{
            scope.row.proStock
          }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="proCost" label="进价"> </el-table-column>
    </el-table>
    <!-- 
       <template slot-scope="scope">
            <el-tag type="primary" v-if="scope.row.flag === 'ROLE_ADMIN'">管理员</el-tag>

            <template slot-scope="scope">
          <span style="color:#6ba0d6">{{scope.row.buyid}}</span> 
         </template> -->
    <!-- 页码 -->
    <div style="padding: 10px 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "Loginlog",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      psort: "",
      name: "",
    };
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      this.request
        .get("/product/page1", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name: this.name,
            psort: this.psort,
          },
        })
        .then((res) => {
          this.tableData = res.data.records;
          this.total = res.data.total;
          console.log(res.data.total);
        });
    },

    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    //重置
    reset() {
      this.name = "";
      this.load();
    },
  },
};
</script>

<style>
</style>