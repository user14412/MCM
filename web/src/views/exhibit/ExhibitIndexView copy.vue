<template>
    <div class="container mt-4">
        <!-- 筛选和搜索区域 -->
        <div class="row mb-4">
            <div class="col-md-3">
                <select v-model="filterCategory" class="form-select" @change="resetPagination">
                    <option value="">所有类别</option>
                    <!-- <option v-for="category in categories" :value="category">{{ category }}</option> -->
                </select>
            </div>
            <div class="col-md-6">
                <div class="input-group">
                    <input v-model="searchKeyword" type="text" class="form-control" placeholder="输入藏品名称">
                    <button @click="resetPagination" class="btn btn-outline-secondary">搜索</button>
                </div>
            </div>
        </div>

        <!-- 展品列表 -->
        <div class="row row-cols-1 row-cols-md-4 g-4">
            <div class="col" v-for="exhibit in paginatedExhibits" :key="exhibit.id">
                <div class="card h-100" @click="goToDetail(exhibit.id)">
                    <img :src="exhibit.primaryPhoto" class="card-img-top exhibit-thumbnail" alt="展品图片">
                    <div class="card-body">
                        <h5 class="card-title fw-bold">{{ exhibit.name }}</h5>
                        <small class="text-muted">{{ exhibit.category }}</small>
                    </div>
                </div>
            </div>
        </div>

        <!-- 分页控件 -->
        <nav class="mt-4" v-if="totalPages > 1">
            <ul class="pagination justify-content-center">
                <li class="page-item" :class="{ disabled: currentPage === 1 }">
                    <a class="page-link" href="#" @click.prevent="currentPage--">上一页</a>
                </li>
                <li class="page-item" v-for="page in pageRange" :key="page" :class="{ active: page === currentPage }">
                    <a class="page-link" href="#" @click.prevent="currentPage = page">{{ page }}</a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                    <a class="page-link" href="#" @click.prevent="currentPage++">下一页</a>
                </li>
            </ul>
        </nav>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 模拟数据（实际应从API获取）
const exhibits = ref([
    {
        id: 1,
        name: '青铜鼎',
        category: '青铜器',
        primaryPhoto: 'https://example.com/image1.jpg',
        comment: '商代青铜器代表作',
        exhibition_hall: '第一展厅'
    },
    // 更多模拟数据...
])

// 响应式数据
const filterCategory = ref('')
const searchKeyword = ref('')
const currentPage = ref(1)
const itemsPerPage = 12

// 计算属性
// const categories = computed(() =>
//     [...new Set(exhibits.value.map(e => e.category))]
// )

const filteredExhibits = computed(() =>
    exhibits.value.filter(e =>
        (filterCategory.value ? e.category === filterCategory.value : true) &&
        e.name.includes(searchKeyword.value)
    )
)

const totalItems = computed(() => filteredExhibits.value.length)
const totalPages = computed(() => Math.ceil(totalItems.value / itemsPerPage))
const pageRange = computed(() =>
    Array.from({ length: totalPages.value }, (_, i) => i + 1)
)

const paginatedExhibits = computed(() =>
    filteredExhibits.value.slice(
        (currentPage.value - 1) * itemsPerPage,
        currentPage.value * itemsPerPage
    )
)

// 方法
const resetPagination = () => currentPage.value = 1
const goToDetail = (id) => router.push(`/exhibit/${id}`)
</script>

<style scoped>
.exhibit-thumbnail {
    height: 200px;
    object-fit: cover;
    cursor: pointer;
}

.card:hover {
    transform: translateY(-5px);
    transition: transform 0.2s ease;
}
</style>