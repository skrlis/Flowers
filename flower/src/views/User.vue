<template>
  <div>
    <!-- http://localhost:8080/manager才能看到主界面 -->

    <div style="margin: 10px 0">
      <el-input
        type="text"
        suffix-icon="el-icon-search"
        style="width: 200px"
        placeholder="请输入名称"
        v-model="adname"
      ></el-input>
      <el-button type="primary" class="ml-5" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd"
        >新增<i class="el-icon-circle-plus-outline"></i
      ></el-button>
    </div>
    <el-table
      :data="tableData"
      border
      stripe
      :header-cell-class-name="'headerBg'"
    >
      <el-table-column prop="adid" label="管理员编号" width="140">
      </el-table-column>
      <el-table-column prop="adname" label="管理员姓名" width="120">
      </el-table-column>
      <el-table-column prop="role" label="角色">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.role === 'ROLE_ADMIN'"
            >管理员</el-tag
          >
          <el-tag type="warning" v-if="scope.row.role === 'ROLE_BUYER'"
            >采购员</el-tag
          >
          <el-tag type="success" v-if="scope.row.role === 'ROLE_SALER'"
            >销售员</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column prop="adpwd" label="密码"> </el-table-column>
      <el-table-column prop="phone" label="联系方式"> </el-table-column>
      <el-table-column label="使用状态">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.enable"
            active-color="#13ce66"
            inactive-color="#ccc"
            @change="changeEnable(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
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
            @confirm="handleDel(scope.row.adid)"
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
      title="新增或编辑用户信息"
      :visible.sync="dialogFormVisible"
      width="30%"
    >
      <el-form
        :model="form"
        label-width="90px"
        size="small"
        :rules="rules"
        ref="ruleForm"
        class="demo-ruleForm"
      >
        <el-form-item label="名称" prop="adname">
          <el-input v-model="form.adname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select
            clearable
            v-model="form.role"
            placeholder="请选择角色"
            style="width: 100%"
          >
            <el-option
              v-for="item in roles"
              :key="item.name"
              :label="item.name"
              :value="item.flag"
            >
              <i :class="item.value" />{{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="密码" prop="adpwd">
          <el-input v-model.number="form.adpwd" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save()">提交</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    let validCode = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("密码不能为空"));
      }
      if (!Number.isInteger(value)) {
        callback(new Error("请输入数字值"));
      } else {
        if (value < rule.min) {
          callback(new Error("长度不能少于4个字符"));
        } else {
          callback();
        }
      }
    };
    let validCode1 = (rule, value, callback) => {
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
      adname: "",
      dialogFormVisible: false,
      form: {}, //添加的弹出框
      multipleSelection: [],
      roles: [],
      status: [
        {
          value: "是",
          label: "是",
        },
        {
          value: "否",
          label: "否",
        },
      ],
      rules: {
        adname: [
          //与prop="adname"对应
          { required: true, message: "名称不能为空", trigger: "blur" },
          { required: true, min: 2, message: "长度不能小于2", trigger: "blur" },
        ],
        role: [
          {
            required: true,
            message: "角色不能为空",
            trigger: ["blur", "change"],
          },
        ],
        phone: [{ required: true, validator: validCode1, trigger: "blur" }],
        adpwd: [
          { required: true, min: 4, validator: validCode, trigger: "blur" },
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
        .get("/manager/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            adname: this.adname,
          },
        })
        .then((res) => {
          this.tableData = res.data.records;
          this.total = res.data.total;
        });
      this.request.get("/role").then((res) => {
        this.roles = res.data;
      });
    },
    //保存数据
    save() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request.post("/manager", this.form).then((res) => {
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
    //启用
    changeEnable(row) {
      this.request.post("/manager/update", row).then((res) => {
        if (res.code === "200") {
          this.$message.success("操作成功");
        }
      });
    },
    //删除
    handleDel(adid) {
      this.request.delete("/manager/" + adid).then((res) => {
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
      let adids = this.multipleSelection.map((v) => v.adid); //[{},{},{}]=>[1,2,3]扁平化处理把一个对象数组变成纯id数组
      this.request.post("/manager/del/batch", adids).then((res) => {
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
      this.adname = "";
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
      window.open("http://localhost:9090/manager/export");
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