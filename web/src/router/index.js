import HomeIndexView from '@/views/home/HomeIndexView.vue'
import IntroductionIndexView from '@/views/introduction/IntroductionIndexView.vue'
import NotFound from '@/views/error/NotFound.vue'
import { createRouter, createWebHistory } from 'vue-router'
import UserProfileIndexView from '@/views/user/profile/UserProfileIndexView.vue'

const routes = [
  // 重定向
  {
    path: "/",
    name: "home",
    redirect: "/home/",
  },
  // 将URL绑定到View上
  {
    path: "/home/",
    name: "home_index",
    component: HomeIndexView
  },
  {
    path: "/introduction/",
    name: "introduction_index",
    component: IntroductionIndexView,
  },
  {
    path: "/user/profile/",
    name: "user_profile_index",
    component: UserProfileIndexView,
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
  },
  // 错误处理
  {
    path: "/:catchAll(.*)",
    redirect: "/404/",
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
