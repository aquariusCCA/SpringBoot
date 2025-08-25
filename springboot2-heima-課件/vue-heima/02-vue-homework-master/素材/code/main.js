// webpack 打包的入口文件
import Vue from "vue"; // 引入Vue
import App from "./App.vue"; // 引入跟组件

Vue.config.productionTip = false; //  提示语

// 滚动到底部自定义指令
Vue.directive('per', {
  inserted: function(el, binding) {
    const flag = ['admin', 'zhangsan', 'lisi'].includes(binding.value)
    flag ? el.style.display = "block":el.style.display = "none"
  },
})

new Vue({
  // 生成一个实例
  render: (h) => h(App), // createElement
  // 渲染app
}).$mount("#app");
