import Axios from 'axios';
// import Vue from 'vue';
import store from '@/store';
import { User } from '../model/User';
import * as _ from 'lodash';

const AUTH_HEADER = 'Authorization';

const ENDPOINTS = {
    LOGIN: 'auth/login',
    REGISTER: 'auth/register'
}

export default {

    setLocalStorageAuthData(data) {
        localStorage.setItem('accessToken',data.token);  
      },

    setAuthHeader(unset = false) {
        if (unset) {
          delete Axios.defaults.headers[AUTH_HEADER];
          return;
        }
        _.assign(
          Axios.defaults.headers,
          {'Authorization' : 'Bearer ' + localStorage.getItem('accessToken')}
        );
      },

    //   initStoreAuth() {
    //     const userData = JSON.parse(localStorage.getItem('user'));
    //     if (userData) {
    //       this.setAuthHeader();
    //       store.commit('auth', userData);
    //       return LoginApiService.refreshUser().then((response) => {
    //         if (response.data) {
    //           this.updateUserInLocalStorage(response.data);
    //         }
    //       });
    //     }
    //     return this.setAuthHeader(true);
    //   },

    login(bodyInfo) {
        return Axios.post(ENDPOINTS.LOGIN, bodyInfo).then((response) => {
            var user = new User();
            Object.assign(user, response.data);
            this.setLocalStorageAuthData(user);
            this.setAuthHeader();
            store.commit('login');
            store.commit('setUser', response.data);
            return response;
        });
      },

    register(registration){
        return Axios.post(ENDPOINTS.REGISTER, registration)
    },

    logout() {
      this.setLocalStorageAuthData({
        accessToken: null,
        userId: null,
      });
      this.setAuthHeader(true);
      store.commit('logout');
    },
  
}

Axios.interceptors.request.use(
  (config) => {
    let token = localStorage.getItem('accessToken');

    if (token) {
      config.headers['Authorization'] = `Bearer ${ token }`;
    }

    return config;
  }, 

  (error) => {
    return Promise.reject(error);
  }
);