<template>
  <div>
    <!-- http://localhost:8080/manager才能看到主界面 -->

    <el-row :gutter="20">
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="sortname"
            placeholder="请输入类别名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px; width: 200px; margin-top: 30px"
          />
          <el-button type="primary" class="ml-5" @click="loadSort"
            >搜索</el-button
          >
        </div>
        <div class="head-container">
          <el-tree
            :data="sortData"
            :props="props"
            :expand-on-click-node="false"
            ref="tree"
            @node-click="handleNodeClick"
          >
            <span class="custom-tree-node" slot-scope="{ data }">
              <span>{{ data.sortname }}</span>
            </span>
          </el-tree>
        </div>
      </el-col>

      <el-col :span="20" :xs="24">
        <div style="margin: 30px 0">
          <el-input
            type="text"
            suffix-icon="el-icon-search"
            style="width: 200px"
            placeholder="请输入商品名称"
            v-model="name"
          ></el-input>
          <el-input
            type="text"
            v-if="false"
            suffix-icon="el-icon-search"
            style="width: 200px"
            placeholder="请输入名称"
            v-model="psort"
          ></el-input>

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
            action="http://localhost:9090/product/import"
            :show-file-list="false"
            accept="xlsx"
            style="display: inline-block"
            :on-success="handleExcelImportSuccess"
          >
            <el-button type="info" class="ml-5"
              >导入<i class="el-icon-bottom"></i
            ></el-button>
          </el-upload>
          <el-button type="info" style="margin-left: 5px" @click="exp"
            >商品信息导出<i class="el-icon-top"></i
          ></el-button>
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
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="花材编号" width="80">
          </el-table-column>
          <el-table-column prop="name" label="花材名称"> </el-table-column>
          <!-- <el-table-column prop="psort" label="所属类别" >
        </el-table-column>
         <el-table-column prop="sort" label="所属类别" >
        </el-table-column> -->
          <el-table-column prop="proCost" label="进价"> </el-table-column>
          <el-table-column prop="proPrice" label="售价"> </el-table-column>
          <el-table-column prop="proStock" label="库存"> </el-table-column>
          <el-table-column prop="proFrom" label="供应商"> </el-table-column>
          <el-table-column prop="proSales" label="销量"> </el-table-column>
          <el-table-column label="操作" width="280" align="center">
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
      </el-col>
    </el-row>
    <!-- 弹窗1 -->
    <el-dialog
      title="新增或编辑花材信息"
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
        <el-form-item label="花材名称" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所属类别" prop="psort">
          <el-select
            clearable
            v-model="form.psort"
            placeholder="请选择类别"
            style="width: 100%"
          >
            <el-option
              v-for="item in sorts"
              :key="item.id"
              :label="item.sortname"
              :value="item.id"
            >
              <!-- <i :class="item.id" />{{item.sortName}} -->
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="进价" prop="proCost">
          <el-input v-model.number="form.proCost" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="售价" prop="proPrice">
          <el-input
            v-model.number="form.proPrice"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="供应商" prop="proFrom">
          <el-select
            clearable
            v-model="form.proFrom"
            placeholder="请选择供应商"
            style="width: 100%"
          >
            <el-option
              v-for="item in suppliers"
              :key="item.id"
              :label="item.name"
              :value="item.name"
            >
              <!-- <i :class="item.id" />{{item.sortName}} -->
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="销量" prop="proSales">
          <el-input v-model="form.proSales" autocomplete="off"></el-input>
        </el-form-item>
        <!-- <el-form-item label="活动区域" :label-width="formLabelWidth">
      <el-select v-model="form.region" placeholder="请选择活动区域">
        <el-option label="区域一" value="shanghai"></el-option>
        <el-option label="区域二" value="beijing"></el-option>
      </el-select>
    </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Flower",
  data() {
    let VaildproCost = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("进价不能为空"));
      } else {
        if (!Number.isInteger(value)) {
          callback(new Error("请输入数字值"));
        } else {
          callback();
        }
      }
    };
    let VaildproPrice = (rule1, value1, callback) => {
      if (!value1) {
        return callback(new Error("售价不能为空"));
      }
      if (!Number.isInteger(value1)) {
        callback(new Error("请输入数字值"));
      } else {
        callback();
      }
    };
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      sortData: [],
      props: {
        label: "name",
      },
      name: "",
      sortname: "",
      psort: "",
      sorts: [],
      suppliers: [],
      rules: {
        name: [
          //与prop="adname"对应
          { required: true, message: "名称不能为空", trigger: "blur" },
        ],
        psort: [
          {
            required: true,
            message: "类别不能为空",
            trigger: ["blur", "change"],
          },
        ],
        proCost: [{ required: true, validator: VaildproCost, trigger: "blur" }],
        proPrice: [
          { required: true, validator: VaildproPrice, trigger: "blur" },
        ],
        proFrom: [
          {
            required: true,
            message: "供应商不能为空",
            trigger: ["blur", "change"],
          },
        ],
      },

      dialogFormVisible: false,
      // menuDialogVis:false,
      form: {}, //添加的弹出框
      multipleSelection: [],
      headerBg: "headerBg",
      manager: localStorage.getItem("manager")
        ? JSON.parse(localStorage.getItem("manager"))
        : {},
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
        .get("/product/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            id: this.id,
            name: this.name,
            psort: this.psort,
          },
        })
        .then((res) => {
          this.tableData = res.data.records;
          this.total = res.data.total;
        });

      this.request.get("/sort").then((res) => {
        this.sorts = res.data;
      });
      this.request.get("/supplier/").then((res) => {
        this.suppliers = res.data;
      });
      //请求菜单数据
      this.request.get("/sort").then((res) => {
        this.sortData = res.data;
        //把后台返回的菜单数据处理成id数组
        this.expends = this.sortData.map((v) => v.id);
      });
    },
    loadSort() {
      //请求分页查询数据
      this.request
        .get("/sort/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            id: this.id,
            sortname: this.sortname,
          },
        })
        .then((res) => {
          this.sortData = res.data.records;
          //把后台返回的菜单数据处理成id数组
          this.expends = this.sortData.map((v) => v.id);
        });
    },
    //节点单击事件
    handleNodeClick(data) {
      this.psort = data.id;
      this.load();
    },
    //保存数据
    //保存数据
    save() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request.post("/product", this.form).then((res) => {
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
      this.request.delete("/product/" + id).then((res) => {
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
      this.request.post("/product/del/batch", ids).then((res) => {
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
      this.psort = "";
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
    exp() {
      window.open("http://localhost:9090/product/export");
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
</style>