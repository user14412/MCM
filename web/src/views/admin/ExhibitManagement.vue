<template>
  <div class="exhibit-management">
    <div class="header">
      <h2>展品管理</h2>
      <div class="search-box">
        <el-input
          v-model="searchQuery"
          placeholder="搜索展品名称"
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
      <el-select v-model="filterCategory" placeholder="分类筛选" clearable @change="handleFilterChange">
        <el-option label="所有" value="" />
        <el-option 
          v-for="category in categories" 
          :key="category" 
          :label="category" 
          :value="category" 
        />
      </el-select>
      <el-button type="primary" @click="handleAdd">添加展品</el-button>
    </div>

    <el-table
      :data="paginatedExhibits"
      style="width: 100%"
      v-loading="loading"
      :resize-observer="false"
      :key="tableKey"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="主图" width="120">
        <template #default="{ row }">
          <el-image 
            :src="row.primaryPhoto" 
            :preview-src-list="[row.primaryPhoto]"
            fit="cover"
            style="width: 80px; height: 80px;"
          />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="category" label="分类" />
      <el-table-column label="操作" width="300">
        <template #default="{ row }">
          <el-button @click="handleEdit(row)">编辑</el-button>
          <el-button @click="handleManageImages(row)">管理图片</el-button>
          <el-button type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="totalExhibits"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :resize-observer="false"
        :key="paginationKey"
      />
    </div>

    <!-- 添加/编辑展品对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="50%"
    >
      <el-form :model="exhibitForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="exhibitForm.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input 
            v-model="exhibitForm.comment" 
            type="textarea" 
            :rows="6"
            placeholder="请输入展品描述，建议200字左右"
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="exhibitForm.category">
            <el-option 
              v-for="category in categories" 
              :key="category" 
              :label="category" 
              :value="category" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="主图">
          <input 
            type="file" 
            @change="handleFileChange"
            accept="image/*"
            style="margin-bottom: 10px;"
          />
          <el-button type="primary" @click="uploadFile">上传主图</el-button>
          <el-image
            v-if="exhibitForm.primaryPhoto"
            :src="exhibitForm.primaryPhoto"
            style="width: 100px; height: 100px; margin-top: 10px;"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 管理图片对话框 -->
    <el-dialog
      v-model="imageDialogVisible"
      title="管理图片"
      width="70%"
    >
      <div class="image-management">
        <div class="upload-box">
          <input 
            type="file" 
            @change="handleImageFileChange"
            accept="image/*"
            style="margin-bottom: 10px;"
          />
          <el-button type="primary" @click="uploadImageFile">上传图片</el-button>
        </div>
        <div class="image-grid">
          <div v-for="image in exhibitImages" :key="image.id" class="image-item">
            <el-image
              :src="image.url"
              :preview-src-list="[image.url]"
              fit="cover"
              style="width: 150px; height: 150px;"
            />
            <el-button
              type="danger"
              size="small"
              @click="handleDeleteImage(image)"
              style="margin-top: 10px;"
            >
              删除
            </el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import axios from 'axios'
import { useStore } from 'vuex'

const store = useStore()
const searchQuery = ref('')
const filterCategory = ref('')
const exhibitList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const imageDialogVisible = ref(false)
const dialogTitle = ref('添加展品')
const exhibitForm = ref({
  id: null,
  name: '',
  comment: '',
  category: '',
  primaryPhoto: ''
})
const exhibitImages = ref([])
const currentExhibitId = ref(null)
const file = ref(null)
const imageFile = ref(null)
const totalExhibits = ref(0)
const tableKey = ref(0)
const paginationKey = ref(0)

// 获取所有分类
const categories = computed(() => {
  return [...new Set(exhibitList.value.map(e => e.category))]
})

// 获取筛选后的展品列表
const filteredExhibits = computed(() => {
  return exhibitList.value.filter(exhibit => {
    const matchesSearch = !searchQuery.value || 
      exhibit.id.toString().includes(searchQuery.value) || 
      exhibit.name.includes(searchQuery.value);
    
    const matchesCategory = !filterCategory.value || 
      exhibit.category === filterCategory.value;
    
    return matchesSearch && matchesCategory;
  });
});

// 获取当前页的展品列表
const paginatedExhibits = computed(() => {
  return filteredExhibits.value
});

