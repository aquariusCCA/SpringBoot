<template>
  <div>
    <div>
      <label for="orderNo">订单号：</label>
      <input id="orderNo" v-model="currentOrderNo" />
      <button @click="fetchOrderInfo">查询</button>
    </div>
    <div v-if="!loading && orderInfo" class="logistics-info">
      <MyGood :good="orderInfo.goodInfo">
        <template v-slot:desc>
            <img style="width: 140px" :src="orderInfo.goodInfo.desc">
        </template>
      </MyGood>
      <LogisticsInfo :orderInfo="orderInfo"></LogisticsInfo>
    </div>
  </div>
</template>

<script>
import MyGood from './MyGood.vue'
import LogisticsInfo from './LogisticsInfo.vue'

export default {
  components: {
    MyGood,
    LogisticsInfo
  },
  data() {
    return {
      currentOrderNo: '',
      orderInfo: null,
      loading:false
    }
  },
  methods: {
    fetchOrderInfo() {
      if (!this.currentOrderNo.trim()) {
        return alert('请输入订单号')
      }
      this.loading = true
      // 模拟请求后端数据
      setTimeout(() => {
        this.loading = false
        this.orderInfo = {
          orderNo: this.currentOrderNo,
          role: 'admin',
          logisticsStatus: 2, // 0 未发货 1 已发货 2 已签收 3 未签收
          logisticsCompany: '顺丰速运',
          logisticsNo: 'SF123456789',
          logisticsInfo: [
            {
              time: '2023-02-01 10:00:00',
              content: '快件已发货'
            },
            {
              time: '2023-02-02 09:00:00',
              content: '快件到达深圳中心'
            },
            {
              time: '2023-02-03 08:00:00',
              content: '快件派送中'
            },
            {
              time: '2023-02-04 16:00:00',
              content: '已签收'
            }
          ],
          goodInfo: {
            goodNo: '12345678912312',
            goodName: '三体',
            type: 0, // 0 代表的是文本 1代表的是 图片
            desc: '	http://registakeaway.itheima.net/common/download?name=9b978122-179a-4b0e-891b-595d0cd3d875.jpg'
          }
        }
      }, 1000)
    }
  }

}
</script>
<style scoped>
  .logistics-info {
    border: 1px solid #ccc;
    border-radius: 5px;
    box-shadow: 0px 0px 10px #eee;
    padding: 10px;
    font-size: 14px;
  }
</style>
