<template>
  <div class="message-management">
    <div class="header">
      <div class="header-title">留言管理</div>
      <div class="search-box">
        <el-input v-model="searchQuery" placeholder="搜索留言ID、用户ID或用户名" class="search-input" @keyup.enter="handleSearch">
          <template #append>
            <el-button @click="handleSearch">
              <el-icon>
                <Search />
              </el-icon>
            </el-button>
          </template>
        </el-input>
      </div>
    </div>

    <div class="filter-box">
      <el-select v-model="filterStatus" placeholder="筛选状态" clearable @change="handleFilterChange" style="width: 200px;">
        <el-option label="所有" value="" />
        <el-option label="待审核" value="pending" />
        <el-option label="已通过" value="accepted" />
        <el-option label="已拒绝" value="rejected" />
      </el-select>
    </div>

    <el-table :data="messageList" style="width: 100%" v-loading="loading" :key="tableKey">
      <el-table-column prop="id" label="留言ID" width="80" align="center" />
      <el-table-column label="用户信息" width="200" align="center">
        <template #default="{ row }">
          <div class="user-info">
            <el-avatar :size="40" :src="row.userphoto" />
            <div class="user-details">
              <div class="username">{{ row.username }}</div>
              <div class="user-id">ID: {{ row.userId }}</div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="scene" label="场景" width="120" align="center" />
      <el-table-column label="留言内容" min-width="200">
        <template #default="{ row }">
          <div class="content-cell">
            <div class="content-preview">{{ truncateContent(row.content) }}</div>
            <el-button v-if="row.content.length > 100" type="text" @click="showContentDetail(row)">
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
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="statusTagType(row.status)">
            {{ statusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" align="center">
        <template #default="{ row }">
          <el-button v-if="row.status === 'pending'" type="success" size="small" @click="handleAccept(row)">
            通过
          </el-button>
          <el-button v-if="row.status === 'pending'" type="warning" size="small" @click="handleReject(row)">
            拒绝
          </el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 内容详情对话框 -->
    <el-dialog v-model="contentDialogVisible" title="留言内容详情" width="50%" destroy-on-close>
      <div class="content-detail">
        {{ currentContent }}
      </div>
    </el-dialog>

    <div class="pagination">
      <el-pagination :current-page="currentPage" :page-size="pageSize" :total="total" :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
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
const messageList = ref([])
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

const statusText = (status) => {
  if (status === 'pending') return '待审核';
  if (status === 'accepted') return '已通过';
  if (status === 'rejected') return '已拒绝';
  return status;
}
const statusTagType = (status) => {
  if (status === 'pending') return 'warning';
  if (status === 'accepted') return 'success';
  if (status === 'rejected') return 'danger';
  return '';
}

const fetchMessages = async () => {
  loading.value = true
  try {
    const response = await axios.get('http://127.0.0.1:3000/admin/message/list/', {
      params: {
        page: currentPage.value,
        size: pageSize.value,
        status: filterStatus.value,
        query: searchQuery.value
      },
      headers: {
        Authorization: 'Bearer ' + store.state.user.token
      }
    })
    messageList.value = response.data.items
    total.value = response.data.total
    tableKey.value++
  } catch (error) {
    console.error('获取留言列表失败：', error);
    ElMessage.error('获取留言列表失败：' + (error.response?.data?.message || error.message));
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchMessages()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  fetchMessages()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchMessages()
}

const handleFilterChange = () => {
  currentPage.value = 1
  fetchMessages()
}

const handleAccept = async (row) => {
  try {
    await axios.put(
      `http://127.0.0.1:3000/admin/message/${row.id}/accept/`,
      {},
      {
        headers: {
          Authorization: 'Bearer ' + store.state.user.token
        }
      }
    )
    ElMessage.success('审核通过')
    fetchMessages()
  } catch (error) {
    console.error('审核通过失败：', error);
    ElMessage.error('审核通过失败：' + (error.response?.data?.message || error.message));
  }
}

const handleReject = async (row) => {
  try {
    await axios.put(
      `http://127.0.0.1:3000/admin/message/${row.id}/reject/`,
      {},
      {
        headers: {
          Authorization: 'Bearer ' + store.state.user.token
        }
      }
    )
    ElMessage.success('已拒绝')
    fetchMessages()
  } catch (error) {
    console.error('拒绝失败：', error);
    ElMessage.error('拒绝失败：' + (error.response?.data?.message || error.message));
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除这条留言吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await axios.delete(
        `http://127.0.0.1:3000/admin/message/${row.id}/`,
        {
          headers: {
            Authorization: 'Bearer ' + store.state.user.token
          }
        }
      )
      ElMessage.success('删除成功')
      fetchMessages()
    } catch (error) {
      console.error('删除失败：', error);
      ElMessage.error('删除失败：' + (error.response?.data?.message || error.message));
    }
  })
}

onMounted(() => {
  fetchMessages()
})
</script>

<style scoped>
.message-management {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  width: 100%;
  box-sizing: border-box;
  min-height: 500px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  width: 100%;
}
.header-title {
  text-align: center;
  color: #333;
  margin: 0;
  font-weight: 550 !important;
  font-size: 1.5em;
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
  padding-bottom: 20px;
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