const fetchExhibits = async () => {
  loading.value = true
  try {
    const response = await axios.get('http://127.0.0.1:3000/admin/exhibits/', {
      params: {
        page: currentPage.value,
        size: pageSize.value,
        query: searchQuery.value
      },
      headers: {
        Authorization: 'Bearer ' + store.state.user.token
      }
    })
    exhibitList.value = response.data.items
    totalExhibits.value = response.data.total || response.data.items.length
  } catch (error) {
    ElMessage.error('获取展品列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchExhibits()
}

const refreshComponents = () => {
  tableKey.value++
  paginationKey.value++
}

const handleFilterChange = async () => {
  currentPage.value = 1
  await nextTick()
  refreshComponents()
  fetchExhibits()
}

const handleSizeChange = async (val) => {
  pageSize.value = val
  currentPage.value = 1
  await nextTick()
  refreshComponents()
  fetchExhibits()
}

const handleCurrentChange = async (val) => {
  currentPage.value = val
  await nextTick()
  refreshComponents()
  fetchExhibits()
}

const handleAdd = () => {
  dialogTitle.value = '添加展品'
  exhibitForm.value = {
    id: null,
    name: '',
    comment: '',
    category: '',
    primaryPhoto: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑展品'
  exhibitForm.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    if (!exhibitForm.value.name) {
      ElMessage.error('请输入展品名称');
      return;
    }
    if (!exhibitForm.value.category) {
      ElMessage.error('请选择展品分类');
      return;
    }
    if (!exhibitForm.value.primaryPhoto) {
      ElMessage.error('请上传展品主图');
      return;
    }

    const url = exhibitForm.value.id 
      ? `http://127.0.0.1:3000/admin/exhibits/${exhibitForm.value.id}/`
      : 'http://127.0.0.1:3000/admin/exhibits/';
    
    const method = exhibitForm.value.id ? 'put' : 'post';
    
    // 使用FormData发送数据
    const formData = new FormData();
    formData.append('name', exhibitForm.value.name);
    formData.append('comment', exhibitForm.value.comment || '');
    formData.append('category', exhibitForm.value.category);
    formData.append('primaryPhoto', exhibitForm.value.primaryPhoto);
    
    console.log('发送的请求数据：', Object.fromEntries(formData));  // 添加日志
    
    const response = await axios({
      url,
      method,
      data: formData,
      headers: {
        Authorization: 'Bearer ' + store.state.user.token,
        'Content-Type': 'multipart/form-data'
      }
    });

    console.log('服务器响应：', response.data);  // 添加日志
    
    ElMessage.success(exhibitForm.value.id ? '展品更新成功' : '展品添加成功');
    dialogVisible.value = false;
    fetchExhibits();
  } catch (error) {
    console.error('操作失败：', error);
    console.error('错误详情：', error.response?.data);  // 添加详细错误日志
    ElMessage.error('操作失败：' + (error.response?.data?.message || error.message));
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除展品 ${row.name} 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await axios.delete(
        `http://127.0.0.1:3000/admin/exhibits/${row.id}/`,
        {
          headers: {
            Authorization: 'Bearer ' + store.state.user.token
          }
        }
      )
      ElMessage.success('展品删除成功')
      fetchExhibits()
    } catch (error) {
      console.error('删除失败：', error);
      ElMessage.error('删除失败：' + (error.response?.data?.message || error.message));
    }
  })
}

const handleManageImages = async (row) => {
  currentExhibitId.value = row.id
  try {
    const response = await axios.get(
      `http://127.0.0.1:3000/admin/exhibits/${row.id}/images/`,
      {
        headers: {
          Authorization: 'Bearer ' + store.state.user.token
        }
      }
    )
    exhibitImages.value = response.data.items
    imageDialogVisible.value = true
  } catch (error) {
    console.error('获取图片列表失败：', error);
    ElMessage.error('获取图片列表失败：' + (error.response?.data?.message || error.message));
  }
}

const handleFileChange = (event) => {
  file.value = event.target.files[0];
};

const uploadFile = async () => {
  if (!file.value) {
    ElMessage.error('请选择文件');
    return;
  }
  
  const formData = new FormData();
  formData.append("file", file.value);
  
  try {
    const response = await axios({
      url: "http://127.0.0.1:3000/upload/image/",
      method: "POST",
      data: formData,
      headers: {
        Authorization: "Bearer " + store.state.user.token,
        'Content-Type': 'multipart/form-data'
      }
    });
    
    exhibitForm.value.primaryPhoto = response.data;
    ElMessage.success('上传成功');
  } catch (error) {
    console.error('上传失败：', error);
    ElMessage.error('上传失败：' + (error.response?.data?.message || error.message));
  }
};

const handleImageFileChange = (event) => {
  imageFile.value = event.target.files[0];
};

const uploadImageFile = async () => {
  if (!imageFile.value) {
    ElMessage.error('请选择文件');
    return;
  }
  
  const formData = new FormData();
  formData.append("file", imageFile.value);
  
  try {
    const response = await axios({
      url: "http://127.0.0.1:3000/upload/image/",
      method: "POST",
      data: formData,
      headers: {
        Authorization: "Bearer " + store.state.user.token,
        'Content-Type': 'multipart/form-data'
      }
    });
    
    // 上传成功后添加图片到展品
    const imageFormData = new FormData();
    imageFormData.append('imageUrl', response.data);
    
    await axios.post(
      `http://127.0.0.1:3000/admin/exhibits/${currentExhibitId.value}/images/`,
      imageFormData,
      {
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
          'Content-Type': 'multipart/form-data'
        }
      }
    );
    
    ElMessage.success('图片上传成功');
    handleManageImages({ id: currentExhibitId.value });
  } catch (error) {
    console.error('上传失败：', error);
    ElMessage.error('上传失败：' + (error.response?.data?.message || error.message));
  }
};

const handleDeleteImage = async (image) => {
  ElMessageBox.confirm(
    '确定要删除这张图片吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await axios.delete(
        `http://127.0.0.1:3000/admin/exhibits/images/${image.id}/`,
        {
          headers: {
            Authorization: 'Bearer ' + store.state.user.token
          }
        }
      )
      ElMessage.success('图片删除成功')
      handleManageImages({ id: currentExhibitId.value })
    } catch (error) {
      console.error('删除失败：', error);
      ElMessage.error('删除失败：' + (error.response?.data?.message || error.message));
    }
  })
}

onMounted(() => {
  fetchExhibits()
})
</script>

<style scoped>
.exhibit-management {
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

.image-management {
  margin-top: 20px;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.image-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.upload-box {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style> 