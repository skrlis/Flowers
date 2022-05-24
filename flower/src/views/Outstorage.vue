<template>
  <div style="width: 100%; height: 100%">
    <!-- 搜索表单 -->
    <el-form
      :inline="true"
      label-width="100px"
      size="small"
      class="demo-form-inline"
    >
      <el-form-item label="出库单号：">
        <el-input
          v-model="buyid"
          style="width: 300px"
          clearable
          placeholder="请输入出库单号"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="开始时间：">
        <el-date-picker
          v-model="startTime"
          align="right"
          type="date"
          placeholder="选择日期"
          :picker-options="pickerOptions"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item label="结束时间：">
        <el-date-picker
          v-model="endTime"
          align="right"
          type="date"
          placeholder="选择日期"
          :picker-options="pickerOptions"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item label="客户：">
        <el-select
          v-model="outCustomer"
          clearable
          filterable
          placeholder="选择客户"
          style="width: 300px"
        >
          <el-option
            v-for="item in customers"
            :key="item.id"
            :label="item.cusName"
            :value="item.cusName"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="操作人：">
        <el-select
          v-model="buyApply"
          clearable
          filterable
          placeholder="选择操作人"
          style="width: 300px"
        >
          <el-option
            v-for="item in salers"
            :key="item.adid"
            :label="item.adname"
            :value="item.adname"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button
          style="margin-left: 50px"
          type="primary"
          @click="load"
          icon="Search"
          >查询
        </el-button>
        <el-button type="info" @click="reset">重置</el-button>
      </el-form-item>
      <div>
        <el-button icon="Plus" @click="handleAddbuy" style="margin-left: 10px"
          ><i class="el-icon-circle-plus-outline"></i>新增出库单</el-button
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
        <el-button type="primary" class="ml-5" @click="exp"
          >导出<i class="el-icon-top"></i
        ></el-button>
      </div>
    </el-form>

    <el-input v-if="false" v-model.number="this.num1"></el-input>
    <el-input v-if="false" v-model.number="this.num2"></el-input>
    <el-input v-if="false" v-model.number="this.num3"></el-input>

    <!-- 数据展示 -->
    <el-table
      :data="tableData"
      border
      stripe
      :header-cell-class-name="'headerBg'"
      style="margin-top: 20px; width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="出库单号：">
              <span>{{ props.row.outId }}</span>
            </el-form-item>
            <el-form-item label="出库商品：">
              <span>{{ props.row.proname }}</span>
            </el-form-item>
            <el-form-item label="出库日期：">
              <span>{{ props.row.outDate | formatDate }}</span>
            </el-form-item>
            <el-form-item label="供应商：">
              <span>{{ props.row.outSupplier }}</span>
            </el-form-item>
            <el-form-item label="客户：">
              <span>{{ props.row.outCustomer }}</span>
            </el-form-item>
            <el-form-item label="操作人：">
              <span>{{ props.row.outOper }}</span>
            </el-form-item>
            <el-form-item label="出库数量：">
              <span>{{ props.row.outNum }}</span>
            </el-form-item>
            <el-form-item label="数量单位：">
              <span>{{ props.row.outUnit }}</span>
            </el-form-item>
            <el-form-item label="单价：">
              <span>{{ props.row.outPrice }}</span>
            </el-form-item>
            <el-form-item label="总价：">
              <span>{{ props.row.outTotal }}</span>
            </el-form-item>
            <el-form-item label="利润：">
              <span>{{ props.row.outProfit }}</span>
            </el-form-item>
            <el-form-item label="已支付：">
              <span>{{ props.row.outPaid }}</span>
            </el-form-item>
            <el-form-item label="状态：">
              <span>{{ props.row.status }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="outId" label="出库单号"> </el-table-column>
      <el-table-column prop="proname" label="出库产品"> </el-table-column>
      <el-table-column prop="outSupplier" label="供应商"> </el-table-column>
      <el-table-column prop="outOper" label="操作人"> </el-table-column>
      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            @click="putup(scope.row)"
            v-if="scope.row.status === '未提交'"
            >提交<i class="el-icon-edit"></i
          ></el-button>
          <el-button
            type="warning"
            disabled
            v-if="scope.row.status === '已出库'"
            >已出库</el-button
          >
          <el-button
            type="primary"
            @click="reduceStock(scope.row)"
            v-if="scope.row.status === '已提交'"
            >出库<i class="el-icon-edit"></i
          ></el-button>
          <el-button
            type="success"
            @click="handleEdit(scope.row)"
            v-if="scope.row.status === '未提交'"
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
    <!-- 新增出库单 -->
    <el-dialog
      :visible.sync="dialogFormVisible"
      :fullscreen="full"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">新增出库单</div>
      </template>
      <el-form
        :model="form1"
        :inline="true"
        status-icon
        :rules="rules"
        ref="ruleForm"
        size="small"
        class="demo-form-inline"
      >
        <el-form-item
          label="出库单编号:"
          class="redss"
          style="margin-left: 50px"
          prop="salenewid"
        >
          <span>{{ this.salenewid }}</span>
        </el-form-item>
        <el-form-item
          label="出库日期:"
          style="margin-left: 70px"
          prop="saleDate"
        >
          <el-date-picker
            v-model="form1.saleDate"
            align="right"
            type="date"
            placeholder="选择出库日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item
          label="操作人:"
          style="margin-left: 70px"
          prop="saleApply"
        >
          <el-select
            v-model="form1.saleApply"
            clearable
            filterable
            placeholder="选择操作人"
            style="width: 300px"
          >
            <el-option
              v-for="item in salers"
              :key="item.adid"
              :label="item.adname"
              :value="item.adname"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="供应商:"
          style="margin-left: 70px"
          prop="saleSupplier"
        >
          <el-select
            v-model="form1.saleSupplier"
            clearable
            filterable
            placeholder="选择供应商"
            style="width: 300px"
          >
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.name"
              :value="item.name"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="客户:"
          style="margin-left: 70px; margin-top: 20px"
          prop="buyCustomer"
        >
          <el-select
            v-model="form1.buyCustomer"
            clearable
            filterable
            placeholder="选择客户"
            style="width: 300px"
          >
            <el-option
              v-for="item in customers"
              :key="item.id"
              :label="item.cusName"
              :value="item.cusName"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <div class="personal-mybuluo-head">
          <div class="personal-mybuluo-wording">
            出库商品明细
            <el-button type="primary" @click="fromBuy">
              <i class="el-icon-position"></i>从出库通知单导入</el-button
            >
          </div>
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>
        <div>
          <div style="margin-top: 200px" class="headerr">
            <div style="width: 360px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">出库商品</h3>
            </div>
            <div
              v-if="this.form1.proname1"
              style="width: 360px; height: 40px; float: left"
            >
              <h3 style="padding-top: 10px; font-size: 15px">出库数量</h3>
            </div>
            <div style="width: 360px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">数量单位</h3>
            </div>
            <div style="width: 200px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">单价</h3>
            </div>
            <div style="width: 200px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">总价格</h3>
            </div>
            <div style="width: 200px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">利润</h3>
            </div>
            <div style="width: 200px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">已付款</h3>
            </div>
          </div>
          <div style="width: 100%; height: 50px; border-bottom: 1px solid #eee">
            <el-form-item style="width: 300px; float: left" prop="proname1">
              <el-select
                style="width: 300px; margin-top: 10px; float: left"
                @change="getkucun"
                v-model="form1.proname1"
                placeholder="请选择出库商品"
              >
                <!-- //都是数据库的字段 -->
                <el-option
                  v-for="item in productData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-input v-if="false" v-model="form1.prostock"></el-input>
            <el-form-item
              v-if="this.form1.proname1"
              style="width: 360px; float: left"
              prop="saleNum"
            >
              <el-input-number
                @change="maxalert1"
                type="number"
                placeholder="出库数量"
                style="width: 360px; margin-top: 10px; float: left"
                v-model="form1.saleNum"
              ></el-input-number>
            </el-form-item>
            <el-form-item style="width: 300px; float: left" prop="buyUnit">
              <el-cascader
                v-model="form1.buyUnit"
                placeholder="选择数量单位"
                style="width: 300px; margin-top: 10px; float: left"
                ref="cascaderAddr"
                :options="menuData"
                :props="{
                  expandTrigger: 'click',
                  checkStrictly: true,
                  value: 'unitsid',
                  label: 'unitname',
                }"
                @change="getbuyunit"
                @blur="getAllMenuTree"
                clearable
              >
              </el-cascader>
            </el-form-item>
            <el-form-item style="width: 200px; float: left" prop="salePrice">
              <el-input-number
                @change="calculate1"
                type="number"
                v-model="form1.salePrice"
                placeholder="单价"
                style="width: 200px; margin-top: 10px; float: left"
              ></el-input-number>
            </el-form-item>
            <el-form-item
              style="
                width: 200px;
                height: 50px;
                margin-top: 10px;
                margin-left: 10px;
                text-align: center;
                float: left;
              "
              prop="saleTotal"
            >
              <span>{{ this.form1.saleTotal }}</span>
            </el-form-item>
            <el-form-item
              style="
                width: 200px;
                height: 50px;
                margin-top: 10px;
                text-align: center;
                float: left;
                margin-left: -20px;
              "
              prop="saleTotal"
            >
              <span>{{ this.form1.outProfit }}</span>
            </el-form-item>
            <el-form-item
              prop="outPaid"
              style="width: 200px; margin-left: 30px; margin-top: 10px"
            >
              <el-input
                placeholder="请输入已支付金额"
                v-model="form1.outPaid"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </div>
        </div>
        <div class="personal-mybuluo-head" style="margin-top: -50px">
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>
        <br />
      </el-form>

      <div slot="footer" style="margin-top: 100px" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <!-- -----------------------------------------弹出框222222222222222222222222--------------------------------------------------------------------- -->
    <!-- 编辑出库单 -->
    <el-dialog
      :visible.sync="vis"
      :fullscreen="full"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">修改出库单</div>
      </template>
      <el-form
        :model="form1"
        :inline="true"
        status-icon
        size="small"
        :rules="rules"
        ref="ruleForm"
        class="demo-form-inline"
      >
        <el-form-item
          label="通知单编号"
          class="redss"
          style="margin-left: 50px"
        >
          <span>{{ form1.outId }}</span>
        </el-form-item>
        <el-form-item label="申请日期" prop="outDate" style="margin-left: 70px">
          <el-date-picker
            v-model="form1.outDate"
            align="right"
            type="date"
            placeholder="选择日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="操作人" prop="outOper" style="margin-left: 70px">
          <el-select
            v-model="form1.outOper"
            clearable
            filterable
            placeholder="选择操作人"
            style="width: 300px"
          >
            <el-option
              v-for="item in salers"
              :key="item.adid"
              :label="item.adname"
              :value="item.adname"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="客户" prop="outCustomer" style="margin-left: 70px">
          <el-select
            v-model="form1.outCustomer"
            clearable
            filterable
            placeholder="选择客户"
            style="width: 300px"
          >
            <el-option
              v-for="item in customers"
              :key="item.id"
              :label="item.cusName"
              :value="item.cusName"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <div class="personal-mybuluo-head">
          <div class="personal-mybuluo-wording">出库单明细</div>
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>

        <div>
          <div style="margin-top: 200px" class="headerr">
            <div style="width: 260px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">出库商品</h3>
            </div>
            <div style="width: 260px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">供应商</h3>
            </div>
            <div style="width: 260px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">库存量</h3>
            </div>
            <div style="width: 260px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">出库数量</h3>
            </div>
            <div style="width: 260px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">数量单位</h3>
            </div>
            <div style="width: 260px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">单价</h3>
            </div>
            <div style="width: 260px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">总价格</h3>
            </div>
          </div>
          <div style="width: 100%; height: 50px; border-bottom: 1px solid #eee">
            <el-form-item
              style="width: 230px; margin-top: 10px; float: left"
              prop="proname"
            >
              <el-select
                style="width: 230px; float: left"
                @change="getkucun"
                v-model="form1.proname"
                placeholder="请选择出库商品"
              >
                <!-- //都是数据库的字段 -->
                <el-option
                  v-for="item in productData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-input v-if="false" v-model="form1.outName"></el-input>
            <el-form-item style="width: 230px; float: left" prop="outSupplier">
              <el-select
                style="width: 230px; margin-top: 10px; float: left"
                v-model="form1.outSupplier"
                placeholder="请选择供应商"
              >
                <el-option
                  v-for="item in options"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name"
                >
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item style="width: 230px; margin-top: 10px; float: left">
              <el-input v-model="form1.prostock"></el-input>
              <!-- <span>{{this.proStock1}}</span> -->
            </el-form-item>

            <el-form-item style="width: 230px; float: left" prop="outNum">
              <el-input-number
                @change="maxalert"
                type="number"
                placeholder="出库数量"
                style="width: 230px; margin-top: 10px; float: left"
                v-model="form1.outNum"
              ></el-input-number>
            </el-form-item>
            <el-form-item style="width: 230px; margin-top: 10px; float: left">
              <span style="margin-left: 100px; margin-top: 40px">{{
                form1.outUnit
              }}</span>
            </el-form-item>
            <el-form-item style="width: 230px; float: left" prop="outPrice">
              <el-input-number
                @change="calculate"
                type="number"
                v-model="form1.outPrice"
                placeholder="单价"
                style="width: 260px; margin-top: 10px; float: left"
              ></el-input-number>
            </el-form-item>
            <el-form-item style="width: 200px; float: left" prop="outTotal">
              <el-input
                placeholder="总价"
                v-model="form1.outTotal"
                autocomplete="off"
                style="
                  width: 290px;
                  margin-top: 10px;
                  float: left;
                  margin-left: 30px;
                "
              ></el-input>
            </el-form-item>
          </div>
        </div>
        <div class="personal-mybuluo-head" style="margin-top: -50px">
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>
        <br />
        <el-form-item
          label="总利润:"
          class="redss"
          style="margin-left: -1550px; margin-top: 100px"
        >
          <span>{{ form1.outProfit }}</span>
        </el-form-item>
        <el-form-item
          label="已付款:"
          class="redss"
          style="margin-left: 50px; margin-top: 100px"
          prop="outPaid"
        >
          <el-input-number
            placeholder="已付款"
            style="width: 230px"
            v-model="form1.outPaid"
          ></el-input-number>
        </el-form-item>
      </el-form>

      <div slot="footer" style="margin-top: 50px" class="dialog-footer">
        <el-button @click="vis = false">取 消</el-button>
        <el-button type="primary" @click="save1">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 提交 -->
    <el-dialog
      class="vis3"
      :visible.sync="vis3"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">出库单</div>
      </template>
      <el-form :model="form1" status-icon size="small" class="demo-form-inline">
        <el-form-item
          label="出库单号："
          class="redss"
          style="margin-left: 50px"
        >
          <span>{{ this.form1.outId }}</span>
        </el-form-item>
        <el-form-item label="出库日期：" style="margin-left: 70px">
          <span>{{ this.form1.outDate | formatDate }}</span>
        </el-form-item>
        <el-form-item label="操作人：" style="margin-left: 70px">
          <span>{{ this.form1.outOper }}</span>
        </el-form-item>
        <el-form-item label="出库商品：" style="margin-left: 70px">
          <span>{{ this.form1.proname }}</span>
        </el-form-item>
        <el-form-item label="供应商：" style="margin-left: 70px">
          <span>{{ this.form1.outSupplier }}</span>
        </el-form-item>
        <el-form-item label="出库数量：" style="margin-left: 70px">
          <span>{{ this.form1.outNum }}</span>
        </el-form-item>

        <el-form-item label="数量单位：" style="margin-left: 70px">
          <span>{{ this.form1.outUnit }}</span>
        </el-form-item>
        <el-form-item label="售价：" style="margin-left: 70px">
          <span>{{ this.form1.outPrice }}</span>
        </el-form-item>
        <el-form-item label="总价：" style="margin-left: 70px">
          <span>{{ this.form1.outTotal }}</span>
        </el-form-item>
        <el-form-item label="利润：" style="margin-left: 70px">
          <span>{{ this.form1.outPrice }}</span>
        </el-form-item>
        <el-form-item label="已支付：" style="margin-left: 70px">
          <span>{{ this.form1.outPaid }}</span>
        </el-form-item>
        <el-form-item>
          <span
            ><img src="../assets/注意.png" style="width: 20px; height: 20px" />
            出库后不可修改，请再次确认！</span
          >
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="vis3 = false">取 消</el-button>
        <el-button type="primary" @click="updateStatus">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 从出库通知单导入  -->
    <el-dialog
      :visible.sync="vis1"
      :fullscreen="full"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">从出库通知单导入</div>
      </template>
      <el-form
        :model="form1"
        :inline="true"
        status-icon
        size="small"
        class="demo-form-inline"
      >
        <el-form-item
          label="出库单编号"
          class="redss"
          style="margin-left: 50px"
        >
          <el-input
            v-model="saleid2"
            autocomplete="off"
            style="width: 300px"
            placeholder="请输入出库通知单号"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="开始时间" style="margin-left: 70px">
          <el-date-picker
            v-model="startTime2"
            align="right"
            type="date"
            placeholder="选择开始日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" style="margin-left: 70px">
          <el-date-picker
            v-model="endTime2"
            align="right"
            type="date"
            placeholder="选择结束日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="申请人" style="margin-left: 70px">
          <el-select
            v-model="buyApply2"
            clearable
            filterable
            placeholder="选择申请人"
            style="width: 300px"
          >
            <el-option
              v-for="item in salers"
              :key="item.adid"
              :label="item.adname"
              :value="item.adname"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核人" style="margin-left: 70px">
          <el-select
            v-model="buyer2"
            clearable
            filterable
            placeholder="选择审核人"
            style="width: 300px"
          >
            <el-option
              v-for="item in admins"
              :key="item.adid"
              :label="item.adname"
              :value="item.adname"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            style="width: 100px; margin-left: 50px"
            type="primary"
            @click="load1"
            icon="Search"
            >查询</el-button
          >
          <el-button type="warning" @click="reset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 出库申请单数据展示 -->
      <el-table
        :data="sales"
        border
        stripe
        @cell-dblclick="cellDblClick"
        :header-cell-class-name="'headerBg'"
        style="margin-top: 20px; width: 100%"
      >
        <el-table-column
          style="color: blue"
          prop="buyid"
          label="采购单编号"
          width="220"
        >
          <template slot-scope="scope">
            <span style="color: #6ba0d6">{{ scope.row.saleId }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="proname" label="出库商品" width="220">
        </el-table-column>
        <el-table-column prop="saleSupplier" label="供应商" width="220">
        </el-table-column>
        <el-table-column prop="saleNum" label="出库数量" width="220">
        </el-table-column>
        <el-table-column prop="saleUnit" label="数量单位" width="220">
        </el-table-column>
        <el-table-column prop="salePrice" label="单价" width="220">
        </el-table-column>
        <el-table-column prop="saler" label="申请人" width="220">
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" @click="lookapply(scope.row)">
              <i class="el-icon-view"></i>查看</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 查看出库通知单单 -->
    <el-dialog
      :visible.sync="vis2"
      :fullscreen="full"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">查看出库通知单</div>
      </template>
      <el-form
        :model="form1"
        :inline="true"
        status-icon
        size="small"
        class="demo-form-inline"
      >
        <el-form-item
          label="通知单编号:"
          prop="buyid"
          class="redss"
          style="margin-left: 50px"
        >
          <span>{{ form1.saleId }}</span>
        </el-form-item>
        <el-form-item
          label="申请日期："
          prop="buyDate"
          style="margin-left: 270px"
        >
          <span>{{ form1.saleDate | formatDate }}</span>
        </el-form-item>
        <el-form-item
          label="申请人："
          prop="buyApply"
          style="margin-left: 270px"
        >
          <span>{{ form1.saler }}</span>
        </el-form-item>

        <div>
          <div style="margin-top: 100px" class="headerr">
            <div style="width: 440px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">出库商品</h3>
            </div>
            <div style="width: 440px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">供应商</h3>
            </div>
            <div style="width: 440px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">出库数量</h3>
            </div>
            <div style="width: 300px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">单价(元)</h3>
            </div>
          </div>
          <div style="width: 100%; height: 50px; border-bottom: 1px solid #eee">
            <span
              autocomplete="off"
              style="width: 440px; margin-top: 10px; float: left"
              >{{ form1.proname }}</span
            >
            <span
              autocomplete="off"
              style="width: 440px; margin-top: 10px; float: left"
              >{{ form1.saleSupplier }}</span
            >
            <span
              autocomplete="off"
              style="width: 440px; margin-top: 10px; float: left"
              >{{ form1.saleNum }} / {{ form1.saleUnit }}</span
            >
            <span
              autocomplete="off"
              style="width: 390px; margin-top: 10px; float: left"
              >{{ form1.salePrice }}</span
            >
          </div>

          <span
            autocomplete="off"
            style="width: 440px; margin-top: 10px; float: left"
            >总价：{{ form1.saleTotal }}</span
          >
          <span
            autocomplete="off"
            style="width: 440px; margin-top: 10px; float: left"
            >已付款：{{ form1.salePaid }}</span
          ><br />
          <span
            autocomplete="off"
            style="width: 440px; margin-top: 10px; float: left"
            >备注：{{ form1.applynote }}</span
          >
        </div>

        <div class="personal-mybuluo-head">
          <div class="personal-mybuluo-wording">审核结果</div>
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>

        <div style="margin-top: 150px">
          <el-form-item
            label="审核结果："
            prop="buyer"
            style="margin-left: 270px"
          >
            <span>{{ form1.results }}</span>
          </el-form-item>

          <el-form-item
            label="审核人："
            prop="buyer"
            style="margin-left: 270px"
          >
            <span>{{ form1.reviewer }}</span>
          </el-form-item>
          <el-form-item
            label="审核日期:"
            prop="buyer"
            style="margin-left: 270px"
          >
            <span>{{ form1.reviewdate }}</span> </el-form-item
          ><br />
          <el-form-item
            label="审核备注:"
            prop="buyer"
            style="margin-left: 270px"
          >
            <span>{{ form1.remarks }}</span>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="vis2 = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { formatDate } from "@/common/date.js"; // 在组件中引用date.js
export default {
  name: "Outstorage",
  data() {
    return {
      form: {},
      sales: [],
      region: "全部",
      queryForm: {},
      formData: {
        tableData: [],
      },
      form1: {},
      buy: "",
      buyPrice1: "",
      proname: "",
      tableData: [],
      buyData: [],
      admins: [],
      salers: [],
      menuData: [],
      productData: [],
      customers: [],
      adname: "",
      buyid: "",
      buyData: "",
      buyApply: "",
      outCustomer: "",
      buyer: "",
      buyStatus: "",
      saleid2: "",
      buyApply2: "",
      buyer2: "",
      startTime2: "",
      endTime2: "",
      status1: "已出库",
      status: "已提交",
      status2: "未提交",
      startTime: "",
      endTime: "",
      num1: 0,
      num2: 0,
      num3: 0,
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
      vis3: false,
      dd: true,
      aa: true,
      full: true,
      buyers: [],
      options: [],
      num: 1,
      value: [],
      salenewid: "",
      agentData: [
        {
          value: "全部",
          label: "全部",
        },
        {
          value: "未审核",
          label: "未审核",
        },
        {
          value: "通过",
          label: "通过",
        },
        {
          value: "未通过",
          label: "未通过",
        },
      ],
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
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
      value1: "",
      value2: "",
      thsAreaCode: "",
      maxNum: "",
      proStock1: "",
      Status1: "已提交",
      rules: {
        saleDate: [
          { required: true, message: "日期不能为空", trigger: "blur" },
        ],
        saleApply: [
          {
            required: true,
            message: "操作人不能为空",
            trigger: ["blur", "change"],
          },
        ],
        saleSupplier: [
          {
            required: true,
            message: "供应商不能为空",
            trigger: ["blur", "change"],
          },
        ],
        buyCustomer: [
          {
            required: true,
            message: "客户不能为空",
            trigger: ["blur", "change"],
          },
        ],
        proname1: [
          {
            required: true,
            message: "出库商品不能为空",
            trigger: ["blur", "change"],
          },
        ],
        saleNum: [
          {
            required: true,
            message: "出库数量不能为空",
            trigger: ["blur", "change"],
          },
        ],
        buyUnit: [
          {
            required: true,
            message: "单位不能为空",
            trigger: ["blur", "change"],
          },
        ],
        salePrice: [
          {
            required: true,
            message: "单价不能为空",
            trigger: ["blur", "change"],
          },
        ],
        outPaid: [
          {
            required: true,
            message: "总支付不能为空",
            trigger: ["blur", "change"],
          },
        ],
        //编辑
        outDate: [
          {
            required: true,
            message: "日期不能为空",
            trigger: ["blur", "change"],
          },
        ],
        outOper: [
          {
            required: true,
            message: "操作人不能为空",
            trigger: ["blur", "change"],
          },
        ],
        outCustomer: [
          {
            required: true,
            message: "客户不能为空",
            trigger: ["blur", "change"],
          },
        ],
        proname: [
          {
            required: true,
            message: "出库商品不能为空",
            trigger: ["blur", "change"],
          },
        ],
        outSupplier: [
          {
            required: true,
            message: "供应商不能为空",
            trigger: ["blur", "change"],
          },
        ],
        outNum: [
          {
            required: true,
            message: "出库数量不能为空",
            trigger: ["blur", "change"],
          },
        ],
        outPrice: [
          {
            required: true,
            message: "售价不能为空",
            trigger: ["blur", "change"],
          },
        ],
        outTotal: [
          {
            required: true,
            message: "总价不能为空",
            trigger: ["blur", "change"],
          },
        ],
        outPaid: [
          {
            required: true,
            message: "总支付不能为空",
            trigger: ["blur", "change"],
          },
        ],
      },
    };
  },
  computed: {
    totalNum() {
      return this.num1 - this.num2;
    },
    totalSaleNum() {
      return +this.num2 + +this.num3;
    },
  },

  created() {
    this.getAllMenuTree();

    this.form1.salenewid =
      this.getProjectNum() + Math.floor(Math.random() * 10000);
    //请求分页查询数据
    this.load();
  },
  filters: {
    // 时间格式自定义 只需把字符串里面的改成自己所需的格式
    formatDate(time) {
      let date = new Date(time);
      return formatDate(date, "yyyy年MM月dd日");
    },
    // formatDate2(time) {
    //   let date = new Date(time);
    //   return formatDate(date, 'hh:mm:ss');
    // },
    // formatDate3(time) {
    //   let date = new Date(time);
    //   return formatDate(date, 'yyyy年MM月dd日 hh:mm:ss');
    // }
  },
  methods: {
    load() {
      this.request
        .get("/out/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            outId: this.buyid,
            outOper: this.buyApply,
            outCustomer: this.outCustomer,
            startTime: this.startTime,
            endTime: this.endTime,
          },
        })
        .then((res) => {
          this.tableData = res.data.records;
          this.total1 = res.data.total;
        });
      this.request.get("/supplier/").then((res) => {
        this.options = res.data;
      });
      this.request.get("/customer").then((res) => {
        this.customers = res.data;
      });
      //筛选出销售员
      this.request
        .get("/manager/flagissale", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          },
        })
        .then((res) => {
          this.salers = res.data.records;
        });
      this.request.get("/product/").then((res) => {
        this.productData = res.data;
      });

      this.request.get("/sort").then((res) => {
        this.sorts = res.data;
      });

      this.request
        .get("/sale/passsale", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          },
        })
        .then((res) => {
          this.sales = res.data.records;
        });
      //筛选出管理员
      this.request
        .get("/manager/flagisadmin", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          },
        })
        .then((res) => {
          this.admins = res.data.records;
        });
    },
    //编辑时
    getkucun(value) {
      this.productData.forEach((item) => {
        if (item.id === value) {
          console.log(item);
          this.form1.prostock = item.proStock;
          this.form1.outSupplier = item.proFrom;
          this.form1.flowerid = item.id;
          this.form1.proCost = item.proCost;
          this.form1.outName = item.id;
          if (this.form1.saleNum && this.form1.salePrice) {
            this.form1.saleTotal = this.form1.saleNum * this.form1.salePrice;
            this.form1.cost = this.form1.proCost * this.form1.saleNum;
            this.form1.outProfit = this.form1.saleTotal - this.form1.cost;
          } else {
            this.form1.saleTotal = "";
            this.form1.cost = "";
            this.form1.outProfit = "";
          }
        }
      });
    },
    //出库
    reduceStock(row) {
      this.num1 = row.prostock;
      this.num2 = row.outNum;
      this.num3 = row.prosales;
      console.log(this.num1, this.num2, this.num3);
      this.request
        .post(
          "/product",
          (this.form1 = {
            id: row.outName, //编号
            proStock: this.totalNum,
            proSales: this.totalSaleNum,
          })
        )
        .then((res) => {
          if (res.code === "200") {
            this.$message.success("出库成功");
          } else {
            this.$message.error("出库失败");
          }
        });

      this.request
        .post(
          "/out",
          (this.form1 = {
            id: row.id,
            status: this.status1,
          })
        )
        .then((res) => {
          if (res.code === "200") {
            this.vis3 = false;
            this.load();
          } else {
          }
        });
    },

    fromBuy() {
      this.vis1 = true;
    },
    //双击导入
    cellDblClick(row) {
      this.form1 = JSON.parse(JSON.stringify(row));
      this.vis1 = false;
      this.$message.success("导入成功");
    },
    maxalert1() {
      if (this.form1.saleNum > this.form1.prostock - 1) {
        this.$message.error("库存不能为空！！");
        this.form1.saleNum = this.form1.prostock - 2;
        if (this.form1.salePrice) {
          this.form1.saleTotal = this.form1.saleNum * this.form1.salePrice;
          this.form1.cost = this.form1.proCost * this.form1.saleNum;
          this.form1.outProfit = this.form1.saleTotal - this.form1.cost;
        } else {
          this.form1.saleTotal = "";
          this.form1.outProfit = "";
        }
      } else {
        this.form1.saleNum;
        if (this.form1.salePrice) {
          this.form1.saleTotal = this.form1.saleNum * this.form1.salePrice;
          this.form1.cost = this.form1.proCost * this.form1.saleNum;
          this.form1.outProfit = this.form1.saleTotal - this.form1.cost;
        } else {
          this.form1.saleTotal = "";
          this.form1.outProfit = "";
        }
      }
    },
    calculate1() {
      if (this.form1.saleNum) {
        this.form1.saleTotal = this.form1.saleNum * this.form1.salePrice;
        this.form1.cost = this.form1.proCost * this.form1.saleNum;
        this.form1.outProfit = this.form1.saleTotal - this.form1.cost;
      } else {
        this.form1.saleTotal = "";
        this.form1.outProfit = "";
      }
    },
    maxalert() {
      if (this.form1.outNum > this.form1.prostock - 1) {
        this.$message.error("库存不能为空！！");
        this.form1.outNum = this.form1.prostock - 2;
        if (this.form1.outPrice) {
          this.form1.outTotal = this.form1.outNum * this.form1.outPrice;
        } else {
          this.form1.outTotal = "";
        }
      } else {
        this.form1.outNum;
        if (this.form1.outPrice) {
          this.form1.outTotal = this.form1.outNum * this.form1.outPrice;
        } else {
          this.form1.outTotal = "";
        }
      }
    },
    calculate() {
      if (this.form1.outNum) {
        this.form1.outTotal = this.form1.outNum * this.form1.outPrice;
      } else {
        this.form1.outTotal = "";
      }
    },

    //从出库单导入时，查询出库单
    load1() {
      //请求分页查询数据
      this.request
        .get("/sale/page1", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            results: this.buyStatus,
            saleId: this.saleid2,
            saler: this.buyApply2,
            reviewer: this.buyer2,
            startTime: this.startTime2,
            endTime: this.endTime2,
          },
        })
        .then((res) => {
          this.sales = res.data.records;
        });
    },

    getbuyunit(val) {
      console.log(val);
      this.form1.buyUnit = this.$refs["cascaderAddr"]
        .getCheckedNodes()[0]
        .pathLabels.join("/");
    },
    //新增采购单时自动生成采购单编号
    getProjectNum() {
      const projectTime = new Date();
      const Year = projectTime.getFullYear();
      const Month =
        projectTime.getMonth() + 1 < 10
          ? "0" + (projectTime.getMonth() + 1)
          : projectTime.getMonth() + 1;
      const Day =
        projectTime.getDate() < 10
          ? "0" + projectTime.getDate()
          : projectTime.getDate();
      var currentDate = Year;
      if (Month >= 10) {
        currentDate += Month;
      } else {
        currentDate += "0" + Month;
      }
      if (Day >= 10) {
        currentDate += Day;
      } else {
        currentDate += "0" + Day;
      }
      return currentDate;
    },

    // 获取级联选择器数据
    getAllMenuTree() {
      this.request.get("/unit").then((res) => {
        // 调用递归方法，去除级联数据后将数据赋值给级联选择器
        this.menuData = res.data;
        console.log(this.menuData);
        for (var i = 0; i < this.menuData.length; i++) {
          if (
            this.menuData[i].children == undefined ||
            this.menuData[i].children.length <= 0
          ) {
            this.menuData[i].children = undefined;
          } else {
            this.menuData[i].children;
          }
        }
        return this.menuData.children;
      });
    },

    //保存数据
    save() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request
            .post(
              "/out",
              (this.form1 = {
                outId: this.salenewid, //编号
                outDate: this.form1.saleDate, //日期
                outOper: this.form1.saleApply, //申请人
                outName: this.form1.proname1, //采购商品
                outCustomer: this.form1.buyCustomer, //供应商
                outSupplier: this.form1.saleSupplier, //供应商
                outNum: this.form1.saleNum, //采购数
                outUnit: this.form1.buyUnit,
                outPrice: this.form1.salePrice,
                outTotal: this.form1.saleTotal,
                outProfit: this.form1.outProfit,
                outPaid: this.form1.outPaid,
                status: this.status2,
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
    //提交
    putup(row) {
      this.form1 = JSON.parse(JSON.stringify(row)); //可以解决编辑未确认就改变的问题
      console.log(this.form1.prosales + "kucun");
      this.vis3 = true;
    },
    //编辑数据
    save1() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request
            .post(
              "/out",
              (this.form1 = {
                id: this.form1.id,
                outDate: this.form1.outDate, //日期
                outOper: this.form1.outOper, //申请人
                outName: this.form1.proname, //采购商品
                outCustomer: this.form1.outCustomer, //供应商
                outSupplier: this.form1.outSupplier, //供应商
                outNum: this.form1.outNum, //采购数
                outPrice: this.form1.outPrice,
                outTotal: this.form1.outTotal,
                outProfit: this.form1.outProfit,
                outPaid: this.form1.outPaid,
              })
            )
            .then((res) => {
              if (res.code === "200") {
                this.$message.success("保存成功");
                this.vis = false;
                this.load();
              } else {
                this.$message.error("保存失败");
              }
            });
        }
      });
    },

    updateStatus() {
      this.request
        .post(
          "/out",
          (this.form1 = {
            id: this.form1.id,
            status: this.status,
          })
        )
        .then((res) => {
          if (res.code === "200") {
            this.$message.success("提交成功");
            this.vis3 = false;
            this.load();
          } else {
            this.$message.error("提交失败");
          }
        });
    },
    //新增弹窗  新增采购单
    handleAddbuy() {
      this.dialogFormVisible = true;
      this.form1 = {};
      this.form1.buyStatus = "未通过";
      this.salenewid = this.getProjectNum() + Math.floor(Math.random() * 10000);
    },
    //查看采购申请单
    lookapply(row) {
      this.vis2 = true;
      this.form1 = JSON.parse(JSON.stringify(row)); //可以解决编辑未确认就改变的问题
    },
    //修改审核单弹出框
    handleEdit(row) {
      this.vis = true;
      this.form1 = JSON.parse(JSON.stringify(row));
      this.id = this.form1.saleName;
      this.request.get("/product/id/" + this.id).then((res) => {
        if (res.code === "200") {
          console.log(res.data.proStock);
          this.proStock1 = res.data.proStock;
          console.log(this.proStock1);
        } else {
          this.$message.error("删除失败");
        }
      });
    },
    //审核（由管理员执行）
    handleCheck(row) {
      this.vis2 = true;

      this.form1 = JSON.parse(JSON.stringify(row));
    },
    //删除
    handleDel(id) {
      this.request.delete("/out/" + id).then((res) => {
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
      this.request.post("/out/del/batch", ids).then((res) => {
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
      this.buyid = "";
      this.startTime = "";
      this.endTime = "";
      this.outCustomer = "";
      this.buyApply = "";
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
      window.open("http://localhost:9090/out/export");
    },
    //提示文件导入成功
    handleExcelImportSuccess() {
      this.$message.success("导入成功");
      this.load();
    },
  },
};
</script>


<style scoped>
.headerr {
  width: 100%;
  height: 40px;
  background-color: #eee;
}
.vis3 {
  width: 1000px;
  height: 1000px;
  margin: -80px auto;
}
.personal-mybuluo-head {
  height: 14px;
  position: relative;
  margin-top: 60px;
}
.personal-mybuluo-wording {
  position: absolute;
  top: 56px;
  z-index: 2;
  left: 50%;
  background-color: #fff;
  color: rgb(28, 36, 174);
  text-align: center;
  -webkit-transform: translate(-50%, 0);
  transform: translate(-50%, 0);
  padding: 0 10px;
  font-size: 30px;
}
.personal-border {
  position: absolute;
  top: 67px;
  left: 0;
  width: 100%;
  height: 14px;
  z-index: 1;
}
.jmu-border-1px {
  position: relative;
}
.jmu-border-1px.border-bottom:after {
  border-bottom: 1px solid #dedfe0;
}
.jmu-border-1px:after {
  right: -100%;
  bottom: -100%;
}
.jmu-border-1px:after {
  display: block;
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  pointer-events: none;
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
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #0759cd;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>