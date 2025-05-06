<template>
  <div class="admin-layout">
    <div class="admin-sidebar">
      <el-menu
        :default-active="activeMenu"
        class="admin-menu"
        @select="handleMenuSelect"
      >
        <el-menu-item index="users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="exhibits">
          <el-icon><Picture /></el-icon>
          <span>展品管理</span>
        </el-menu-item>
        <el-menu-item index="messages">
          <el-icon><ChatDotRound /></el-icon>
          <span>留言管理</span>
        </el-menu-item>
        <el-menu-item index="surveys">
          <el-icon><Document /></el-icon>
          <span>问卷管理</span>
        </el-menu-item>
        <el-menu-item index="feedbacks">
          <el-icon><MessageBox /></el-icon>
          <span>反馈管理</span>
        </el-menu-item>
        <el-menu-item index="statistics">
          <el-icon><DataLine /></el-icon>
          <span>数据统计</span>
        </el-menu-item>
      </el-menu>
    </div>
    <div class="admin-content">
      <component :is="currentComponent" />
    </div>
  </div>
</template>

<script setup>
import { computed, markRaw, defineAsyncComponent } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { User, Picture, Document, ChatDotRound, DataLine, MessageBox } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 动态导入组件
const components = {
  users: markRaw(defineAsyncComponent(() => import('./components/UserManagement.vue'))),
  exhibits: markRaw(defineAsyncComponent(() => import('./components/ExhibitManagement.vue'))),
  surveys: markRaw(defineAsyncComponent(() => import('./components/SurveyManagement.vue'))),
  feedbacks: markRaw(defineAsyncComponent(() => import('./components/FeedbackManagement.vue'))),
  messages: markRaw(defineAsyncComponent(() => import('./components/MessageManagement.vue'))),
  statistics: markRaw(defineAsyncComponent(() => import('./components/StatisticsView.vue')))
}

const activeMenu = computed(() => route.path.split('/').pop())
const currentComponent = computed(() => components[activeMenu.value])

const handleMenuSelect = (index) => {
  router.push(`/admin/${index}`)
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
  padding-top: 22px;
}

.admin-sidebar {
  width: 200px;
  height: 100vh;
  background-color: #fff;
  border-right: 1px solid #e6e6e6;
}

.admin-menu {
  height: 100%;
  border-right: none;
}

.admin-content {
  flex: 1;
  height: 100vh;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
}
</style> 