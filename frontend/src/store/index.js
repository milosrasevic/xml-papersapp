import Vue from 'vue';
import Vuex from 'vuex';
// import { auth } from './modules';
import createPersistedState from 'vuex-persistedstate'
// import * as Cookies from 'js-cookie'

Vue.use(Vuex)

// const storeData = {
//   modules: {
//     auth,
//   }
// }

// export default new Vuex.Store({
//   storeData
// });

export default new Vuex.Store({
  plugins: [createPersistedState()],
  state: {
    userLoggedIn: false,
    userId: 0,
    user: {}
  },
  
  getters: {
    isNonAuthUser: state => !state.userLoggedIn,
    isAuthor: state => state.user ? state.user.role === "ROLE_AUTHOR" : false,
    isEditor: state => state.user ? state.user.role === "ROLE_EDITOR" : false
  },
  
  mutations: {
    login(state) {
      state.userLoggedIn = true;
    },
    logout(state) {
      state.userLoggedIn = false;
      state.user = {}
    },
    setUserId(state, id){
      state.userId = id;
    },
    setUser(state, user){
      state.user = user;
    }
  },
  
  actions: {
    login(context) {
      context.commit('login')
    },
    logout(context) {
      context.commit('logout')
    },
    setUserId(context, id){
      context.setUserId(id);
    },
    setUser(context, user){
      context.setUser(user);
    },
  }
});