<template>
  <div class="statistics-container">
    <el-row :gutter="20">
      <!-- 用户统计卡片 -->
      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>用户统计</span>
              <el-button type="text" @click="refreshUserStats">刷新</el-button>
            </div>
          </template>
          <div class="stat-content">
            <div class="stat-item">
              <div class="stat-label">总用户数</div>
              <div class="stat-value">{{ userStats.totalUsers }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">管理员</div>
              <div class="stat-value">{{ userStats.adminCount }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">普通用户</div>
              <div class="stat-value">{{ userStats.normalUserCount }}</div>
            </div>
          </div>
          <div class="chart-container">
            <div ref="userChart" style="width: 100%; height: 200px;"></div>
          </div>
        </el-card>
      </el-col>

      <!-- 展品统计卡片 -->
      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>展品统计</span>
              <el-button type="text" @click="refreshExhibitStats">刷新</el-button>
            </div>
          </template>
          <div class="stat-content">
            <div class="stat-item">
              <div class="stat-label">展品总数</div>
              <div class="stat-value">{{ exhibitStats.totalExhibits }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">图片总数</div>
              <div class="stat-value">{{ exhibitStats.totalImages }}</div>
            </div>
          </div>
          <div class="chart-container">
            <div ref="exhibitChart" style="width: 100%; height: 200px;"></div>
          </div>
        </el-card>
      </el-col>

      <!-- 反馈统计卡片 -->
      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>反馈统计</span>
              <el-button type="text" @click="refreshFeedbackStats">刷新</el-button>
            </div>
          </template>
          <div class="stat-content">
            <div class="stat-item">
              <div class="stat-label">反馈总数</div>
              <div class="stat-value">{{ feedbackStats.totalFeedback }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">已解决</div>
              <div class="stat-value">{{ feedbackStats.resolvedCount }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">未解决</div>
              <div class="stat-value">{{ feedbackStats.unresolvedCount }}</div>
            </div>
          </div>
          <div class="chart-container">
            <div ref="feedbackChart" style="width: 100%; height: 200px;"></div>
          </div>
        </el-card>
      </el-col>

      <!-- 问卷统计卡片 -->
      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>问卷统计</span>
              <el-button type="text" @click="refreshSurveyStats">刷新</el-button>
            </div>
          </template>
          <div class="stat-content">
            <div class="stat-item">
              <div class="stat-label">问卷总数</div>
              <div class="stat-value">{{ surveyStats.totalSurveys }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">回答总数</div>
              <div class="stat-value">{{ surveyStats.totalAnswers }}</div>
            </div>
          </div>
          <div class="chart-container">
            <div ref="surveyChart" style="width: 100%; height: 200px;"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import axios from 'axios'
import * as echarts from 'echarts/core'
import { LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'

// 注册必要的组件
echarts.use([
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  LineChart,
  CanvasRenderer
])

const store = useStore()

// 统计数据
const userStats = ref({
  totalUsers: 0,
  adminCount: 0,
  normalUserCount: 0,
  registrationTrend: []
})

const exhibitStats = ref({
  totalExhibits: 0,
  totalImages: 0,
  categoryCount: {},
  exhibitTrend: []
})

const feedbackStats = ref({
  totalFeedback: 0,
  resolvedCount: 0,
  unresolvedCount: 0,
  feedbackTrend: []
})

const surveyStats = ref({
  totalSurveys: 0,
  totalAnswers: 0,
  surveyTrend: []
})

// 图表实例
const userChart = ref(null)
const exhibitChart = ref(null)
const feedbackChart = ref(null)
const surveyChart = ref(null)

// 获取统计数据
const fetchUserStats = async () => {
  try {
    const response = await axios.get('http://localhost:3000/admin/statistics/user/', {
      headers: {
        Authorization: 'Bearer ' + store.state.user.token
      }
    })
    if (response.data.error) {
      console.error('获取用户统计数据失败：', response.data.error)
      return
    }
    userStats.value = response.data
    if (userChart.value) {
      initUserChart()
    }
  } catch (error) {
    console.error('获取用户统计数据失败：', error)
  }
}

const fetchExhibitStats = async () => {
  try {
    const response = await axios.get('http://localhost:3000/admin/statistics/exhibit/', {
      headers: {
        Authorization: 'Bearer ' + store.state.user.token
      }
    })
    if (response.data.error) {
      console.error('获取展品统计数据失败：', response.data.error)
      return
    }
    exhibitStats.value = response.data
    if (exhibitChart.value) {
      initExhibitChart()
    }
  } catch (error) {
    console.error('获取展品统计数据失败：', error)
  }
}

const fetchFeedbackStats = async () => {
  try {
    const response = await axios.get('http://localhost:3000/admin/statistics/feedback/', {
      headers: {
        Authorization: 'Bearer ' + store.state.user.token
      }
    })
    if (response.data.error) {
      console.error('获取反馈统计数据失败：', response.data.error)
      return
    }
    feedbackStats.value = response.data
    if (feedbackChart.value) {
      initFeedbackChart()
    }
  } catch (error) {
    console.error('获取反馈统计数据失败：', error)
  }
}

const fetchSurveyStats = async () => {
  try {
    const response = await axios.get('http://localhost:3000/admin/statistics/survey/', {
      headers: {
        Authorization: 'Bearer ' + store.state.user.token
      }
    })
    if (response.data.error) {
      console.error('获取问卷统计数据失败：', response.data.error)
      return
    }
    surveyStats.value = response.data
    if (surveyChart.value) {
      initSurveyChart()
    }
  } catch (error) {
    console.error('获取问卷统计数据失败：', error)
  }
}

// 初始化图表
const initUserChart = () => {
  if (!userStats.value.registrationTrend) return
  const chart = echarts.init(userChart.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: userStats.value.registrationTrend.map(item => item.date)
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: userStats.value.registrationTrend.map(item => item.count),
      type: 'line',
      smooth: true
    }]
  }
  chart.setOption(option)
}

const initExhibitChart = () => {
  if (!exhibitStats.value.exhibitTrend) return
  const chart = echarts.init(exhibitChart.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: exhibitStats.value.exhibitTrend.map(item => item.date)
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: exhibitStats.value.exhibitTrend.map(item => item.count),
      type: 'line',
      smooth: true
    }]
  }
  chart.setOption(option)
}

const initFeedbackChart = () => {
  if (!feedbackStats.value.feedbackTrend) return
  const chart = echarts.init(feedbackChart.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: feedbackStats.value.feedbackTrend.map(item => item.date)
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: feedbackStats.value.feedbackTrend.map(item => item.count),
      type: 'line',
      smooth: true
    }]
  }
  chart.setOption(option)
}

const initSurveyChart = () => {
  if (!surveyStats.value.surveyTrend) return
  const chart = echarts.init(surveyChart.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: surveyStats.value.surveyTrend.map(item => item.date)
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: surveyStats.value.surveyTrend.map(item => item.count),
      type: 'line',
      smooth: true
    }]
  }
  chart.setOption(option)
}

