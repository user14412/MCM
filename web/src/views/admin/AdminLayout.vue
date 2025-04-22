<template>
  <div class="admin-layout" style="padding-top: 22px;">
    <div class="admin-sidebar">
      <el-menu
        :default-active="activeMenu"
        class="admin-menu"
        router
      >
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/exhibits">
          <el-icon><Picture /></el-icon>
          <span>展品管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/surveys">
          <el-icon><Document /></el-icon>
          <span>问卷管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/feedbacks">
          <el-icon><ChatDotRound /></el-icon>
          <span>反馈管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/statistics">
          <el-icon><DataLine /></el-icon>
          <span>数据统计</span>
        </el-menu-item>
      </el-menu>
    </div>
    <div class="admin-content" style="margin-top: 0px">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { User, Picture, Document, ChatDotRound, DataLine } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: calc(100vh - 60px);
}

.admin-sidebar {
  width: 200px;
  background-color: #fff;
  border-right: 1px solid #e6e6e6;
}

.admin-menu {
  height: 100%;
  border-right: none;
}

.admin-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
}
</style> 