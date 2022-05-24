<template>
  <div>
    <el-row :gutter="10" style="margin-bottom:40px">
      <el-col :span="6">
        <el-card style="color:#409eff">
          <div><i class="el-icon-user-solid"/>用户总数</div>
          <div style="padding:10px 0;text-align:center;font-weight:bold">
            100
          </div>

        </el-card>
      </el-col>
      <el-col :span="6">
       <el-card style="color:#f56c6c">
           <div><i class="el-icon-money"/>销售总量</div>
           <div style="padding:10px 0;text-align:center;font-weight:bold">
            100000
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
         <el-card style="color:#67c23a">
          <div><i class="el-icon-bank-card"/>收益总额</div>
          <div style="padding:10px 0;text-align:center;font-weight:bold">
            ￥1009989
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color:#e6a23c">
          <div><i class="el-icon-s-shop"/>门店总数</div>
          <div style="padding:10px 0;text-align:center;font-weight:bold">
            100
          </div>
        </el-card>
        
      </el-col>

    </el-row>
    <el-row >
      <el-col :span="12">
      <div id="main" style="width:500px;height:400px"></div>
        
      </el-col>
       <el-col :span="12">
      <div id="pie"  style="width:500px;height:400px"></div>
        
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
    name:"Home",
    data() {
      return {
        
      }
    },
    mounted() {//页面元素渲染之后再触发
      var chartDom = document.getElementById('main');
      var myChart = echarts.init(chartDom);
      var option = {
         title: {
        text: '各季度客户数量统计',
        subtext: '趋势图',
        left: 'center'
      },
        tooltip: {
        trigger: 'item'
      },
        legend: {
        orient: 'vertical',
        left: 'left'
      },
        xAxis: {
          type: 'category',
          data: ["第一季度","第二季度","第三季度","第四季度"]
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name:"批发",
            data: [],
            type: 'bar'//柱状图
            // type: 'line'//折线图
          },
           {
            name:"零售",
            data: [],
            type: 'bar'//柱状图
          }
        ]
      };


      //饼图
       var pieDom = document.getElementById('pie');
      var pieChart = echarts.init(pieDom);
      var pieOption = {
      title: {
        text: '各季度会员数量统计',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          // name: 'Access From',
          type: 'pie',
          radius: '50%',
          label:{//饼图图形上的文本标签
          normal:{
            show:true,
            position:'inner',//标签的位置
            textStyle:{
              fontWeight:300,
              fontSize:16,//文字的字体大小
              color:"#fff"
            },
            formatter:'{d}%'//显示比例
          }

          },
          data: [
            // { value: 1048, name: 'Search Engine' },
            // { value: 735, name: 'Direct' },
            // { value: 580, name: 'Email' },
            // { value: 484, name: 'Union Ads' },
            // { value: 300, name: 'Video Ads' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
};
  this.request.get("/echarts/members").then(res =>{
        //填空
        // option.xAxis.data=res.data.x 
        option.series[0].data=res.data
        option.series[1].data=res.data
        //在数据准备完毕之后再set
        myChart.setOption(option);

        pieOption.series[0].data=[
          {name:"第一季度",value:res.data[0]},
           {name:"第二季度",value:res.data[1]},
            {name:"第三季度",value:res.data[2]},
             {name:"第四季度",value:res.data[3]}
        ]
         pieChart.setOption(pieOption);


        }  )
    },
}
</script>

<style>

</style>