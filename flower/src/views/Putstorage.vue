<template>
  <div style="width: 100%; height: 100%">
    <!-- 搜索表单 -->
    <el-form
      :inline="true"
      label-width="100px"
      size="small"
      class="demo-form-inline"
    >
      <el-form-item label="入库单号：">
        <el-input
          v-model="buyid"
          style="width: 300px"
          clearable
          placeholder="请输采购单号"
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

      <el-form-item label="操作人：">
        <el-select
          v-model="buyApply"
          clearable
          filterable
          placeholder="选择申请人"
          style="width: 300px"
        >
          <el-option
            v-for="item in buyers"
            :key="item.adid"
            :label="item.adname"
            :value="item.adname"
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
        <el-button type="info" @click="reset">重置</el-button>
      </el-form-item>

      <div>
        <el-button icon="Plus" @click="handleAddbuy" style="margin-left: 10px"
          ><i class="el-icon-circle-plus-outline"></i>新增入库单</el-button
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
            <el-form-item label="入库单号：">
              <span>{{ props.row.putId }}</span>
            </el-form-item>
            <el-form-item label="入库商品：">
              <span>{{ props.row.proname }}</span>
            </el-form-item>
            <el-form-item label="入库日期：">
              <span>{{ props.row.putDate | formatDate }}</span>
            </el-form-item>
            <el-form-item label="供应商：">
              <span>{{ props.row.putSupplier }}</span>
            </el-form-item>
            <el-form-item label="操作人：">
              <span>{{ props.row.putOper }}</span>
            </el-form-item>
            <el-form-item label="入库数量：">
              <span>{{ props.row.putNum }}</span>
            </el-form-item>
            <el-form-item label="数量单位：">
              <span>{{ props.row.putUnit }}</span>
            </el-form-item>
            <el-form-item label="单价：">
              <span>{{ props.row.putPrice }}</span>
            </el-form-item>
            <el-form-item label="总价：">
              <span>{{ props.row.putTotal }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="putId" label="入库单号"> </el-table-column>
      <el-table-column prop="proname" label="入库商品"> </el-table-column>
      <el-table-column prop="putSupplier" label="供应商"> </el-table-column>
      <el-table-column prop="putOper" label="操作人"> </el-table-column>
      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            @click.once="addPut(scope.row)"
            v-if="scope.row.status === '已提交'"
            >入库<i class="el-icon-edit"></i
          ></el-button>
          <el-button
            type="warning"
            disabled
            v-if="scope.row.status === '已入库'"
            >已入库</el-button
          >
          <el-button
            type="primary"
            @click="putup(scope.row)"
            v-if="scope.row.status === '未提交'"
            >提交<i class="el-icon-edit"></i
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
    <!-- 新增入库单 -->
    <el-dialog
      :visible.sync="dialogFormVisible"
      :fullscreen="full"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">新增入库单</div>
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
          label="入库单编号:"
          class="redss"
          style="margin-left: 50px"
        >
          <span>{{ this.salenewid }}</span>
        </el-form-item>
        <el-form-item
          label="入库日期:"
          style="margin-left: 70px"
          prop="saleDate"
        >
          <el-date-picker
            v-model="form1.saleDate"
            align="right"
            type="date"
            placeholder="选择入库日期"
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
              v-for="item in buyers"
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
          prop="buySupplier"
        >
          <el-select
            v-model="form1.buySupplier"
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
        <div class="personal-mybuluo-head">
          <div class="personal-mybuluo-wording">
            入库商品明细
            <el-button type="primary" @click="fromBuy">
              <i class="el-icon-position"></i>从采购单导入</el-button
            >
          </div>
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>
        <div>
          <div style="margin-top: 200px" class="headerr">
            <div style="width: 360px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">入库商品</h3>
            </div>
            <div style="width: 360px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">入库数量</h3>
            </div>
            <div style="width: 360px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">数量单位</h3>
            </div>
            <div style="width: 360px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">单价</h3>
            </div>
            <div style="width: 360px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">总价格</h3>
            </div>
          </div>
          <div style="width: 100%; height: 50px; border-bottom: 1px solid #eee">
            <el-form-item style="width: 300px; float: left" prop="putName">
              <el-select
                style="width: 300px; margin-top: 10px; float: left"
                @change="getkucun"
                v-model="form1.putName"
                placeholder="请选择入库商品"
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

            <el-form-item style="width: 360px; float: left" prop="buyCount">
              <el-input-number
                @change="calculate2"
                type="number"
                placeholder="入库数量"
                style="width: 360px; margin-top: 10px; float: left"
                v-model="form1.buyCount"
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
            <el-form-item style="width: 360px; float: left" prop="price">
              <el-input-number
                @change="calculate3"
                type="number"
                v-model="form1.price"
                placeholder="单价"
                style="width: 360px; margin-top: 10px; float: left"
              ></el-input-number>
            </el-form-item>
            <el-form-item style="width: 360px; float: left" prop="saleTotal">
              <el-input
                placeholder="总价"
                v-model="form1.saleTotal"
                autocomplete="off"
                style="
                  width: 360px;
                  margin-top: 10px;
                  float: left;
                  margin-left: 30px;
                "
              ></el-input>
            </el-form-item>

            <el-form-item v-if="false">
              <el-input
                v-model="status"
                autocomplete="off"
                style="
                  width: 360px;
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
      </el-form>

      <div slot="footer" style="margin-top: 200px" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <!-- -----------------------------------------弹出框222222222222222222222222--------------------------------------------------------------------- -->
    <!-- 编辑采购申请单 -->
    <el-dialog
      :visible.sync="vis"
      :fullscreen="full"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">修改入库单</div>
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
          label="入库单号:"
          prop="buyid"
          class="redss"
          style="margin-left: 50px"
        >
          <span>{{ form1.putId }}</span>
        </el-form-item>
        <el-form-item
          label="入库日期:"
          prop="putDate"
          style="margin-left: 70px"
        >
          <el-date-picker
            v-model="form1.putDate"
            align="right"
            type="date"
            placeholder="选择日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="操作人:" prop="putOper" style="margin-left: 70px">
          <el-select
            v-model="form1.putOper"
            clearable
            filterable
            placeholder="选择操作人"
            style="width: 300px"
          >
            <el-option
              v-for="item in buyers"
              :key="item.adid"
              :label="item.adname"
              :value="item.adname"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <div class="personal-mybuluo-head">
          <div class="personal-mybuluo-wording">入库单明细</div>
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>

        <div>
          <div style="margin-top: 200px" class="headerr">
            <div style="width: 260px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">入库商品</h3>
            </div>
            <div style="width: 260px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">供应商</h3>
            </div>
            <div style="width: 260px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">入库数量</h3>
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
            <el-form-item style="width: 230px; float: left" prop="proname">
              <el-select
                style="width: 230px; margin-top: 10px; float: left"
                @change="getkucun"
                v-model="form1.proname"
                placeholder="请选择入库商品"
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
            <el-input v-if="false" v-model="form1.saleName"></el-input>
            <el-form-item style="width: 230px; float: left" prop="putSupplier">
              <el-select
                style="width: 230px; margin-top: 10px; float: left"
                v-model="form1.putSupplier"
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

            <el-form-item style="width: 230px; float: left" prop="putNum">
              <el-input-number
                @change="calculate"
                type="number"
                placeholder="入库数量"
                style="width: 230px; margin-top: 10px; float: left"
                v-model="form1.putNum"
              ></el-input-number>
            </el-form-item>
            <el-form-item
              style="width: 230px; margin-top: 10px; float: left"
              prop="putUnit"
            >
              <span style="margin-left: 90px; margin-top: 10px">{{
                form1.putUnit
              }}</span>
            </el-form-item>
            <el-form-item style="width: 230px; float: left" prop="putPrice">
              <el-input-number
                @change="calculate1"
                type="number"
                v-model="form1.putPrice"
                placeholder="单价"
                style="width: 260px; margin-top: 10px; float: left"
              ></el-input-number>
            </el-form-item>
            <el-form-item style="width: 200px; float: left" prop="putTotal">
              <span style="margin-left: 90px; margin-top: 10px">{{
                form1.putTotal
              }}</span>
            </el-form-item>
            <!-- <el-form-item  v-if="false"  style="width:400px;margin-top:10px;float:left">
            <el-input style="width:100%" v-model="form1.buyStatus"></el-input>
         </el-form-item>  -->
          </div>
        </div>
        <div class="personal-mybuluo-head" style="margin-top: -50px">
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>

        <br />
      </el-form>

      <div slot="footer" style="margin-top: 100px" class="dialog-footer">
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
        <div style="font-size: 25px; font-weight: bold">入库单</div>
      </template>
      <el-form :model="form1" status-icon size="small" class="demo-form-inline">
        <el-form-item
          label="入库单编号:"
          class="redss"
          style="margin-left: 50px"
        >
          <span>{{ this.form1.putId }}</span>
        </el-form-item>
        <el-form-item label="入库日期:" style="margin-left: 70px">
          <span>{{ this.form1.putDate | formatDate }}</span>
        </el-form-item>
        <el-form-item label="操作人:" style="margin-left: 70px">
          <span>{{ this.form1.putOper }}</span>
        </el-form-item>
        <el-form-item label="入库商品:" style="margin-left: 70px">
          <span>{{ this.form1.putName }}</span>
        </el-form-item>
        <el-form-item label="供应商:" style="margin-left: 70px">
          <span>{{ this.form1.putSupplier }}</span>
        </el-form-item>
        <el-form-item label="入库数量:" style="margin-left: 70px">
          <span>{{ this.form1.putNum }}</span>
        </el-form-item>

        <el-form-item label="数量单位:" style="margin-left: 70px">
          <span>{{ this.form1.putUnit }}</span>
        </el-form-item>
        <el-form-item label="单价:" style="margin-left: 70px">
          <span>{{ this.form1.putPrice }}</span>
        </el-form-item>
        <el-form-item label="总价:" style="margin-left: 70px">
          <span>{{ this.form1.putTotal }}</span>
        </el-form-item>
        <el-form-item v-if="false">
          <span>{{ this.Status1 }}</span>
        </el-form-item>
        <el-form-item>
          <span
            ><img src="../assets/注意.png" style="width: 20px; height: 20px" />
            提交后不可修改，将进行入库操作</span
          >
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="vis3 = false">取 消</el-button>
        <el-button type="primary" @click="updateStatus">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 从采购单导入  -->
    <el-dialog
      :visible.sync="vis1"
      :fullscreen="full"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">从采购单导入</div>
      </template>
      <el-form
        :model="form1"
        :inline="true"
        status-icon
        size="small"
        class="demo-form-inline"
      >
        <el-form-item
          label="采购单编号"
          class="redss"
          style="margin-left: 50px"
        >
          <el-input
            v-model="buyid2"
            autocomplete="off"
            style="width: 300px"
            placeholder="请输入采购单号"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="开始时间" style="margin-left: 70px">
          <el-date-picker
            v-model="buyid2"
            align="right"
            type="date"
            placeholder="选择采购日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" style="margin-left: 70px">
          <el-date-picker
            v-model="endtime2"
            align="right"
            type="date"
            placeholder="选择采购日期"
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
              v-for="item in buyers"
              :key="item.adid"
              :label="item.adname"
              :value="item.adname"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="采购人" style="margin-left: 70px">
          <el-select
            v-model="buyer2"
            clearable
            filterable
            placeholder="选择采购人"
            style="width: 300px"
          >
            <el-option
              v-for="item in buyers"
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

      <!-- 采购单数据展示 -->
      <el-table
        :data="buys"
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
            <span style="color: #6ba0d6">{{ scope.row.buyid }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="buyName" label="采购商品" width="220">
        </el-table-column>
        <el-table-column prop="buySupplier" label="供应商" width="220">
        </el-table-column>
        <el-table-column prop="buyCount" label="采购数量" width="220">
        </el-table-column>
        <el-table-column prop="buyUnit" label="数量单位" width="220">
        </el-table-column>
        <el-table-column prop="buyPrice" label="单价" width="220">
        </el-table-column>
        <el-table-column prop="buyApply" label="申请人" width="220">
        </el-table-column>
        <el-table-column prop="buyer" label="采购人" width="120">
        </el-table-column>

        <el-table-column prop="buyStatus" label="审核状态" width="120">
        </el-table-column>

        <el-table-column fixed="right" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" @click="lookapply(scope.row)">
              <i class="el-icon-view"></i>
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog
      :visible.sync="vis4"
      :fullscreen="full"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">查看采购申请单</div>
      </template>
      <el-form
        :model="form"
        :inline="true"
        status-icon
        size="small"
        class="demo-form-inline"
      >
        <el-form-item
          label="采购单编号:"
          prop="buyid"
          class="redss"
          style="margin-left: 50px"
        >
          <span>{{ form.buyid }}</span>
        </el-form-item>
        <el-form-item
          label="采购日期："
          prop="buyDate"
          style="margin-left: 270px"
        >
          <span>{{ form.buyDate | formatDate }}</span>
        </el-form-item>
        <el-form-item
          label="申请人："
          prop="buyApply"
          style="margin-left: 270px"
        >
          <span>{{ form.buyApply }}</span>
        </el-form-item>
        <el-form-item label="采购人：" prop="buyer" style="margin-left: 270px">
          <span>{{ form.buyer }}</span>
        </el-form-item>

        <div>
          <div style="margin-top: 100px" class="headerr">
            <div style="width: 440px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">采购商品</h3>
            </div>
            <div style="width: 440px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">供应商</h3>
            </div>
            <div style="width: 440px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">采购数量</h3>
            </div>
            <div style="width: 300px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">单价</h3>
            </div>
          </div>
          <div style="width: 100%; height: 50px; border-bottom: 1px solid #eee">
            <span
              autocomplete="off"
              style="width: 440px; margin-top: 10px; float: left"
              >{{ form.buyName }}</span
            >
            <span
              autocomplete="off"
              style="width: 440px; margin-top: 10px; float: left"
              >{{ form.buySupplier }}</span
            >
            <span
              autocomplete="off"
              style="width: 440px; margin-top: 10px; float: left"
              >{{ form.buyCount }} / {{ form.buyUnit }}</span
            >
            <span
              autocomplete="off"
              style="width: 390px; margin-top: 10px; float: left"
              >{{ form.buyPrice }}</span
            >
          </div>
        </div>

        <div class="personal-mybuluo-head">
          <div class="personal-mybuluo-wording">审核结果</div>
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>

        <div style="margin-top: 150px">
          <el-form-item
            label="审核状态："
            prop="buyer"
            style="margin-left: 270px"
          >
            <span>{{ form.buyStatus }}</span>
          </el-form-item>

          <el-form-item
            label="审核人："
            prop="buyer"
            style="margin-left: 270px"
          >
            <span>{{ form.reviewer }}</span>
          </el-form-item>
          <el-form-item
            label="审核日期:"
            prop="buyer"
            style="margin-left: 270px"
          >
            <span>{{ form.reviewdate }}</span>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="vis4 = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { formatDate } from "@/common/date.js"; // 在组件中引用date.js
export default {
  name: "Putstorage",
  data() {
    return {
      form: {},
      region: "全部",
      queryForm: {},
      formData: {
        tableData: [],
      },
      buy: "",
      buyPrice1: "",
      tableData: [],
      buys: [],
      menuData: [],
      productData: [],
      buyid: "",
      buyApply: "",
      buyer: "",
      buyName: "",
      buyStatus: "",
      startTime: "",
      endTime: "",
      buyid2: "",
      buyDate2: "",
      endtime2: "",
      buyApply2: "",
      buyer2: "",
      status: "未提交",
      status1: "已入库",
      salenewid: "",
      total1: 0,
      total: 0,
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
      vis4: false,
      dd: true,
      aa: true,
      full: true,
      num1: 0,
      num2: 0,
      buyers: [],
      options: [],
      form2: [],
      num: 1,
      value: [],
      form1: {
        buyStatus: "未通过",
      },
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
      id: "",
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
        buySupplier: [
          {
            required: true,
            message: "供应商不能为空",
            trigger: ["blur", "change"],
          },
        ],
        putName: [
          {
            required: true,
            message: "入库商品不能为空",
            trigger: ["blur", "change"],
          },
        ],
        buyCount: [
          {
            required: true,
            message: "入库数量不能为空",
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
        price: [
          {
            required: true,
            message: "单价不能为空",
            trigger: ["blur", "change"],
          },
        ],
        saleTotal: [
          {
            required: true,
            message: "进价不能为空",
            trigger: ["blur", "change"],
          },
        ],
        //编辑校验
        putDate: [{ required: true, message: "日期不能为空", trigger: "blur" }],
        putOper: [
          {
            required: true,
            message: "操作人不能为空",
            trigger: ["blur", "change"],
          },
        ],
        proname: [
          {
            required: true,
            message: "商品不能为空",
            trigger: ["blur", "change"],
          },
        ],
        putSupplier: [
          {
            required: true,
            message: "供应商不能为空",
            trigger: ["blur", "change"],
          },
        ],
        putNum: [
          {
            required: true,
            message: "入库数量不能为空",
            trigger: ["blur", "change"],
          },
        ],
        putUnit: [
          {
            required: true,
            message: "单位不能为空",
            trigger: ["blur", "change"],
          },
        ],
        putPrice: [
          {
            required: true,
            message: "单价不能为空",
            trigger: ["blur", "change"],
          },
        ],
        putTotal: [
          {
            required: true,
            message: "总价不能为空",
            trigger: ["blur", "change"],
          },
        ],
      },
    };
  },
  computed: {
    totalNum() {
      // return this.num1 +this.num2
      return +this.num1 + +this.num2;
    },
  },

  created() {
    this.getAllMenuTree();

    this.form1.salenewid =
      "RK" + this.getProjectNum() + Math.floor(Math.random() * 10000);
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
        .get("/put/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            putId: this.buyid,
            putOper: this.buyApply,
            startTime: this.startTime,
            endTime: this.endTime,
          },
        })
        .then((res) => {
          this.tableData = res.data.records;
          this.total1 = res.data.total;
          console.log(res.data);
        });

      this.request.get("/supplier/").then((res) => {
        this.options = res.data;
      });

      this.request.get("/product/").then((res) => {
        this.productData = res.data;
      });
      //  this.request.get("/buy/").then(res => {
      //         this.buyData = res.data
      //       })
      this.request.get("/sort").then((res) => {
        this.sorts = res.data;
      });
      // 筛选出采购员
      this.request
        .get("/manager/flagisbuy", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            role: this.role,
          },
        })
        .then((res) => {
          this.buyers = res.data.records;
          this.total = res.data.total;
        });

      this.request
        .get("/buy/passbuy", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          },
        })
        .then((res) => {
          this.buys = res.data.records;
        });
    },
    //编辑时
    getkucun(value) {
      this.productData.forEach((item) => {
        if (item.id === value) {
          console.log(item);
          this.proStock = item.proStock;
          this.form1.saleSupplier = item.proFrom;
          this.form1.flowerid = item.id;
          this.form1.proCost = item.proCost;
          this.form1.saleName = item.id;
          console.log(this.form1.proCost + "dddddddd");
        }
      });
    },
    //从采购单导入弹窗
    fromBuy() {
      this.vis1 = true;
    },
    //提交
    updateStatus() {
      this.request
        .post(
          "/put",
          (this.form1 = {
            id: this.form1.id,
            status: this.Status1,
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
    //双击导入
    cellDblClick(row) {
      this.form1 = JSON.parse(JSON.stringify(row));
      this.vis1 = false;
      this.$message.success("导入成功");
    },
    calculate() {
      if (this.form1.putPrice) {
        this.form1.putTotal = this.form1.putNum * this.form1.putPrice;
      } else {
        this.form1.putTotal = "";
      }
    },
    calculate1() {
      if (this.form1.putNum) {
        this.form1.putTotal = this.form1.putNum * this.form1.putPrice;
      } else {
        this.form1.putTotal = "";
      }
    },
    calculate2() {
      if (this.form1.price) {
        this.form1.saleTotal = this.form1.buyCount * this.form1.price;
      } else {
        this.form1.saleTotal = "";
      }
    },
    calculate3() {
      if (this.form1.buyCount) {
        this.form1.saleTotal = this.form1.buyCount * this.form1.price;
      } else {
        this.form1.saleTotal = "";
      }
    },
    load1() {
      //请求分页查询数据
      this.request
        .get("/buy/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            buyid: this.buyid2,
            buyApply: this.buyApply2,
            buyer: this.buyer2,
            buyName: this.buyName,
            buyStatus: this.buyStatus,
            startTime: this.buyDate2,
            endTime: this.endtime2,
          },
        })
        .then((res) => {
          this.buys = res.data.records;
          this.total1 = res.data.total;
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
              "/put",
              (this.form1 = {
                putId: this.salenewid, //编号
                putDate: this.form1.saleDate, //日期
                putOper: this.form1.saleApply, //申请人
                putName: this.form1.putName, //采购商品
                putSupplier: this.form1.buySupplier, //供应商
                putNum: this.form1.buyCount, //采购数
                putUnit: this.form1.buyUnit,
                putPrice: this.form1.price,
                putTotal: this.form1.saleTotal,
                status: this.status, //编号
              })
            )
            .then((res) => {
              if (res.code === "200") {
                this.$message.success("保存成功");
                //      setTimeout(this.request.post("/product",
                // this.form1={
                //   id: this.form1.flowerid,//编号
                //   proStock:this.proStock + this.form1.buyCount,//日期
                // }),2000 );

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
      this.vis3 = true;
    },
    //编辑数据
    save1() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request
            .post(
              "/put",
              (this.form1 = {
                id: this.form1.id,
                putDate: this.form1.putDate, //日期
                putOper: this.form1.putOper, //申请人
                putName: this.form1.proname, //采购商品
                putSupplier: this.form1.putSupplier, //供应商
                putNum: this.form1.putNum, //采购数
                putUnit: this.form1.putUnit,
                putPrice: this.form1.putPrice,
                putTotal: this.form1.putTotal,
                // status:this.Status1,//编号
              })
            )
            .then((res) => {
              if (res.code === "200") {
                this.$message.success("保存成功");
                this.vis = false;
                console.log(this.Status1);
                this.load();
              } else {
                this.$message.error("保存失败");
              }
            });
        }
      });
    },
    //新增弹窗  新增采购单
    handleAddbuy() {
      this.dialogFormVisible = true;
      this.form1 = {};
      this.form1.buyStatus = "未通过";
      this.salenewid =
        "RK" + this.getProjectNum() + Math.floor(Math.random() * 10000);
    },
    //查看采购申请单
    lookapply(row) {
      this.vis4 = true;
      this.form = JSON.parse(JSON.stringify(row)); //可以解决编辑未确认就改变的问题
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
          this.form1.putSupplier = res.data.proFrom;
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
    //入库  由管理员入库
    addPut(row) {
      this.num1 = row.prostock;
      this.num2 = row.putNum;
      console.log(this.totalNum + "totalNum");
      this.request
        .post(
          "/product",
          (this.form1 = {
            id: row.putName, //编号
            proStock: this.totalNum,
          })
        )
        .then((res) => {
          if (res.code === "200") {
            this.$message.success("入库成功");
            this.load();
          } else {
            this.$message.error("入库失败");
          }
        });

      this.request
        .post(
          "/put",
          (this.form1 = {
            id: row.id,
            status: this.status1,
            // status:this.Status1,//编号
          })
        )
        .then((res) => {
          if (res.code === "200") {
            this.load();
          } else {
          }
        });
    },

    //删除
    handleDel(id) {
      this.request.delete("/put/" + id).then((res) => {
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
      this.request.post("/put/del/batch", ids).then((res) => {
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
      window.open("http://localhost:9090/put/export");
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
.vis3 {
  width: 1000px;
  height: 1000px;
  margin: 0 auto;
}
.headerr {
  width: 100%;
  height: 40px;
  background-color: #eee;
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