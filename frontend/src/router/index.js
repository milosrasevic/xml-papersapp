import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Logout from '../views/Logout.vue'
import SignIn from '../views/SignIn.vue'
import CreateReview from '../views/CreateReview.vue'
import AuthorsProfile from "../views/AuthorsProfile";
import SubmitPaperAndLetter from "../views/SubmitPaperAndLetter";
import AssignReviewers from "../views/AssignReviewers";
import AuthorReviewAssigments from "../views/AuthorReviewAssigments";
import SciencePaperReviews from "../views/SciencePaperReviews";

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
  },
  {
    path: '/profile',
    name: 'AuthorsProfile',
    component: AuthorsProfile
  },
  {
    path: '/submit-paper-and-letter',
    name: 'SubmitPaperAndLetter',
    component: SubmitPaperAndLetter
  },
  {
    path: '/assign-reviewers/:sciencePaperTitle',
    name: 'AssignReviewers',
    component: AssignReviewers
  },
  {
    path: '/myAssigments',
    name: 'AuthorReviewAssigments',
    component: AuthorReviewAssigments
  },
  {
    path: '/science-paper/:id/:title/reviews',
    name: 'SciencePaperReviews',
    component: SciencePaperReviews
  }
  
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
