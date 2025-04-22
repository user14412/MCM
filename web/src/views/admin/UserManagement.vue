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

    <el-table
      :data="paginatedUsers"
      :key="tableKey"
      style="width: 100%"
      v-loading="loading"
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

    <div class="pagination">
      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="totalUsers"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
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

// 获取筛选后的用户列表
const filteredUsers = computed(() => {
  return userList.value.filter(user => {
    const matchesSearch = !searchQuery.value || 
      user.id.toString().includes(searchQuery.value) || 
      user.username.includes(searchQuery.value);
    
    const matchesRole = !filterRole.value || 
      (filterRole.value === 'admin' && user.isAdmin) || 
      (filterRole.value === 'user' && !user.isAdmin);
    
    const matchesStatus = !filterStatus.value || 
      (filterStatus.value === 'active' && !user.isBanned) || 
      (filterStatus.value === 'banned' && user.isBanned);
    
    return matchesSearch && matchesRole && matchesStatus;
  });
});

// 计算总用户数
const totalUsers = computed(() => filteredUsers.value.length);

// 获取当前页的用户列表
const paginatedUsers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredUsers.value.slice(start, end);
});

const fetchUsers = async () => {
  loading.value = true
  try {
    const response = await axios.get('http://127.0.0.1:3000/admin/users/', {
      headers: {
        Authorization: 'Bearer ' + store.state.user.token
      }
    })
    userList.value = response.data.items
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  tableKey.value++
}

const handleFilterChange = () => {
  currentPage.value = 1
  tableKey.value++
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  tableKey.value++
}

const handleCurrentChange = (val) => {
  currentPage.value = val
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

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.user-management {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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
  justify-content: flex-end;
}
</style> 