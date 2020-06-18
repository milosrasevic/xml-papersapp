import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Logout from '../views/Logout.vue'
import SignIn from '../views/SignIn.vue'
import CreateReview from '../views/CreateReview.vue'

Vue.use(VueRouter)

  const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/logout',
    name: 'Logout',
    component: Logout
  },
  {
    path: '/signup',
    name: 'SignIn',
    component: SignIn
  },
  {
    path: '/createReview/:scipaperTitle',
    // path: '/createReview',
    name: 'CreateReview',
    component: CreateReview
  }
  
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