// 刷新函数
const refreshUserStats = () => {
  fetchUserStats()
}

const refreshExhibitStats = () => {
  fetchExhibitStats()
}

const refreshFeedbackStats = () => {
  fetchFeedbackStats()
}

const refreshSurveyStats = () => {
  fetchSurveyStats()
}

// 组件挂载时加载数据
onMounted(() => {
  fetchUserStats()
  fetchExhibitStats()
  fetchFeedbackStats()
  fetchSurveyStats()
})
</script>

<style scoped>
.statistics-container {
  padding: 24px;
  min-height: 100%;
  background-color: #f5f7fa;
}

.el-row {
  margin: -12px;
  display: flex;
  flex-wrap: wrap;
}

.el-col {
  padding: 12px;
  height: auto;
  min-height: auto;
}

.stat-card {
  height: 500px;
  transition: all 0.3s;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  border-bottom: 1px solid #ebeef5;
}

.card-header span {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.stat-content {
  padding: 20px;
  background: #fff;
  min-height: 200px;
  flex-shrink: 0;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #f8f9fb;
  border-radius: 6px;
  transition: all 0.3s;
  margin-bottom: 8px;
}

.stat-item:hover {
  background: #ecf5ff;
}

.stat-label {
  color: #606266;
  font-size: 14px;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #409eff;
}

.chart-container {
  flex: 1;
  padding: 20px;
  background: #fff;
  border-radius: 0 0 8px 8px;
  min-height: 200px;
  position: relative;
}

.chart-container > div {
  position: absolute;
  top: 20px;
  left: 20px;
  right: 20px;
  bottom: 20px;
}

:deep(.el-card__header) {
  padding: 0;
  border-bottom: none;
}

:deep(.el-card__body) {
  padding: 0;
  flex: 1;
  display: flex;
  flex-direction: column;
}

:deep(.el-button--text) {
  padding: 4px 0;
  font-size: 14px;
}

:deep(.el-button--text:hover) {
  color: #66b1ff;
}
</style> 