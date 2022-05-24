<template>
  <div>
    <!-- http://localhost:8080/manager才能看到主界面 -->

    <div style="margin: 10px 0">
      <el-input
        type="text"
        suffix-icon="el-icon-search"
        style="width: 200px"
        placeholder="请输入名称"
        v-model="name"
      ></el-input>
      <el-button type="primary" class="ml-5" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAddMian"
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

      <!-- <el-upload
                            action="http://localhost:9090/role/import" 
                            :show-file-list="false"
                             accept="xlsx" style="display:inline-block"
                             :on-success="handleExcelImportSuccess">
                           <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>
                        </el-upload>
         <el-button type="primary" class="ml-5" @click="exp">导出<i class="el-icon-top"></i></el-button> -->
    </div>
    <el-table
      :data="tableData"
      border
      stripe
      :header-cell-class-name="'headerBg'"
      row-key="id"
      default-expand-all
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="编号" width="80"> </el-table-column>
      <el-table-column prop="name" label="名称"> </el-table-column>
      <el-table-column prop="path" label="路径"> </el-table-column>
      <el-table-column prop="pagePath" label="页面路径"> </el-table-column>
      <el-table-column
        label="图标"
        class-name="fontSize18"
        align="center"
        label-class-name="fontSize12"
      >
        <template slot-scope="scope">
          <i :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述"> </el-table-column>
      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            @click="handleAdd(scope.row.id)"
            v-if="!scope.row.pid && !scope.row.path"
            >新增子菜单<i class="el-icon-plus"></i
          ></el-button>
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

    <!-- 弹窗 -->
    <el-dialog
      title="新增或编辑菜单信息"
      :visible.sync="dialogFormVisible"
      width="30%"
    >
      <el-form
        :model="form"
        label-width="90px"
        size="small"
        :rules="rules"
        ref="ruleForm"
      >
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径" prop="path">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="页面路径" prop="pagePath">
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-select
            clearable
            v-model="form.icon"
            placeholder="请选择"
            style="width: 100%"
          >
            <el-option
              v-for="item in options"
              :key="item.name"
              :label="item.name"
              :value="item.value"
            >
              <i :class="item.value" />{{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
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
  name: "Menu",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      name: "",
      dialogFormVisible: false,
      form: {}, //添加的弹出框
      multipleSelection: [],
      options: [],
      rules: {
        name: [
          //与prop="adname"对应
          { required: true, message: "名称不能为空", trigger: "blur" },
        ],
        path: [{ required: true, message: "路径不能为空", trigger: "blur" }],
        pagePath: [
          { required: true, message: "页面路径不能为空", trigger: "blur" },
        ],
        icon: [{ required: true, message: "图标不能为空", trigger: "blur" }],
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
        .get("/menu", {
          params: {
            name: this.name,
          },
        })
        .then((res) => {
          this.tableData = res.data;
        });
      // 请求图标的数据
      this.request.get("/menu/icons").then((res) => {
        this.options = res.data;
      });
    },
    //保存数据
    save() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request.post("/menu", this.form).then((res) => {
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
    handleAddMian() {
      this.dialogFormVisible = true;
      this.form = {};
    },
    //新增弹窗
    handleAdd(pid) {
      this.dialogFormVisible = true;
      this.form = {};
      if (pid) {
        this.form.pid = pid;
      }
    },
    //编辑
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row)); //可以解决编辑未确认就改变的问题
      this.dialogFormVisible = true;
    },
    //删除
    handleDel(id) {
      this.request.delete("/menu/" + id).then((res) => {
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
      this.request.post("/menu/del/batch", ids).then((res) => {
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
      this.name = "";
      this.description = "";
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
      window.open("http://localhost:9090/menu/export");
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
.fontSize18 {
  font-size: 18px; /**内容字体 */
}
.fontSize12 {
  font-size: 12px; /**头部字体 */
}
</style>