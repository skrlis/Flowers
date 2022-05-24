<template>
  <div>
    <el-row :gutter="10" style="margin-bottom:40px">
      <el-col :span="6">
        <el-card style="color:#409eff">
          <div><i class="el-icon-user-solid"/>客户总数</div>
          <div style="padding:10px 0;text-align:center;font-weight:bold">
           零售：{{retail}}  批发：{{wholeretail}}
          </div>

        </el-card>
      </el-col>
      <el-col :span="6">
       <el-card style="color:#f56c6c">
           <div><i class="el-icon-money"/>销售总量</div>
           <div style="padding:10px 0;text-align:center;font-weight:bold">
            {{volume}}单
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
         <el-card style="color:#67c23a">
          <div><i class="el-icon-bank-card"/>收益总额</div>
          <div style="padding:10px 0;text-align:center;font-weight:bold">
           ￥{{income}}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color:#e6a23c">
          <div><i class="el-icon-s-shop"/>采购支出</div>
          <div style="padding:10px 0;text-align:center;font-weight:bold">
            ￥{{expend}}
          </div>
        </el-card>
        
      </el-col>

    </el-row>
    <el-row >
      <el-col :span="12">
      <div id="main" style="width:500px;height:500px"></div>
        
      </el-col>
       <el-col :span="12">
      <div id="main1"  style="width:500px;height:500px"></div>
        
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
        retail:'',//零售客户
        wholeretail:'',//批发客户
        expend:'',//采购支出
        income:'',//销售总额
        volume:''//销量
      }
    },
    mounted() {//页面元素渲染之后再触发
    

   var chartDom1 = document.getElementById('main1');
      var myChart1 = echarts.init(chartDom1);
      var option1 = {
         title: {
        text: '热销花卉统计',
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
           type: 'value'
        },
        yAxis: {
          type: 'category',
          data: []
        },
        series: [
          {
            // name:"批发",
            data: [],
            type: 'bar'//柱状图
            // type: 'line'//折线图
          },
        ]
      };

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
        color: ['#c23531','#2f4554', '#61a0a8','#61a0a8'],
        xAxis: {
          type: 'category',
          data: ["第一季度","第二季度","第三季度","第四季度"]
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [],
            type: 'bar',//柱状图
            itemStyle: {
            normal: {
　　　　　　　　//这里是重点
                color: function(params) {
                	//注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                    var colorList = ['#d48265', '#91c7ae','#749f83', '#ca8622'];
                    return colorList[params.dataIndex]
                }
            }
            },}
        ]
      };



 

  this.request.get("/echarts/members1").then(res1 =>{
    console.log(res1.data)
    // if(this.res.data != undefined || this.res.data.length >0){
       for (var i = 0; i < res1.data.length; i++) {
    var item = res1.data[i];
  option1.yAxis.data.push(item.name);
   option1.series[0].data.push(item.proSales);
}
 myChart1.setOption(option1);
//如果数据量大时，可能会因为数据加载不完全就渲染图表，会引发报错，因此可以使用setTimeout进行延时处理
})

   this.request.get("/echarts/members").then(res =>{
     console.log(res.data)
        //填空
        // option.xAxis.data=res.data.x 
        option.series[0].data=res.data
        //在数据准备完毕之后再set
        myChart.setOption(option);

  


        }  )

 myChart.setOption(option);




  this.request.get("/customer/findsale").then(res =>{
     this.retail=res.data
      
        }  )
          this.request.get("/customer/findwholesale").then(res =>{
     this.wholeretail=res.data
      
        }  )
        this.request.get("/put/findsum").then(res =>{
       this.expend=res.data
      
        }  )
  this.request.get("/out/findsum").then(res =>{
       this.income=res.data
        }  )
          this.request.get("/out/findwholesale").then(res =>{
       this.volume=res.data
        }  )
//如果数据量大时，可能会因为数据加载不完全就渲染图表，会引发报错，因此可以使用setTimeout进行延时处理
}


}

  

</script>

<style>

</style>