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
      <el-table-column prop="id" label="编号" width="80"> </el-table-column>
      <el-table-column prop="name" label="名称">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.flag === 'ROLE_ADMIN'"
            >管理员</el-tag
          >
          <el-tag type="warning" v-if="scope.row.flag === 'ROLE_BUYER'"
            >采购员</el-tag
          >
          <el-tag type="success" v-if="scope.row.flag === 'ROLE_SALER'"
            >销售员</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column prop="flag" label="唯一标识"> </el-table-column>
      <el-table-column prop="description" label="描述"> </el-table-column>
      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row)"
            >分配菜单<i class="el-icon-menu"></i
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
    <!-- 弹窗1 -->
    <el-dialog
      title="新增或编辑角色信息"
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
        <el-form-item label="唯一标识" prop="flag">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <!-- <el-form-item label="活动区域" :label-width="formLabelWidth">
      <el-select v-model="form.region" placeholder="请选择活动区域">
        <el-option label="区域一" value="shanghai"></el-option>
        <el-option label="区域二" value="beijing"></el-option>
      </el-select>
    </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save">提交</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </div>
    </el-dialog>

    <!-- 弹窗2-->
    <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
      <el-tree
        :props="props"
        :data="menuData"
        show-checkbox
        node-key="id"
        ref="tree"
        :default-expanded-keys="expends"
        :default-checked-keys="checks"
      >
        <span class="custom-tree-node" slot-scope="{ data }">
          <span><i :class="data.icon"></i>{{ data.name }}</span>
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      name: "",
      dialogFormVisible: false,
      menuDialogVis: false,
      form: {}, //添加的弹出框
      multipleSelection: [],
      headerBg: "headerBg",
      menuData: [],
      props: {
        label: "name",
      },
      rules: {
        name: [
          //与prop="supName"对应
          { required: true, message: "名称不能为空", trigger: "blur" },
        ],
        flag: [
          //与prop="supName"对应
          { required: true, message: "唯一标识不能为空", trigger: "blur" },
        ],
      },
      expends: [],
      checks: [],
      roleId: 0,
      roleFlag: "",
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
        .get("/role/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            id: this.id,
            name: this.name,
            description: this.description,
          },
        })
        .then((res) => {
          this.tableData = res.data.records;
          this.total = res.data.total;
        });
    },
    //保存数据
    save() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request.post("/role", this.form).then((res) => {
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
    //分配菜单
    saveRoleMenu() {
      this.request
        .post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys())
        .then((res) => {
          if (res.code === "200") {
            this.$message.success("绑定成功");
            this.menuDialogVis = false;

            //操作管理员角色后需要重新登录
            if (this.roleFlag === "ROLE_ADMIN") {
              this.$store.commit("logout");
            }
          } else {
            this.$message.error(res.msg);
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
      this.request.delete("/role/" + id).then((res) => {
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
      this.request.post("/role/del/batch", ids).then((res) => {
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
    selectMenu(role) {
      this.menuDialogVis = true;
      this.roleId = role.id;
      this.roleFlag = role.flag;
      //请求菜单数据
      this.request.get("/menu").then((res) => {
        this.menuData = res.data;
        //把后台返回的菜单数据处理成id数组
        this.expends = this.menuData.map((v) => v.id);
      });

      this.request.get("/role/roleMenu/" + this.roleId).then((res) => {
        //先渲染弹窗里的元素
        this.menuDialogVis = true;

        this.checks = res.data;

        this.request.get("/menu/ids").then((r) => {
          const ids = r.data;
          ids.forEach((id) => {
            if (!this.checks.includes(id)) {
              //checks之前选中的菜单id
              this.$refs.tree.setChecked(id, false);
            }
          });
        });
      });
    },
  },
};
</script>

<style>
</style>