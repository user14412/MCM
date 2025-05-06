import HomeIndexView from '@/views/home/HomeIndexView.vue'
import NotFound from '@/views/error/NotFound.vue'
import { createRouter, createWebHistory } from 'vue-router'
import UserProfileIndexView from '@/views/user/profile/index/UserProfileIndexView.vue'
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView.vue'
import NewsIndexView from '@/views/introduction/NewsIndexView.vue'
import IntroductionIndexView from '@/views/introduction/IntroductionIndexView.vue'
import VisitGuideIndexView from '@/views/introduction/VisitGuideIndexView.vue'
import ArticleIndexView from '@/views/introduction/ArticleIndexView.vue'
import TestView from '@/views/error/TestView.vue'
import ExhibitIndexView from '@/views/exhibit/ExhibitIndexView.vue'
import PanoramaIndexView from '@/views/panorama/PanoramaIndexView.vue'
import SurveyIndexView from '@/views/feedback/SurveyIndexView.vue'
import FeedbackIndexView from '@/views/feedback/FeedbackIndexView.vue'
import ExhibitDetailsIndexView from '@/views/exhibit/ExhibitDetailsIndexView.vue'
import UserProfileDetailsIndexView from '@/views/user/profile/index/UserProfileDetailsIndexView.vue'
import SurveyDetailsIndexView from '@/views/feedback/SurveyDetailsIndexView.vue'
import PanoramaView from '@/views/panorama/PanoramaView.vue'
import AdminLayout from '@/views/admin/AdminLayout.vue'

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
    component: HomeIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/article/:articleId/",
    name: "article_index",
    component: ArticleIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/introduction/",
    name: "introduction_index",
    component: IntroductionIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/news/",
    name: "news_index",
    component: NewsIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/guide/",
    name: "visit_guide_index",
    component: VisitGuideIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/exhibit/",
    name: "exhibit_index",
    component: ExhibitIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/exhibit/:exhibitId/",
    name: "exhibit_details_index",
    component: ExhibitDetailsIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/panorama/",
    name: "panorama_index",
    component: PanoramaIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/panorama/:sceneId/",
    name: "PanoramaView",
    component: PanoramaView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/survey/",
    name: "survey_index",
    component: SurveyIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/survey/:surveyId/",
    name: "survey_details_index",
    component: SurveyDetailsIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/feedback/",
    name: "feedback_index",
    component: FeedbackIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/user/profile/",
    name: "user_profile_index",
    component: UserProfileIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/user/profile/:userId/",
    name: "user_profile_details_index",
    component: UserProfileDetailsIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: "/test/",
    name: "test",
    component: TestView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: '/admin',
    component: AdminLayout,
    meta: {
      requestAuth: true,
      requireAdmin: true
    },
    children: [
      {
        path: '',
        redirect: '/admin/users'
      },
      {
        path: 'users',
        name: 'admin_users',
        component: () => import('@/views/admin/components/UserManagement.vue')
      },
      {
        path: 'exhibits',
        name: 'admin_exhibits',
        component: () => import('@/views/admin/components/ExhibitManagement.vue')
      },
      {
        path: 'surveys',
        name: 'admin_surveys',
        component: () => import('@/views/admin/components/SurveyManagement.vue')
      },
      {
        path: 'feedbacks',
        name: 'admin_feedbacks',
        component: () => import('@/views/admin/components/FeedbackManagement.vue')
      },
      {
        path: 'messages',
        name: 'admin_messages',
        component: () => import('@/views/admin/components/MessageManagement.vue')
      },
      {
        path: 'statistics',
        name: 'admin_statistics',
        component: () => import('@/views/admin/components/StatisticsView.vue')
      }
    ]
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

// 添加路由守卫：
router.beforeEach((to, from, next) => {
  const store = require('@/store').default; // 避免循环引入
  // 如果需要授权&&未登录，跳转到登录页面
  if (to.meta.requestAuth && !store.state.user.is_login) {
    console.log("+++");
    next({name: "user_account_login"})
  } else {
    // 不需要授权||已登录直接打开原页面
    next();
  }
});

export default router
