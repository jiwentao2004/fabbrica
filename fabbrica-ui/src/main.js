import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import App from './App.vue'
import './quasar'
import router from './router'
import store from './store'

Vue.config.productionTip = false

Vue.use(VueAxios, axios);

const accessToken = localStorage.getItem('access_token');
if (accessToken) {
  Vue.prototype.$http.defaults.headers.common.Authorization = 'Bearer ' + accessToken;
}
Vue.prototype.$http.defaults.baseURL = 'http://127.0.0.1:8080/api/v1';

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
