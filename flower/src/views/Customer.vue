<template>
  <div>
    <!-- http://localhost:8080/manager才能看到主界面 -->

    <div style="margin: 10px 0">
      <el-input
        type="text"
        suffix-icon="el-icon-search"
        style="width: 200px"
        placeholder="请输入客户名称"
        v-model="cusName"
      ></el-input>
      <el-input
        type="text"
        suffix-icon="el-icon-message"
        style="width: 200px"
        placeholder="请输入联系方式"
        v-model="cusPhone"
        class="ml-5"
      ></el-input>
      <el-select
        clearable
        v-model="cusSort"
        placeholder="请选择客户类型"
        style="width: 200px"
        class="ml-5"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-button type="primary" class="ml-5" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd"
        >新增<i class="el-icon-circle-plus-outline"></i
      ></el-button>

      <el-popconfirm
        class="ml-5"
        confirm-button-text="确定"
        cancel-button-text="我再想想"
        icon="el-icon-info"
        icon-color="red"
        title="您确定批量删除这新数据吗？"
        @confirm="delBatch"
      >
        <el-button type="danger" slot="reference"
          >批量删除<i class="el-icon-remove-outline"></i
        ></el-button>
      </el-popconfirm>

      <el-upload
        action="http://localhost:9090/customer/import"
        :show-file-list="false"
        accept="xlsx"
        style="display: inline-block"
        :on-success="handleExcelImportSuccess"
      >
        <el-button type="primary" class="ml-5"
          >导入<i class="el-icon-bottom"></i
        ></el-button>
      </el-upload>
      <el-button type="primary" class="ml-5" @click="exp"
        >导出<i class="el-icon-top"></i
      ></el-button>
    </div>
    <el-table
      :data="tableData"
      border
      stripe
      :header-cell-class-name="'headerBg'"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="客户编号"> </el-table-column>
      <el-table-column prop="cusName" label="客户名称"> </el-table-column>
      <el-table-column prop="cusSort" label="类别"> </el-table-column>
      <el-table-column prop="cusPhone" label="联系方式"> </el-table-column>
      <el-table-column prop="cusAddress" label="地址"> </el-table-column>
      <el-table-column prop="cusCost" label="总花费"> </el-table-column>
      <el-table-column prop="cusPay" label="总支付"> </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)"
            >编辑<i class="el-icon-edit"></i
          ></el-button>
          <el-popconfirm
            class="ml-5"
            confirm-button-text="确定"
            cancel-button-text="我再想想"
            icon="el-icon-info"
            icon-color="red"
            title="您确定删除吗？"
            @confirm="handleDel(scope.row.id)"
          >
            <el-button type="danger" slot="reference"
              >删除<i class="el-icon-remove-outline"></i
            ></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

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
    <!-- 弹窗 -->
    <el-dialog
      title="新增或编辑客户信息"
      :visible.sync="dialogFormVisible"
      width="30%"
    >
      <el-form
        :model="form"
        status-icon
        :rules="rules"
        ref="ruleForm"
        label-width="90px"
        size="small"
      >
        <el-form-item label="客户名称" prop="cusName">
          <el-input v-model="form.cusName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="类别" prop="cusSort">
          <el-select
            clearable
            v-model="form.cusSort"
            placeholder="请选择"
            style="width: 100%"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="联系方式" prop="cusPhone">
          <el-input v-model="form.cusPhone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="cusAddress">
          <el-input v-model="form.cusAddress" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="总花费" prop="cusCost">
          <el-input v-model.number="form.cusCost" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="总支付" prop="cusPay">
          <el-input v-model.number="form.cusPay" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save">提交</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Customer",
  data() {
    let validCode = (rule, value, callback) => {
      let reg = /[0-9]{11}/;
      if (!reg.test(value)) {
        callback(new Error("请输入正确手机号"));
      } else {
        callback();
      }
    };

    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      cusName: "",
      cusSort: "",
      cusPhone: "",
      dialogFormVisible: false,
      form: {}, //添加的弹出框
      multipleSelection: [],
      options: [
        {
          value: "批发",
          label: "批发",
        },
        {
          value: "零售",
          label: "零售",
        },
      ],
      value: "",
      rules: {
        cusName: [
          //与prop="supName"对应
          { required: true, message: "名称不能为空", trigger: "blur" },
        ],
        cusSort: [{ required: true, message: "类别不能为空", trigger: "blur" }],
        cusPhone: [
          { required: true, message: "联系方式不能为空", trigger: "blur" },
          {
            validator: validCode,
            message: "请输入正确手机号",
            trigger: "blur",
          },
        ],
        cusAddress: [
          { required: true, message: "地址不能为空", trigger: "blur" },
        ],
        cusCost: [
          { required: true, message: "总花费不能为空", trigger: "blur" },
          { type: "number", message: "请输入数字", trigger: "blur" },
        ],
        cusPay: [
          { required: true, message: "总支付不能为空", trigger: "blur" },
          { type: "number", message: "请输入数字", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    //请求分页查询数据
    this.load();
  },
  methods: {
    load() {
      //请求分页查询数据
      this.request
        .get("/customer/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            cusName: this.cusName,
            cusPhone: this.cusPhone,
            cusSort: this.cusSort,
          },
        })
        .then((res) => {
          this.tableData = res.data.records;
          this.total = res.data.total;
        });
    },
    //表单校验
    save() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request.post("/customer", this.form).then((res) => {
            if (res.code === "200") {
              this.$message.success("保存成功");
              this.dialogFormVisible = false;
              this.load();
            } else {
              this.$message.error("保存失败");
            }
          });
        }
      });
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    //新增弹窗
    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {};
    },
    //编辑
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row)); //可以解决编辑未确认就改变的问题
      this.dialogFormVisible = true;
    },
    //删除
    handleDel(id) {
      this.request.delete("/customer/" + id).then((res) => {
        if (res.code === "200") {
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error("删除失败");
        }
      });
    },
    //批量选择
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //批量删除
    delBatch() {
      let ids = this.multipleSelection.map((v) => v.id); //[{},{},{}]=>[1,2,3]扁平化处理把一个对象数组变成纯id数组
      this.request.post("/customer/del/batch", ids).then((res) => {
        if (res.code === "200") {
          this.$message.success("批量删除成功");
          this.load();
        } else {
          this.$message.error("批量删除失败");
        }
      });
    },
    //重置
    reset() {
      this.cusName = "";
      this.cusPhone = "";
      this.cusSort = "";
      this.load();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    //导出
    exp() {
      window.open("http://localhost:9090/customer/export");
    },
    //提示文件导入成功
    handleExcelImportSuccess() {
      this.$message.success("导入成功");
      this.load();
    },
  },
};
</script>

<style>
.headerBg {
  background: #eee !important;
}
</style>