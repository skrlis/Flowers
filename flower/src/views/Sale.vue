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
          v-model="saleid"
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

      <el-form-item label="申请人：">
        <el-select
          v-model="buyApply"
          clearable
          filterable
          placeholder="选择申请人"
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

      <el-form-item label="审核人：">
        <el-select
          v-model="buyer"
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
      <el-form-item label="审核状态">
        <el-select
          v-model="buyStatus"
          clearable
          filterable
          placeholder="审核状态"
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
          ><i class="el-icon-circle-plus-outline"></i>新增出库申请单</el-button
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
      <el-table-column prop="saleId" label="出库单号">
        <template slot-scope="scope">
          <span style="color: #6ba0d6">{{ scope.row.saleId }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="proname" label="出库商品"> </el-table-column>

      <el-table-column prop="saleTotal" label="总价"> </el-table-column>
      <el-table-column prop="salePaid" label="已付款"> </el-table-column>

      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-dropdown
            split-button
            type="primary"
            @click="lookapply(scope.row)"
          >
            <i class="el-icon-view"></i>
            查看

            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleEdit(scope.row)"
                ><i class="el-icon-edit"></i>修改</el-dropdown-item
              >
              <el-dropdown-item
                v-if="
                  adname.role === 'ROLE_ADMIN' && scope.row.results === null
                "
                @click.native="handleCheck(scope.row)"
                ><i class="el-icon-s-check"></i> 审核</el-dropdown-item
              >
              <el-popconfirm
                confirm-button-text="确定"
                cancel-button-text="我再想想"
                icon="el-icon-info"
                icon-color="red"
                title="您确定删除吗？"
                @confirm="handleDel(scope.row.id)"
              >
                <el-dropdown-item slot="reference"
                  ><i class="el-icon-delete"></i>删除</el-dropdown-item
                >
              </el-popconfirm>
            </el-dropdown-menu>
          </el-dropdown>
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
    <!-- 新增出库通知单 -->
    <el-dialog
      :visible.sync="dialogFormVisible"
      :fullscreen="full"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">新增出库申请单</div>
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
          label="出库编号:"
          prop="saleId"
          class="redss"
          style="margin-left: 50px"
        >
          <span>{{ form1.salenewid }}</span>
        </el-form-item>
        <el-form-item
          label="申请日期:"
          prop="saleDate"
          style="margin-left: 70px"
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
        <el-form-item label="申请人:" prop="saler" style="margin-left: 70px">
          <el-select
            v-model="form1.saler"
            placeholder="选择申请人"
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

        <div class="personal-mybuluo-head">
          <div class="personal-mybuluo-wording">出库通知单明细</div>
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
              prop="saleName"
            >
              <el-select
                style="width: 230px; float: left"
                @change="getkucun"
                v-model="form1.name1"
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

            <el-form-item style="width: 230px; float: left" prop="buySupplier">
              <el-select
                style="width: 230px; margin-top: 10px; float: left"
                v-model="form1.buySupplier"
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

            <el-form-item
              style="
                width: 200px;
                margin-top: 10px;
                margin-left: 45px;
                float: left;
              "
            >
              <span>{{ this.proStock }}</span>
            </el-form-item>

            <el-form-item style="width: 230px; float: left" prop="buyCount">
              <el-input-number
                @change="maxalert"
                type="number"
                placeholder="出库数量"
                style="width: 230px; margin-top: 10px; float: left"
                v-model.number="form1.buyCount"
              ></el-input-number>
            </el-form-item>
            <el-form-item style="width: 230px; float: left" prop="saleUnit">
              <el-cascader
                v-model.number="form1.saleUnit"
                placeholder="选择数量单位"
                style="width: 230px; margin-top: 10px; float: left"
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
            <el-form-item style="width: 230px; float: left" prop="price">
              <el-input-number
                @change="calculate"
                type="number"
                v-model="form1.price"
                placeholder="单价"
                style="width: 260px; margin-top: 10px; float: left"
              ></el-input-number>
            </el-form-item>
            <el-form-item style="width: 200px; float: left" prop="saleTotal">
              <el-input
                placeholder="总价格"
                v-model="form1.saleTotal"
                autocomplete="off"
                style="
                  width: 290px;
                  margin-top: 10px;
                  float: left;
                  margin-left: 30px;
                "
              ></el-input>
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

        <el-form-item
          label="已付款:"
          prop="salePaid"
          class="redss"
          style="margin-left: 50px; margin-top: 100px"
        >
          <el-input-number
            placeholder="已付款"
            style="width: 430px"
            v-model="form1.salePaid"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
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
        <div style="font-size: 25px; font-weight: bold">修改出库通知单</div>
      </template>
      <el-form
        :model="form1"
        :inline="true"
        :rules="rules"
        ref="ruleForm"
        status-icon
        size="small"
        class="demo-form-inline"
      >
        <el-form-item
          label="通知单编号:"
          class="redss"
          style="margin-left: 50px"
        >
          <span style="margin-left: 10px">{{ form1.saleId }}</span>
        </el-form-item>
        <el-form-item
          label="申请日期:"
          prop="saleDate"
          style="margin-left: 70px"
        >
          <el-date-picker
            v-model="form1.saleDate"
            align="right"
            type="date"
            placeholder="选择日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="申请人:" prop="saler" style="margin-left: 70px">
          <el-select
            v-model="form1.saler"
            clearable
            filterable
            placeholder="选择申请人"
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

        <div class="personal-mybuluo-head">
          <div class="personal-mybuluo-wording">出库通知单明细</div>
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
            <el-form-item style="width: 230px; float: left" prop="proname">
              <el-select
                style="width: 230px; margin-top: 10px; float: left"
                @change="getkucun1"
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
            <!-- <el-input v-model="form1.saleName" ></el-input> -->
            <el-form-item style="width: 230px; float: left" prop="saleSupplier">
              <el-select
                style="width: 230px; margin-top: 10px; float: left"
                v-model="form1.saleSupplier"
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
              <span style="margin-left: 50px">{{ this.proStock1 }}</span>
            </el-form-item>
            <el-form-item style="width: 230px; float: left" prop="saleNum">
              <el-input-number
                @change="maxalert1"
                type="number"
                placeholder="出库数量"
                style="width: 230px; margin-top: 10px; float: left"
                v-model="form1.saleNum"
              ></el-input-number>
            </el-form-item>
            <el-form-item
              style="width: 230px; float: left; margin-top: 10px"
              prop="saleUnit"
            >
              <span style="margin-left: 100px">{{ form1.saleUnit }}</span>
            </el-form-item>
            <el-form-item style="width: 230px; float: left" prop="salePrice">
              <el-input-number
                @change="calculate1"
                type="number"
                v-model="form1.salePrice"
                placeholder="单价"
                style="
                  width: 260px;
                  margin-top: 10px;
                  float: left;
                  margin-left: 40px;
                "
              ></el-input-number>
            </el-form-item>
            <el-form-item style="width: 200px; float: left; margin-top: 10px">
              <span style="margin-left: 130px">{{ form1.saleTotal }}</span>
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

        <el-form-item
          label="已付款:"
          class="redss"
          style="margin-left: 50px; margin-top: 100px"
          prop="salePaid"
        >
          <el-input-number
            placeholder="已付款"
            style="width: 430px"
            v-model="form1.salePaid"
            :min="0"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save1">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看出库通知单单 -->
    <el-dialog
      :visible.sync="vis1"
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
            style="width: 440px; margin-top: -10px; float: left"
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
        <el-button @click="vis1 = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 审核采购申请单 -->
    <el-dialog
      :visible.sync="vis2"
      :fullscreen="full"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">审核出库通知单</div>
      </template>
      <el-form
        :model="form1"
        :inline="true"
        status-icon
        size="small"
        class="demo-form-inline"
      >
        <el-form-item
          label="出库编号:"
          prop="buyid"
          class="redss"
          style="margin-left: 50px"
        >
          <span>{{ form1.saleId }}</span>
        </el-form-item>
        <el-form-item
          label="出库日期："
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
              <h3 style="padding-top: 10px; font-size: 15px">单价</h3>
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
        </div>

        <div class="personal-mybuluo-head">
          <div class="personal-mybuluo-wording">审核</div>
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>

        <div style="margin-top: 150px">
          <el-form-item label="审核：" prop="buyer" style="margin-left: 190px">
            <el-button-group>
              <el-button class="success" @click="passAll"
                ><i class="el-icon-check"></i>通过</el-button
              >
              <el-button class="success" @click="nopassAll"
                ><i class="el-icon-close"></i>不通过</el-button
              >
              <el-form-item v-if="false" style="margin-left: 170px">
                <el-input v-model="pass1"> </el-input>
              </el-form-item>
            </el-button-group>
          </el-form-item>

          <br />
          <el-form-item
            label="审核备注:"
            prop="buyer"
            style="margin-left: 170px"
          >
            <!-- 此处不能写this.remarkss -->
            <el-input
              v-model="remarkss"
              style="width: 1000px; height: 100px"
              type="textarea"
              @input="onInput"
              :rows="9"
              placeholder="请输入备注信息"
            >
            </el-input>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="vis2 = false">关闭</el-button>
        <el-button type="primary" @click="adminCheck">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { formatDate } from "@/common/date.js"; // 在组件中引用date.js
export default {
  name: "Sale",
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
      menuData: [],
      productData: [],
      saleid: "",
      saleName: "",
      salename: "",
      startTime: "",
      endTime: "",
      buyid: "",
      buyDate: "",
      buyApply: "",
      buyer: "",
      buyStatus: "",
      pass: "已通过",
      nopass: "未通过",
      pass1: "",
      remarkss: "",
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
      admins: [],
      options: [],
      num: 1,
      value: [],
      form1: {
        salenewid: "33",
        buyStatus: "未通过",
      },
      agentData: [
        {
          value: "已通过",
          label: "已通过",
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
      proStock: "",
      rules: {
        saleDate: [
          { required: true, message: "日期不能为空", trigger: "blur" },
        ],
        saler: [
          {
            required: true,
            message: "申请人不能为空",
            trigger: ["blur", "change"],
          },
        ],
        saleName: [
          {
            required: true,
            message: "商品不能为空",
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
        buyCount: [
          {
            required: true,
            message: "出库数量不能为空",
            trigger: ["blur", "change"],
          },
        ],
        saleUnit: [
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
        salePaid: [
          {
            required: true,
            message: "总花费不能为空",
            trigger: ["blur", "change"],
          },
        ],
        //编辑校验
        proname: [{ required: true, message: "商品不能为空", trigger: "blur" }],
        saleSupplier: [
          {
            required: true,
            message: "供应商不能为空",
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
        salePrice: [
          {
            required: true,
            message: "单价不能为空",
            trigger: ["blur", "change"],
          },
        ],
      },
    };
  },

  created() {
    this.getAllMenuTree();

    this.form1.salenewid =
      "CK" + this.getProjectNum() + Math.floor(Math.random() * 10000);
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
      return formatDate(date, "yyyy-MM-dd");
    },
  },
  methods: {
    load() {
      //请求分页查询数据
      //
      //请求分页查询数据
      this.request
        .get("/sale/page1", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            saleId: this.saleid,
            saler: this.buyApply,
            reviewer: this.buyer,
            results: this.buyStatus,
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

      this.request.get("/sort").then((res) => {
        this.sorts = res.data;
      });

      //筛选出销售员
      this.request
        .get("/manager/flagissale", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            role: this.role,
          },
        })
        .then((res) => {
          this.sales = res.data.records;
          this.total = res.data.total;
        });

      //筛选出管理员
      this.request
        .get("/manager/flagisadmin", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            role: this.role,
          },
        })
        .then((res) => {
          this.admins = res.data.records;
          this.total = res.data.total;
        });
    },
    //编辑时
    getkucun(value) {
      this.productData.forEach((item) => {
        if (item.id === value) {
          console.log(item);
          this.proStock = item.proStock;
          this.form1.buySupplier = item.proFrom;
          this.form1.price = item.proCost;
          this.form1.saleName = item.id;
          console.log(this.proStock + "库存");
        }
      });
    },
    //编辑时
    getkucun1(value) {
      this.productData.forEach((item) => {
        if (item.id === value) {
          console.log(item);
          this.proStock1 = item.proStock;
          this.form1.saleSupplier = item.proFrom;
          this.form1.proCost = item.proCost;
          this.form1.saleName = item.id;
          console.log(this.form1.proCost + "dddddddd");
        }
      });
    },
    maxalert() {
      if (this.form1.buyCount > this.proStock - 1) {
        this.$message.error("库存不能为空！！");
        this.form1.buyCount = this.proStock - 2;

        if (this.form1.price) {
          this.form1.saleTotal = this.form1.buyCount * this.form1.price;
        } else {
          this.form1.saleTotal = "";
        }
      } else {
        this.form1.buyCount;
        if (this.form1.price) {
          this.form1.saleTotal = this.form1.buyCount * this.form1.price;
        } else {
          this.form1.saleTotal = "";
        }
      }
    },
    maxalert1() {
      if (this.form1.saleNum > this.proStock1 - 1) {
        this.$message.error("库存不能为空！！");
        this.form1.saleNum = this.proStock1 - 2;

        if (this.form1.salePrice) {
          this.form1.saleTotal = this.form1.saleNum * this.form1.salePrice;
        } else {
          this.form1.saleTotal = "";
        }
      } else {
        this.form1.saleNum;
        if (this.form1.salePrice) {
          this.form1.saleTotal = this.form1.saleNum * this.form1.salePrice;
        } else {
          this.form1.saleTotal = "";
        }
      }
    },
    calculate() {
      console.log(this.form1.price);
      if (this.form1.buyCount) {
        this.form1.saleTotal = this.form1.buyCount * this.form1.price;
      } else {
        this.form1.saleTotal = "";
      }
    },

    calculate1() {
      if (this.form1.saleNum) {
        this.form1.saleTotal = this.form1.saleNum * this.form1.salePrice;
      } else {
        this.form1.saleTotal = "";
      }
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
    onInput() {
      this.$forceUpdate();
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
              "/sale",
              (this.form1 = {
                saleId: this.form1.salenewid, //编号
                saleDate: this.form1.saleDate, //日期
                saler: this.form1.saler, //申请人
                saleName: this.form1.name1, //采购商品
                saleSupplier: this.form1.buySupplier, //供应商
                saleNum: this.form1.buyCount, //采购数
                saleUnit: this.form1.buyUnit,
                salePrice: this.form1.price,
                saleTotal: this.form1.saleTotal,
                salePaid: this.form1.salePaid,
              })
            )
            .then((res) => {
              if (res.code === "200") {
                console.log(this.form1.saleDate + "jjjjjjjjjjjj");
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
    //编辑数据
    save1() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request
            .post(
              "/sale",
              (this.form1 = {
                id: this.form1.id,
                saleDate: this.form1.saleDate, //日期
                saler: this.form1.saler, //申请人
                saleName: this.form1.saleName, //采购商品
                saleSupplier: this.form1.saleSupplier, //供应商
                saleNum: this.form1.saleNum, //采购数
                salePrice: this.form1.salePrice,
                saleTotal: this.form1.saleTotal,
                salePaid: this.form1.salePaid,
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

    //审核
    adminCheck() {
      if (this.pass1) {
        this.request
          .post(
            "/sale",
            (this.form1 = {
              id: this.form1.id,
              reviewer: this.adname.adname,
              salePaid: this.form1.salePaid,
              results: this.pass1,
              reviewdate: this.nowtime,
              remarks: this.remarkss,
            })
          )
          .then((res) => {
            if (res.code === "200") {
              this.$message.success("保存成功");
              this.vis2 = false;
              this.load();
            } else {
              this.$message.error("保存失败");
            }
          });
      } else {
        this.$message.warning("请选择审核结果");
      }
    },
    //新增弹窗  新增采购单
    handleAddbuy() {
      this.dialogFormVisible = true;
      this.form1 = {};
      this.form1.buyStatus = "未通过";
      this.form1.salenewid =
        "CK" + this.getProjectNum() + Math.floor(Math.random() * 10000);
    },
    //查看采购申请单
    lookapply(row) {
      this.vis1 = true;
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
    //审核通过
    passAll() {
      this.pass1 = this.pass;
    },
    //审核未通过
    nopassAll() {
      this.pass1 = this.nopass;
    },
    //删除
    handleDel(id) {
      this.request.delete("/sale/" + id).then((res) => {
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
      this.request.post("/sale/del/batch", ids).then((res) => {
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
      this.saleid = "";
      this.startTime = "";
      this.endTime = "";
      this.buyApply = "";
      this.buyer = "";
      this.buyStatus = "";
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
      window.open("http://localhost:9090/sale/export");
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
</style>