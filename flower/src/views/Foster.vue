<template>
  <div style="width: 100%; height: 100%">
    <!-- 搜索表单 -->
    <el-form
      :inline="true"
      label-width="100px"
      size="small"
      class="demo-form-inline"
    >
      <el-form-item label="寄养人手机号">
        <el-input
          v-model="phone"
          style="width: 300px"
          clearable
          placeholder="请输入手机号"
        >
        </el-input>
      </el-form-item>

      <el-form-item label="寄养植物：">
        <el-input
          v-model="fosName"
          style="width: 300px"
          clearable
          placeholder="请输寄养植物名称"
        >
        </el-input>
      </el-form-item>

      <el-form-item label="开始时间：">
        <el-date-picker
          v-model="startTime"
          align="right"
          type="date"
          placeholder="选择开始日期"
          :picker-options="pickerOptions1"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item label="操作人：">
        <el-select
          v-model="fosOper"
          clearable
          filterable
          placeholder="选择操作人"
          style="width: 300px"
        >
          <el-option
            v-for="item in sales"
            :key="item.adid"
            :label="item.adname"
            :value="item.adname"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="支付状态">
        <el-select
          v-model="fosStatus"
          clearable
          filterable
          placeholder="选择支付状态"
          style="width: 300px"
        >
          <el-option
            v-for="item in agentData"
            :key="item.id"
            :label="item.name"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button
          style="margin-left: 10px"
          type="primary"
          @click="load"
          icon="Search"
          >查询
        </el-button>
        <el-button
          style="margin-left: 5px"
          type="info"
          @click="reset"
          icon="Search"
          >重置
        </el-button>
      </el-form-item>
      <div>
        <el-button icon="Plus" @click="handleAddbuy" style="margin-left: 10px"
          ><i class="el-icon-circle-plus-outline"></i>新增寄养信息</el-button
        >
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
      </div>
    </el-form>

    <!-- 数据展示 -->
    <el-table
      :data="tableData"
      border
      stripe
      :header-cell-class-name="'headerBg'"
      style="margin-top: 20px; width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column style="color: blue" prop="id" label="编号" width="220">
      </el-table-column>
      <el-table-column prop="sender" label="寄养人" width="220">
      </el-table-column>
      <el-table-column prop="phone" label="联系方式" width="220">
      </el-table-column>
      <el-table-column prop="fosName" label="寄养植物" width="220">
      </el-table-column>

      <el-table-column prop="fosVariety" label="品种" width="220">
      </el-table-column>
      <el-table-column prop="fosStatus" label="支付状态" width="220">
      </el-table-column>
      <el-table-column prop="fosOper" label="操作人" width="220">
      </el-table-column>

      <el-table-column fixed="right" label="操作" width="300">
        <template slot-scope="scope">
          <el-button type="primary" @click="lookapply(scope.row)"
            ><i class="el-icon-view"></i> 查看
          </el-button>
          <el-button
            type="success"
            disabled
            v-if="scope.row.fosStatus === '已支付'"
            ><i class="el-icon-view"></i> 已结算
          </el-button>
          <el-button
            type="success"
            @click="handleEdit(scope.row)"
            v-if="scope.row.fosStatus === '未支付'"
            ><i class="el-icon-view"></i> 结算
          </el-button>
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

    <!-- 页码 -->
    <div style="padding: 10px 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total1"
      >
      </el-pagination>
    </div>

    <!-- -----------------------------------------弹出框1--------------------------------------------------------------------- -->
    <!-- 新增寄养信息 -->
    <el-dialog
      :visible.sync="dialogFormVisible"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">新增寄养信息</div>
      </template>
      <el-form
        :model="form"
        :inline="true"
        status-icon
        :rules="rules"
        ref="ruleForm"
        size="small"
        class="demo-form-inline"
      >
        <el-form-item label="寄养人" style="margin-left: 50px" prop="sender">
          <el-input
            v-model="form.sender"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="联系方式" style="margin-left: 50px" prop="phone">
          <el-input
            v-model="form.phone"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>

        <el-form-item
          label="寄养植物名称"
          style="margin-left: 10px"
          prop="fosName"
        >
          <el-input
            v-model="form.fosName"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="品种" style="margin-left: 75px" prop="fosVariety">
          <el-input
            v-model="form.fosVariety"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>

        <!-- 头像上传 -->
        <el-upload
          class="avatar-uploader"
          :action="'http://localhost:9090/file/upload'"
          :show-file-list="true"
          :on-success="handleAvatarSuccess"
        >
          <img v-if="imageUrl" :src="imageUrl" class="avatar" />
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>

        <el-form-item
          label="开始日期"
          style="margin-left: 40px; margin-top: 30px"
          prop="fosStarttime"
        >
          <el-date-picker
            style="width: 300px"
            v-model="form.fosStarttime"
            align="right"
            type="date"
            placeholder="选择开始日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item
          label="结束日期"
          style="margin-left: 50px; margin-top: 30px"
          prop="fosEndtime"
        >
          <el-date-picker
            style="width: 300px"
            v-model="form.fosEndtime"
            align="right"
            type="date"
            placeholder="选择结束日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item
          label="单天价格"
          class="redss"
          style="margin-left: 40px"
          prop="fosPrice"
        >
          <el-input
            v-model="form.fosPrice"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>

        <el-form-item
          label="总价"
          class="redss"
          style="margin-left: 75px"
          prop="fosTotal"
        >
          <el-input
            v-model="form.fosTotal"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="养护备注" style="margin-left: 45px">
          <el-input
            v-model="remarkss"
            style="width: 730px"
            type="textarea"
            @input="onInput"
            :rows="4"
            placeholder="请输入备注信息"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 结算 -->
    <el-dialog :visible.sync="vis1" :show-close="dd" :close-on-click-modal="aa">
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">结算</div>
      </template>
      <el-form
        :model="form"
        :inline="true"
        status-icon
        :rules="rules"
        ref="ruleForm"
        size="small"
        class="demo-form-inline"
      >
        <el-form-item label="寄养人" style="margin-left: 50px">
          <el-input
            disabled
            v-model="form.sender"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="联系方式" style="margin-left: 50px">
          <el-input
            disabled
            v-model="form.phone"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="寄养植物名称" style="margin-left: 10px">
          <el-input
            disabled
            v-model="form.fosName"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="品种" style="margin-left: 75px">
          <el-input
            disabled
            v-model="form.fosVariety"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>

        <!-- 头像上传 -->

        <img :src="form.fosUrl" class="avatar3" />

        <el-form-item
          label="开始日期"
          style="margin-left: 40px; margin-top: 30px"
        >
          <el-date-picker
            disabled
            style="width: 300px"
            v-model="form.fosStarttime"
            align="right"
            type="date"
            placeholder="选择开始日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item
          label="结束日期"
          style="margin-left: 50px; margin-top: 30px"
          prop="fosEndtime1"
        >
          <el-date-picker
            style="width: 300px"
            v-model="form.fosEndtime1"
            align="right"
            type="date"
            placeholder="选择结束日期"
            :picker-options="pickerOptions"
            @change="payments"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item label="单天价格" class="redss" style="margin-left: 40px">
          <el-input
            disabled
            v-model="form.fosPrice"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>

        <el-form-item
          label="总价"
          class="redss"
          style="margin-left: 75px"
          prop="fosTotal1"
        >
          <el-input v-model="fosTotal1" autocomplete="off" style="width: 300px">
          </el-input>
        </el-form-item>

        <el-form-item label="养护备注:" style="margin-left: 45px">
          <span>{{ form.fosNote }}</span>
        </el-form-item>
        <br />
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save1">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看寄养信息 -->
    <el-dialog :visible.sync="vis" :show-close="dd" :close-on-click-modal="aa">
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">查看寄养信息</div>
      </template>
      <el-form
        :model="form"
        :inline="true"
        status-icon
        :rules="rules"
        ref="ruleForm"
        size="small"
        class="demo-form-inline"
      >
        <span class="ml_fs"> 寄养人: </span>
        <span class="fs" style="font-size: 16px">{{ form.sender }}</span>

        <span class="ml_fs"> 联系方式: </span>
        <span class="fs">{{ form.phone }}</span>

        <span class="ml_fs"> 寄养植物名称: </span>
        <span class="fs">{{ form.fosName }}</span>

        <span class="ml_fs"> 品种: </span>
        <span class="fs"> {{ form.fosVariety }}</span>
        <br />
        <br />
        <span class="ml_fs"> 图片: </span>
        <img
          :src="form.fosUrl"
          alt=""
          class="avatar3"
          style="margin-top: 20px; maegin-left: 60px"
        />
        <br />
        <span class="ml_fs"> 开始日期: </span>
        <span class="fs">{{ form.fosStarttime | formatDate }}</span>

        <span class="ml_fs"> 结束日期: </span>
        <span class="fs">{{ form.fosEndtime | formatDate }}</span>
        <br />
        <br />
        <span class="ml_fs"> 单天价格: </span>
        <span class="fs">{{ form.fosPrice }}</span>
        <br />
        <br />
        <span class="ml_fs"> 总价: </span>
        <span class="fs">{{ form.fosTotal }}</span>
        <br />
        <br />
        <span class="ml_fs"> 养护备注: </span>
        <span class="fs">{{ form.fosNote }}</span>
        <br />
        <br />
        <span class="ml_fs"> 支付状态: </span>

        <span class="fs">{{ form.fosStatus }}</span>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="vis = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { formatDate } from "@/common/date.js"; // 在组件中引用date.js
