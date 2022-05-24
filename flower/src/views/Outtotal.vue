<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 40px">
      <el-col :span="6">
        <el-card style="color: #409eff">
          <div><i class="el-icon-user-solid" />出库单</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ volume }}单
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #f56c6c">
          <div><i class="el-icon-money" />出库总金额</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            ￥{{ income }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67c23a">
          <div><i class="el-icon-bank-card" />出库总支付</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            ￥{{ paid }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #e6a23c">
          <div><i class="el-icon-s-shop" />出库总欠款</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            ￥{{ unpaid }}
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-form
        :inline="true"
        label-width="100px"
        size="small"
        class="demo-form-inline"
      >
        <el-form-item label="开始日期：">
          <el-date-picker
            v-model="startTime"
            align="right"
            type="date"
            placeholder="选择日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期：">
          <el-date-picker
            v-model="endTime"
            align="right"
            type="date"
            placeholder="选择日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-button
          style="margin-left: 50px"
          type="primary"
          @click="load"
          icon="Search"
          >查询
        </el-button>
        <el-button type="info" @click="reset">重置</el-button>
      </el-form>
      <el-table :data="tableData" border show-summary style="width: 100%">
        <el-table-column prop="outId" label="ID" width="180"> </el-table-column>
        <el-table-column prop="outTotal" sortable label="总金额">
        </el-table-column>
        <el-table-column prop="outPaid" sortable label="已付款">
        </el-table-column>
        <el-table-column prop="outProfit" sortable label="已付款">
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
          :total="total1"
        >
        </el-pagination>
      </div>
      <el-col :span="12">
        <div
          id="main"
          style="width: 1500px; height: 400px; margin-top: 90px"
        ></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  name: "Puttotal",
  data() {
    return {
      // retail:'',//零售客户
      // wholeretail:'',//批发客户
      income: "", //总花费
      volume: "", //销量
      paid: "", //总支付
      unpaid: "", //总花费-总支付
      tableData: [],
      startTime: "",
      endTime: "",
      buyid: "",
      buyApply: "",
      pageNum: 1,
      pageSize: 5,
      total1: 0,
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
    };
  },
  mounted() {
    //页面元素渲染之后再触发

    var chartDom = document.getElementById("main");
    var myChart = echarts.init(chartDom);
    var option;

    option = {
      title: {
        text: "出库信息统计",
        left: "center",
      },
      tooltip: {
        trigger: "item",
      },
      legend: {
        orient: "vertical",
        left: "left",
      },
      xAxis: {
        type: "category",
        data: [],
        axisLine: {
          show: true,
          lineStyle: {
            color: "3233653",
          },
        },
        axisLabel: {
          // color:'#7ec7ff',
          interval: 0,
          formatter: function (params) {
            var time = new Date(params);
            var y = time.getFullYear();
            var m = time.getMonth() + 1;
            var d = time.getDate();
            return (
              y + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d)
            );
          },
          formatDate(time) {
            return [
              time.getFullYear(),
              echarts.time.getMonth() + 1,
              echarts.time.getDate(),
            ].join("-");
          },
        },
      },
      yAxis: {
        type: "value",
      },
      series: [
        {
          name: "出库数量",
          data: [],
          type: "line",
          smooth: true,
        },
        {
          name: "出库金额",
          data: [],
          type: "line",
          smooth: true,
        },
      ],
    };
    this.request.get("/out/findwholesale").then((res) => {
      this.volume = res.data;
    });
    this.request.get("/echarts/members4").then((res1) => {
      for (var i = 0; i < res1.data.length; i++) {
        var item = res1.data[i];
        option.xAxis.data.push(item.outDate);
        option.series[0].data.push(item.outNum);
        option.series[1].data.push(item.outTotal);
      }
      option && myChart.setOption(option);
    });
    this.request.get("/out/findsum").then((res) => {
      this.income = res.data;
    });

    this.request.get("/out/findpaid").then((res) => {
      this.paid = res.data;
      this.unpaid = this.income - this.paid;
    });
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      this.request
        .get("/out/page1", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            startTime: this.startTime,
            endTime: this.endTime,
          },
        })
        .then((res) => {
          this.tableData = res.data.records;
          this.total1 = res.data.total;
          console.log(this.tableData);
        });
    },
    reset() {
      this.startTime = "";
      this.endTime = "";
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

<style>
</style>