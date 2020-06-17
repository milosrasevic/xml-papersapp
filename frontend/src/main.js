import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';
import Axios from 'axios';

Vue.config.productionTip = false

function configureHttp () {
  //TODO ovde treba dinamicki proslediti na kom portu je CA
  Axios.defaults.baseURL = "http://localhost:8088/api/"
  Axios.defaults.headers.Accept = 'application/json'
  Axios.defaults.headers['Access-Control-Allow-Origin'] = '*'
}

configureHttp()

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