export default {
  name: "Foster",
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
      form: {},
      dayCount: 0,
      fosTotal1: 0,
      buy: "",
      phone: "",
      fosName: "",
      startTime: "",
      fosOper: "",
      fosStatus: "",
      remarkss: "",
      finalpay: "",
      status: "未支付",
      status1: "已支付",
      tableData: [],
      agentData: [
        {
          value: "未支付",
          label: "未支付",
        },
        {
          value: "已支付",
          label: "已支付",
        },
      ],
      imageUrl: "",
      total1: 0,
      pageNum: 1,
      pageSize: 5,
      adname: localStorage.getItem("manager")
        ? JSON.parse(localStorage.getItem("manager"))
        : null,
      dialogFormVisible: false,
      vis: false, //查看审核单弹出框
      vis1: false, //修改审核单弹出框
      vis2: false, //审核弹出框（只有管理员可以）
      dd: true,
      aa: true,
      full: true,
      sales: [],
      options: [],
      num: 1,
      value: [],
      form1: {
        buynewid: "33",
        buyStatus: "未通过",
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < new Date().getTime() - 86400000;
        },
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              picker.$emit("pick", new Date());
            },
          },
          {
            text: "昨天",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "一周前",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
        ],
      },
      pickerOptions1: {
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              picker.$emit("pick", new Date());
            },
          },
          {
            text: "昨天",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "一周前",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
        ],
      },

      rules: {
        sender: [
          { required: true, message: "寄养人不能为空", trigger: "blur" },
        ],
        phone: [
          { required: true, message: "联系方式不能为空", trigger: "blur" },
          {
            validator: validCode,
            message: "请输入正确手机号",
            trigger: "blur",
          },
        ],
        fosName: [
          {
            required: true,
            message: "寄养植物不能为空",
            trigger: ["blur", "change"],
          },
        ],
        fosVariety: [
          {
            required: true,
            message: "品种不能为空",
            trigger: ["blur", "change"],
          },
        ],
        fosStarttime: [
          {
            required: true,
            message: "开始日期不能为空",
            trigger: ["blur", "change"],
          },
        ],
        fosEndtime1: [
          {
            required: true,
            message: "结束日期不能为空",
            trigger: ["blur", "change"],
          },
        ],
        fosPrice: [
          {
            required: true,
            message: "单价不能为空",
            trigger: ["blur", "change"],
          },
        ],
        fosStatus: [
          {
            required: true,
            message: "状态不能为空",
            trigger: ["blur", "change"],
          },
        ],
      },
    };
  },
  created() {
    //请求分页查询数据
    this.load();
    var aData = new Date();

    this.nowtime =
      aData.getFullYear() +
      "-" +
      (aData.getMonth() + 1) +
      "-" +
      aData.getDate();
  },
  filters: {
    // 时间格式自定义 只需把字符串里面的改成自己所需的格式
    formatDate(time) {
      let date = new Date(time);
      return formatDate(date, "yyyy年MM月dd日");
    },
  },
  methods: {
    load() {
      //请求分页查询数据
      this.request
        .get("/foster/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            phone: this.phone,
            fosName: this.fosName,
            startTime: this.startTime,
            fosOper: this.fosOper,
            fosStatus: this.fosStatus,
          },
        })
        .then((res) => {
          this.tableData = res.data.records;
          this.total1 = res.data.total;
        });

      this.request
        .get("/manager/flagissale", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          },
        })
        .then((res) => {
          this.sales = res.data.records;
          this.total = res.data.total;
        });
    },

    //保存数据
    save() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request
            .post(
              "/foster",
              (this.form1 = {
                sender: this.form.sender,
                phone: this.form.phone,
                fosName: this.form.fosName,
                fosVariety: this.form.fosVariety,
                fosUrl: this.imageUrl,
                fosNote: this.remarkss,
                fosStarttime: this.form.fosStarttime,
                fosEndtime: this.form.fosEndtime,
                fosPrice: this.form.fosPrice,
                fosTotal: this.form.fosTotal,
                fosStatus: this.status,
                fosOper: this.adname.adname,
              })
            )
            .then((res) => {
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
    save1() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request
            .post(
              "/foster",
              (this.form1 = {
                id: this.form.id,
                fosEndtime: this.form.fosEndtime1,
                fosTotal: this.fosTotal1,
                fosStatus: this.status1,
              })
            )
            .then((res) => {
              if (res.code === "200") {
                this.$message.success("结算成功");
                this.vis1 = false;
                this.load();
              } else {
                this.$message.error("结算失败");
              }
            });
        }
      });
    },
    payments() {
      let day =
        new Date(this.form.fosEndtime1).getTime() -
        new Date(this.form.fosStarttime).getTime(); //日期转时间戳
      this.dayCount = Math.floor(day / 86400000); //时间戳获取天数
      console.log(this.dayCount + "寄养天数");

      // this.finalpay=this.num1 - this.num2
      this.fosTotal1 = this.dayCount * this.form.fosPrice;
      console.log(this.fosTotal1);
    },
    //删除
    handleDel(id) {
      this.request.delete("/foster/" + id).then((res) => {
        if (res.code === "200") {
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error("删除失败");
        }
      });
    },
    onInput() {
      this.$forceUpdate();
    },
    lookapply(row) {
      this.vis = true;
      this.form = JSON.parse(JSON.stringify(row)); //可以解决编辑未确认就改变的问题
    },
    handleEdit(row) {
      this.vis1 = true;
      this.form = JSON.parse(JSON.stringify(row)); //可以解决编辑未确认就改变的问题
    },
    //新增弹窗  新增采购单
    handleAddbuy() {
      this.dialogFormVisible = true;
      this.form = {};
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },

    //删除
    handleDel(id) {
      this.request.delete("/foster/" + id).then((res) => {
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
      this.request.post("/foster/del/batch", ids).then((res) => {
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
      this.phone = "";
      this.fosName = "";
      this.startTime = "";
      this.fosOper = "";
      this.fosStatus = "";
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
  },
};
</script>


<style scoped>
.ml_fs {
  margin-left: 50px;
  font-size: 16px;
}
.fs {
  font-size: 16px;
}
.headerr {
  width: 100%;
  height: 40px;
  background-color: #eee;
}

.headerBg {
  background: #eee !important;
}
/* 审核按钮 */
.success {
  width: 90px;
  height: 50px;
  font-size: 13px;
}
.success:focus {
  background-color: rgb(0, 149, 255);
  color: #fff;
}

.avatar-uploader {
  width: 138px;
  margin-left: 160px;
  margin-bottom: 10px;
  border: 1px dashed #d9d9d9;
}
.avatar-uploader .el-upload {
  border: 1px solid #622121;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
.avatar3 {
  width: 138px;
  height: 138px;
  display: block;
  margin-left: 100px;
}
</style>