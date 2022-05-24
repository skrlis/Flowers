<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 40px">
      <el-col :span="6">
        <el-card style="color: #409eff">
          <div><i class="el-icon-user-solid" />入库单</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ expendnum }}单
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #f56c6c">
          <div><i class="el-icon-money" />进货总金额</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            ￥{{ expend }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67c23a">
          <div><i class="el-icon-bank-card" />进货总支付</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            ￥{{ expend }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #e6a23c">
          <div><i class="el-icon-s-shop" />进货总欠款</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            ￥0
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
        <el-button style="margin-left: 5px" type="info" @click="reset"
          >重置</el-button
        >
      </el-form>
      <el-table :data="tableData" border show-summary style="width: 100%">
        <el-table-column prop="putId" label="ID" width="180"> </el-table-column>
        <el-table-column prop="putTotal" sortable label="总金额">
        </el-table-column>
        <el-table-column prop="putTotal" sortable label="已付款">
        </el-table-column>
      </el-table>

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
      retail: "", //零售客户
      wholeretail: "", //批发客户
      expend: "", //采购支出
      income: "", //销售总额
      volume: "", //销量
      expendnum: "",
      tableData: [],
      startTime: "",
      endTime: "",
      buyid: "",
      buyApply: "",
      pageNum: 1,
      pageSize: 5,
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
        text: "入库信息统计",
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
          color: "#7ec7ff",
          interval: 0,
          formatter: function (params) {
            var time = new Date(params);
            var y = time.getFullYear();
            var m = time.getMonth() + 1;
            var d = time.getDate();
            var h = time.getHours();
            var mm = time.getMinutes();
            var s = time.getSeconds();
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
          name: "采购数量",
          data: [],
          type: "line",
          smooth: true,
        },
        {
          name: "采购金额",
          data: [],
          type: "line",
          smooth: true,
        },
      ],
    };

    this.request.get("/put/findwholput").then((res) => {
      this.expendnum = res.data;

      // if(this.res.data != undefined || this.res.data.length >0){
      //        for (var i = 0; i < res1.data.length; i++) {
      //     var item = res1.data[i];
      //   option1.yAxis.data.push(item.name);
      //    option1.series[0].data.push(item.proSales);
    });

    this.request.get("/echarts/members3").then((res1) => {
      for (var i = 0; i < res1.data.length; i++) {
        var item = res1.data[i];
        option.xAxis.data.push(item.putDate);
        option.series[0].data.push(item.putNum);
        option.series[1].data.push(item.putTotal);

        // if(this.res.data != undefined || this.res.data.length >0){
        //        for (var i = 0; i < res1.data.length; i++) {
        //     var item = res1.data[i];
        //   option1.yAxis.data.push(item.name);
        //    option1.series[0].data.push(item.proSales);
      }
      option && myChart.setOption(option);
    });
    //  myChart1.setOption(option1);
    //如果数据量大时，可能会因为数据加载不完全就渲染图表，会引发报错，因此可以使用setTimeout进行延时处理

    //    this.request.get("/echarts/members").then(res =>{
    //      console.log(res.data)
    //         //填空
    //         // option.xAxis.data=res.data.x
    //         option.series[0].data=res.data
    //         //在数据准备完毕之后再set
    //         myChart.setOption(option);

    //         }  )

    this.request.get("/put/findsum").then((res) => {
      this.expend = res.data;
    });

    //如果数据量大时，可能会因为数据加载不完全就渲染图表，会引发报错，因此可以使用setTimeout进行延时处理
  },
  created() {
    this.load();
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
        });
    },
    reset() {
      this.startTime = "";
      this.endTime = "";
      this.load();
    },
  },
};
</script>

<style>
</style>