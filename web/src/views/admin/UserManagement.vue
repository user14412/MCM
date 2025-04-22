<template>
  <div class="user-management">
    <div class="header">
      <h2>用户管理</h2>
      <div class="search-box">
        <el-input
          v-model="searchQuery"
          placeholder="搜索用户ID或用户名"
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
      <el-select v-model="filterRole" placeholder="角色筛选" clearable @change="handleFilterChange">
        <el-option label="所有" value="" />
        <el-option label="管理员" value="admin" />
        <el-option label="普通用户" value="user" />
      </el-select>
      <el-select v-model="filterStatus" placeholder="状态筛选" clearable @change="handleFilterChange">
        <el-option label="所有" value="" />
        <el-option label="正常" value="active" />
        <el-option label="已封禁" value="banned" />
      </el-select>
    </div>

    <div class="table-container">
      <el-table
        :data="paginatedUsers"
        :key="tableKey"
        style="width: 100%"
        v-loading="loading"
        :resize-observer="false"
        :height="tableHeight"
        :virtual-scroll="false"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="photo" label="头像" width="100">
          <template #default="{ row }">
            <el-avatar :size="40" :src="row.photo" />
          </template>
        </el-table-column>
        <el-table-column prop="isAdmin" label="角色">
          <template #default="{ row }">
            <el-tag :type="row.isAdmin ? 'danger' : 'info'">
              {{ row.isAdmin ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isBanned" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.isBanned ? 'danger' : 'success'">
              {{ row.isBanned ? '已封禁' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button
              :type="row.isBanned ? 'success' : 'danger'"
              size="small"
              @click="handleToggleBan(row)"
            >
              {{ row.isBanned ? '解封' : '封禁' }}
            </el-button>
            <el-button
              type="primary"
              size="small"
              @click="handleToggleAdmin(row)"
              :disabled="row.isAdmin"
            >
              设为管理员
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="totalUsers"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :resize-observer="false"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, onBeforeUnmount } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import axios from 'axios'
import { useStore } from 'vuex'

const store = useStore()
const searchQuery = ref('')
const filterRole = ref('')
const filterStatus = ref('')
const userList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const tableKey = ref(0)
const totalUsers = ref(0)
const tableHeight = ref(600)  // 设置固定高度

const paginatedUsers = computed(() => userList.value)

const fetchUsers = async () => {
  loading.value = true
  try {
    const response = await axios.get('http://127.0.0.1:3000/admin/users/', {
      params: {
        page: currentPage.value,
        size: pageSize.value,
        query: searchQuery.value,
        role: filterRole.value,
        status: filterStatus.value
      },
      headers: {
        Authorization: 'Bearer ' + store.state.user.token
      }
    })
    userList.value = response.data.items
    totalUsers.value = response.data.total || response.data.items.length
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  currentPage.value = 1
  await nextTick()
  fetchUsers()
}

const handleFilterChange = async () => {
  currentPage.value = 1
  await nextTick()
  fetchUsers()
}

const handleSizeChange = async (val) => {
  pageSize.value = val
  currentPage.value = 1
  await nextTick()
  fetchUsers()
}

const handleCurrentChange = async (val) => {
  currentPage.value = val
  await nextTick()
  fetchUsers()
}

const handleToggleBan = (user) => {
  const action = user.isBanned ? '解封' : '封禁'
  ElMessageBox.confirm(
    `确定要${action}用户 ${user.username} 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await axios.post(
        `http://127.0.0.1:3000/admin/users/${user.id}/ban/`,
        {
          ban: !user.isBanned
        },
        {
          headers: {
            Authorization: 'Bearer ' + store.state.user.token
          }
        }
      )
      ElMessage.success(`用户已${action}`)
      fetchUsers()
    } catch (error) {
      ElMessage.error(`${action}用户失败`)
    }
  })
}

const handleToggleAdmin = (user) => {
  ElMessageBox.confirm(
    `确定要将用户 ${user.username} 设为管理员吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await axios.post(
        `http://127.0.0.1:3000/admin/users/${user.id}/admin/`,
        {
          isAdmin: true
        },
        {
          headers: {
            Authorization: 'Bearer ' + store.state.user.token
          }
        }
      )
      ElMessage.success('已设置为管理员')
      fetchUsers()
    } catch (error) {
      ElMessage.error('设置管理员失败')
    }
  })
}

// 添加窗口大小变化监听
const handleResize = () => {
  // 可以根据需要调整表格高度
  tableHeight.value = window.innerHeight - 300
}

onMounted(() => {
  handleResize()
  window.addEventListener('resize', handleResize)
  fetchUsers()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.user-management {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-shrink: 0;
}

.search-box {
  width: 300px;
}

.filter-box {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
  flex-shrink: 0;
}

.table-container {
  flex: 1;
  overflow: hidden;
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  flex-shrink: 0;
}
</style> 