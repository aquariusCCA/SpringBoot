<template>
  <div>
    <h2>物流相关信息</h2>
    <div class="logistics-info-header">
      <div class="logistics-info-header-left">
        <div class="logistics-status blue">{{{0:'未发货', 1:'已发货', 2:'已签收', 3:'未签收'}[orderInfo.logisticsStatus]}}</div>
        <div class="logistics-company">{{ orderInfo.logisticsCompany }}</div>
        <div class="logistics-no">{{ orderInfo.logisticsNo }}</div>
      </div>
    </div>
    <ul class="logistics-info-list">
      <li v-for="(item, index) in orderInfo.logisticsInfo" :key="index">
        <div class="logistics-info-list-time">{{ item.time }}</div>
        <div class="logistics-info-list-content">{{ item.content }}</div>
      </li>
    </ul>
    <button v-per="orderInfo.role">查看物流详情</button>
  </div>
</template>

<script>

export default {
  name: 'LogisticsInfo',
  props: {
    orderInfo: {
      type: Object,
      required: true
    }
  }
}
</script>

<style>
.logistics-info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.logistics-info-header-left {
  display: flex;
  align-items: center;
}

.logistics-status {
  font-weight: bold;
  font-size: 16px;
  /* color: #007bff; */
}
.blue {
  color: #007bff;
}

.logistics-company,
.logistics-no {
  margin-left: 10px;
  font-size: 14px;
  color: #333;
}

.logistics-info-list {
  list-style: none;
  padding: 0;
  margin: 0;
  max-height: 230px;
  overflow-y: auto;
}

.logistics-info-list li {
  display: flex;
  margin-bottom: 10px;
}

.logistics-info-list-time {
  font-size: 12px;
  color: #999;
  width: 80px;
  text-align: right;
  margin-right: 10px;
}

.logistics-info-list-content {
  font-size: 14px;
  color: #333;
}
</style>
