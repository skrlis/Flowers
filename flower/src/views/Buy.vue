<template>
  <div style="width: 100%; height: 100%">
    <!-- 搜索表单 -->
    <el-form
      :inline="true"
      label-width="100px"
      size="small"
      class="demo-form-inline"
    >
      <el-form-item label="采购单号：">
        <el-input
          v-model="buyid"
          style="width: 300px"
          clearable
          placeholder="请输采购单号"
        >
        </el-input>
      </el-form-item>

      <el-form-item label="采购商品：">
        <el-input
          v-model="buyName"
          style="width: 300px"
          clearable
          placeholder="请输采购商品"
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
            v-for="item in buyers"
            :key="item.adid"
            :label="item.adname"
            :value="item.adname"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="采购人：">
        <el-select
          v-model="buyer"
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
          ><i class="el-icon-circle-plus-outline"></i>新增采购申请单</el-button
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
      <el-table-column prop="buyid" label="采购单编号" width="220">
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
                  adname.role === 'ROLE_ADMIN' && scope.row.buyStatus === null
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
    <!-- 新增采购申请单 -->
    <el-dialog
      :visible.sync="dialogFormVisible"
      :fullscreen="full"
      :show-close="dd"
      :close-on-click-modal="aa"
    >
      <template slot="title">
        <div style="font-size: 25px; font-weight: bold">新增采购单</div>
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
          label="采购单编号"
          class="redss"
          style="margin-left: 50px"
          prop="buynewid"
        >
          <el-input
            v-model="form1.buynewid"
            autocomplete="off"
            style="width: 300px"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="采购日期" style="margin-left: 70px" prop="buyDate">
          <el-date-picker
            v-model="form1.buyDate"
            align="right"
            type="date"
            placeholder="选择采购日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="申请人" style="margin-left: 70px" prop="buyApply">
          <el-select
            v-model="form1.buyApply"
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
        <el-form-item label="采购人" style="margin-left: 70px" prop="buyer">
          <el-select
            v-model="form1.buyer"
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
        <div class="personal-mybuluo-head">
          <div class="personal-mybuluo-wording">采购单明细</div>
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>
        <div>
          <div style="margin-top: 200px" class="headerr">
            <div style="width: 400px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">采购商品</h3>
            </div>
            <div style="width: 400px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">供应商</h3>
            </div>
            <div style="width: 400px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">采购数</h3>
            </div>
            <div style="width: 350px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">单位</h3>
            </div>
            <div style="width: 300px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">单价</h3>
            </div>
          </div>
          <div style="width: 100%; height: 50px; border-bottom: 1px solid #eee">
            <el-form-item
              style="width: 280px; margin-top: 10px; float: left"
              prop="buyName"
            >
              <el-select
                style="width: 280px"
                v-model="form1.buyName"
                placeholder="请选择采购商品"
              >
                <el-option
                  v-for="item in productData"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item style="width: 350px; float: left" prop="buySupplier">
              <el-select
                style="
                  width: 350px;
                  margin-top: 10px;
                  float: left;
                  margin-left: 10px;
                "
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
            <el-form-item style="width: 400px; float: left" prop="buyCount">
              <el-input-number
                placeholder="采购数量"
                style="
                  width: 390px;
                  margin-top: 10px;
                  float: left;
                  margin-left: 10px;
                "
                v-model="form1.buyCount"
              ></el-input-number>
            </el-form-item>
            <el-form-item style="width: 400px; float: left" prop="buyUnit">
              <el-cascader
                v-model="form1.buyUnit"
                placeholder="选择数量单位"
                style="
                  width: 290px;
                  margin-top: 10px;
                  float: left;
                  margin-left: 10px;
                "
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
            <el-form-item style="width: 400px; float: left" prop="buyPrice">
              <el-input-number
                placeholder="请输入单价"
                style="
                  width: 390px;
                  margin-top: 10px;
                  float: left;
                  margin-left: -90px;
                "
                v-model="form1.buyPrice"
              ></el-input-number>
            </el-form-item>

            <el-form-item
              v-if="false"
              style="width: 400px; margin-top: 10px; float: left"
            >
              <el-input
                style="width: 100%"
                v-model="form1.buyStatus"
              ></el-input>
            </el-form-item>
          </div>
        </div>
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
        <div style="font-size: 25px; font-weight: bold">修改采购申请单</div>
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
          label="采购单编号"
          prop="buyid"
          class="redss"
          style="margin-left: 50px"
        >
          <span>{{ form1.buyid }}</span>
        </el-form-item>
        <el-form-item label="采购日期" prop="buyDate" style="margin-left: 70px">
          <el-date-picker
            v-model="form1.buyDate"
            align="right"
            type="date"
            placeholder="选择日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="申请人" prop="buyApply" style="margin-left: 70px">
          <el-select
            v-model="form1.buyApply"
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
        <el-form-item label="采购人" prop="buyer" style="margin-left: 70px">
          <el-select
            v-model="form1.buyer"
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
        <div class="personal-mybuluo-head">
          <div class="personal-mybuluo-wording">采购单明细</div>
          <div class="personal-border jmu-border-1px border-bottom"></div>
        </div>

        <div>
          <div style="margin-top: 200px" class="headerr">
            <div style="width: 400px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">采购商品</h3>
            </div>
            <div style="width: 400px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">供应商</h3>
            </div>
            <div style="width: 400px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">采购数</h3>
            </div>
            <div style="width: 350px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">单位</h3>
            </div>
            <div style="width: 300px; height: 40px; float: left">
              <h3 style="padding-top: 10px; font-size: 15px">单价</h3>
            </div>
          </div>
          <div style="width: 100%; height: 50px; border-bottom: 1px solid #eee">
            <el-form-item style="width: 330px; float: left" prop="buyName">
              <el-select
                style="width: 280px; margin-top: 10px"
                v-model="form1.buyName"
                placeholder="请选择采购商品"
              >
                <el-option
                  v-for="item in productData"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name"
                >
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item style="width: 300px; float: left" prop="buySupplier">
              <el-select
                style="
                  width: 300px;
                  margin-top: 10px;
                  float: left;
                  margin-left: 10px;
                "
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

            <el-form-item style="width: 280px; float: left" prop="buyCount">
              <el-input-number
                style="
                  width: 280px;
                  margin-top: 10px;
                  float: left;
                  margin-left: 10px;
                "
                v-model="form1.buyCount"
              ></el-input-number>
            </el-form-item>

            <el-form-item
              style="width: 390px; margin-top: 10px; float: left"
              prop="buyUnit"
            >
              <span style="margin-left: 250px">{{ form1.buyUnit }}</span>
            </el-form-item>

            <el-form-item style="width: 390px; float: left" prop="buyPrice">
              <el-input-number
                style="
                  width: 280px;
                  margin-top: 10px;
                  float: left;
                  margin-left: 70px;
                "
                v-model="form1.buyPrice"
              ></el-input-number>
            </el-form-item>

            <el-input
              v-if="false"
              v-model="form1.buyStatus"
              autocomplete="off"
              style="
                width: 290px;
                margin-top: 10px;
                float: left;
                margin-left: 70px;
              "
            ></el-input>
          </div>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="vis = false">取 消</el-button>
        <el-button type="primary" @click="save1">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看采购申请单 -->
    <el-dialog
      :visible.sync="vis1"
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
        <div style="font-size: 25px; font-weight: bold">查看采购申请单</div>
      </template>
      <el-form
        :model="form1"
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
          <span>{{ form1.buyid }}</span>
        </el-form-item>
        <el-form-item
          label="采购日期："
          prop="buyDate"
          style="margin-left: 270px"
        >
          <span>{{ form1.buyDate | formatDate }}</span>
        </el-form-item>
        <el-form-item
          label="申请人："
          prop="buyApply"
          style="margin-left: 270px"
        >
          <span>{{ form1.buyApply }}</span>
        </el-form-item>
        <el-form-item label="采购人：" prop="buyer" style="margin-left: 270px">
          <span>{{ form1.buyer }}</span>
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
              >{{ form1.buyName }}</span
            >
            <span
              autocomplete="off"
              style="width: 440px; margin-top: 10px; float: left"
              >{{ form1.buySupplier }}</span
            >
            <span
              autocomplete="off"
              style="width: 440px; margin-top: 10px; float: left"
              >{{ form1.buyCount }} / {{ form1.buyUnit }}</span
            >
            <span
              autocomplete="off"
              style="width: 390px; margin-top: 10px; float: left"
              >{{ form1.buyPrice }}</span
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
            </el-button-group>
          </el-form-item>
          <el-form-item v-if="false" style="margin-left: 170px">
            <el-input v-model="pass1"> </el-input>
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
  name: "Buy",
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
      adname: "",
      buyid: "",
      buyName: "",
      buyDate: "",
      buyApply: "",
      buyer: "",
      buyStatus: "",
      startTime: "",
      endTime: "",
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
      buyers: [],
      options: [],
      num: 1,
      value: [],
      form1: {
        buynewid: "33",
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
      rules: {
        buyDate: [{ required: true, message: "日期不能为空", trigger: "blur" }],
        buyApply: [
          {
            required: true,
            message: "申请人不能为空",
            trigger: ["blur", "change"],
          },
        ],
        buyer: [
          {
            required: true,
            message: "采购人不能为空",
            trigger: ["blur", "change"],
          },
        ],
        buyName: [
          {
            required: true,
            message: "采购商品不能为空",
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
            message: "采购数量不能为空",
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
        buyPrice: [
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

    this.form1.buynewid =
      "CG" + this.getProjectNum() + Math.floor(Math.random() * 10000);
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
        .get("/buy/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name: this.name,
            smanager: this.smanager,
            buyid: this.buyid,
            buyApply: this.buyApply,
            buyer: this.buyer,
            buyName: this.buyName,
            buyStatus: this.buyStatus,
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

      this.request.get("/product/").then((res) => {
        this.productData = res.data;
      });
    },
    onInput() {
      this.$forceUpdate();
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
              "/buy",
              (this.form1 = {
                buyid: this.form1.buynewid, //编号
                buyDate: this.form1.buyDate, //日期
                buyApply: this.form1.buyApply, //申请人
                buyer: this.form1.buyer, //采购人
                buyName: this.form1.buyName, //采购商品
                buySupplier: this.form1.buySupplier, //供应商
                buyCount: this.form1.buyCount, //采购数
                buyUnit: this.form1.buyUnit,
                buyPrice: this.form1.buyPrice,
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
    //编辑数据
    save1() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //表单校验合法
          this.request
            .post(
              "/buy",
              (this.form1 = {
                id: this.form1.id,
                buyid: this.form1.buyid, //编号
                buyDate: this.form1.buyDate, //日期
                buyApply: this.form1.buyApply, //申请人
                buyer: this.form1.buyer, //采购人
                buyName: this.form1.buyName, //采购商品
                buySupplier: this.form1.buySupplier, //供应商
                buyCount: this.form1.buyCount, //采购数
                buyUnit: this.form1.buyUnit,
                buyPrice: this.form1.buyPrice,
                buyStatus: this.form1.buyStatus,
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
    //新增弹窗  新增采购单
    handleAddbuy() {
      this.dialogFormVisible = true;
      this.form1 = {};
      this.form1.buyStatus = "未通过";
      this.form1.buynewid =
        "CG" + this.getProjectNum() + Math.floor(Math.random() * 10000);
    },
    //查看采购申请单
    lookapply(row) {
      this.vis1 = true;
      this.form = JSON.parse(JSON.stringify(row)); //可以解决编辑未确认就改变的问题
    },
    //修改审核单弹出框
    handleEdit(row) {
      this.vis = true;

      this.form1 = JSON.parse(JSON.stringify(row));
    },
    //审核（由管理员执行）
    handleCheck(row) {
      this.vis2 = true;

      this.form1 = JSON.parse(JSON.stringify(row));
    },

    //审核
    adminCheck() {
      if (this.pass1) {
        this.request
          .post(
            "/buy",
            (this.form1 = {
              id: this.form1.id,
              reviewer: this.adname.adname,
              reviewdate: this.nowtime,
              buyStatus: this.pass1,
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
    //审核通过
    passAll() {
      this.pass1 = this.pass;
      console.log(this.pass1);
    },
    //审核未通过
    nopassAll() {
      this.pass1 = this.nopass;
    },
    //删除
    handleDel(id) {
      this.request.delete("/buy/" + id).then((res) => {
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
      this.request.post("/buy/del/batch", ids).then((res) => {
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
      this.buyid = "";
      this.buyName = "";
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
      window.open("http://localhost:9090/buy/export");
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