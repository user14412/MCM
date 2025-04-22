<template>
  <div class="feedback-management">
    <div class="header">
      <h2>反馈管理</h2>
      <div class="search-box">
        <el-input
          v-model="searchQuery"
          placeholder="搜索反馈ID、用户ID或用户名"
          class="search-input"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>
    </div>

    <div class="filter-box">
      <el-select 
        v-model="filterStatus" 
        placeholder="筛选状态" 
        clearable 
        @change="handleFilterChange"
        style="width: 200px;"
      >
        <el-option label="所有" value="" />
        <el-option label="未解决" :value="false" />
        <el-option label="已解决" :value="true" />
      </el-select>
    </div>

    <el-table
      :data="feedbackList"
      style="width: 100%"
      v-loading="loading"
      :key="tableKey"
    >
      <el-table-column prop="id" label="反馈ID" width="80" align="center" />
      <el-table-column label="用户信息" width="200" align="center">
        <template #default="{ row }">
          <div class="user-info">
            <el-avatar :size="40" :src="row.userPhoto" />
            <div class="user-details">
              <div class="username">{{ row.username }}</div>
              <div class="user-id">ID: {{ row.userId }}</div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="contactInfo" label="联系方式" width="150" align="center" />
      <el-table-column label="反馈内容" min-width="200">
        <template #default="{ row }">
          <div class="content-cell">
            <div class="content-preview">{{ truncateContent(row.content) }}</div>
            <el-button 
              v-if="row.content.length > 100" 
              type="text" 
              @click="showContentDetail(row)"
            >
              查看全文
            </el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="createtime" label="提交时间" width="180" align="center">
        <template #default="{ row }">
          {{ formatDate(row.createtime) }}
        </template>
      </el-table-column>
      <el-table-column prop="resolved" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.resolved ? 'success' : 'warning'">
            {{ row.resolved ? '已解决' : '未解决' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template #default="{ row }">
          <el-button
            v-if="!row.resolved"
            type="success"
            size="small"
            @click="handleResolve(row)"
          >
            标记已解决
          </el-button>
          <el-button
            type="danger"
            size="small"
            @click="handleDelete(row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 内容详情对话框 -->
    <el-dialog
      v-model="contentDialogVisible"
      title="反馈内容详情"
      width="50%"
      destroy-on-close
    >
      <div class="content-detail">
        {{ currentContent }}
      </div>
    </el-dialog>

    <div class="pagination">
      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import axios from 'axios'
import { useStore } from 'vuex'

const store = useStore()
const feedbackList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterStatus = ref('')
const searchQuery = ref('')
const contentDialogVisible = ref(false)
const currentContent = ref('')
const tableKey = ref(0)

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}年${month}月${day}日`;
}

const truncateContent = (content) => {
  if (content.length > 100) {
    return content.substring(0, 100) + '...';
  }
  return content;
}

const showContentDetail = (row) => {
  currentContent.value = row.content;
  contentDialogVisible.value = true;
}

const fetchFeedbacks = async () => {
  loading.value = true
  try {
    const response = await axios.get('http://127.0.0.1:3000/admin/feedback/list/', {
      params: {
        page: currentPage.value,
        size: pageSize.value,
        resolved: filterStatus.value === '' ? undefined : filterStatus.value,
        query: searchQuery.value
      },
      headers: {
        Authorization: 'Bearer ' + store.state.user.token
      }
    })
    feedbackList.value = response.data.items
    total.value = response.data.total
    tableKey.value++
  } catch (error) {
    console.error('获取反馈列表失败：', error);
    ElMessage.error('获取反馈列表失败：' + (error.response?.data?.message || error.message));
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchFeedbacks()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  fetchFeedbacks()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchFeedbacks()
}

const handleFilterChange = () => {
  currentPage.value = 1
  fetchFeedbacks()
}

const handleResolve = async (row) => {
  try {
    await axios.put(
      `http://127.0.0.1:3000/admin/feedback/${row.id}/resolve/`,
      {},
      {
        headers: {
          Authorization: 'Bearer ' + store.state.user.token
        }
      }
    )
    ElMessage.success('标记已解决成功')
    fetchFeedbacks()
  } catch (error) {
    console.error('标记已解决失败：', error);
    ElMessage.error('标记已解决失败：' + (error.response?.data?.message || error.message));
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除这条反馈吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await axios.delete(
        `http://127.0.0.1:3000/admin/feedback/${row.id}/`,
        {
          headers: {
            Authorization: 'Bearer ' + store.state.user.token
          }
        }
      )
      ElMessage.success('删除成功')
      fetchFeedbacks()
    } catch (error) {
      console.error('删除失败：', error);
      ElMessage.error('删除失败：' + (error.response?.data?.message || error.message));
    }
  })
}

onMounted(() => {
  fetchFeedbacks()
})
</script>

<style scoped>
.feedback-management {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  width: 100%;
  box-sizing: border-box;
  min-height: 500px; /* 添加最小高度 */
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  width: 100%;
}

.search-box {
  width: 300px;
}

.filter-box {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  width: 100%;
  padding-bottom: 20px; /* 添加底部内边距 */
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-details {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.username {
  font-weight: bold;
}

.user-id {
  font-size: 12px;
  color: #666;
}

.content-cell {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.content-preview {
  word-break: break-all;
}

.content-detail {
  white-space: pre-wrap;
  word-break: break-all;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

:deep(.el-table) {
  width: 100% !important;
}

:deep(.el-pagination) {
  justify-content: center;
}
</style> 