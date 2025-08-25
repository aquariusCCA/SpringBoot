<template>
  <div>
    <h2>商品相关信息</h2>
    <div>
      <div>商品id: <strong>{{good.goodNo}}</strong></div>
      <div>商品名称: <strong>{{good.goodName}}</strong></div>
      <div>商品描述: <slot name="desc">{{good.desc}}</slot></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'VueBaseMyGood',
  props: {
    good: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      
    };
  }
};
</script>

<style lang="scss" scoped>

</style>