<template>
    <div v-if="loadding" class="content-container">加载中...</div>
    <div v-else-if="error" class="content-container">错误：{{ error }}</div>
    <div v-else class="content-container">
        <div class="container" style="margin-top:-25px">
            <!-- 筛选和搜索区域 -->
            <div class="row mb-4">
                <div class="col-md-3">
                    <select v-model="filterCategory" class="form-select" @change="resetPagination">
                        <option value="">所有展区</option>
                        <option v-for="category in categories" :key="category" :value="category">{{ category }}</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <div class="input-group">
                        <input v-model="searchKeyword" type="text" class="form-control" placeholder="输入藏品名称">
                        <button @click="resetPagination" class="btn btn-primary">搜索</button>
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
            <nav class="mt-4">
                <ul class="pagination justify-content-center">
                    <li class="page-item" :class="{ disabled: currentPage === 1 }">
                        <a class="page-link" href="#" @click.prevent="currentPage--">上一页</a>
                    </li>
                    <li class="page-item" v-for="page in pageRange" :key="page"
                        :class="{ active: page === currentPage }">
                        <a class="page-link" href="#" @click.prevent="currentPage = page">{{ page }}</a>
                    </li>
                    <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                        <a class="page-link" href="#" @click.prevent="currentPage++">下一页</a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</template>

<script>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { onMounted } from 'vue'
import axios from 'axios'

export default{
    props:{
        userId: {
            type: Number,
            required: true,
        }
    },
    setup(props) {
        const router = useRouter();
        const store = useStore();

        // 藏品列表(从后端获取当前exhibit: id, primary_photo_id[外键], name, category; image: url[id === primary_photo_id])
        const exhibits = ref([])

        // 添加加载状态和错误
        const loading = ref(true)
        const error = ref(null)

        const fetchLikeExhibits = async () => {
            axios({
                url: "http://127.0.0.1:3000/exhibit/getlist/favorite/",
                method: "GET",
                params:{
                    userId: props.userId,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
            })
                .then((resp) => {
                    console.log("resp1", resp);
                    exhibits.value = resp.data;
                    loading.value = false; // 加载完成
                })
                .catch((err) => {
                    error.value = err;
                });
        }

        // 组件挂载时重新获取藏品列表
        onMounted(() => {
            fetchLikeExhibits();
        });

        // 响应式数据
        const filterCategory = ref('') // 筛选类别
        const searchKeyword = ref('') // 搜索关键字
        const currentPage = ref(1)
        const itemsPerPage = 12

        // 获取藏品类别列表
        const categories = computed(() =>
            [...new Set(exhibits.value.map(e => e.category))]
        )

        // 获取筛选后的藏品列表
        const filteredExhibits = computed(() =>
            exhibits.value.filter(e =>
                (filterCategory.value ? e.category === filterCategory.value : true) &&
                e.name.includes(searchKeyword.value)
            )
        )

        const totalItems = computed(() => filteredExhibits.value.length) // 筛选后的藏品总数
        const totalPages = computed(() => Math.ceil(totalItems.value / itemsPerPage)) // 总页数
        const pageRange = computed(() =>
            Array.from({ length: totalPages.value }, (_, i) => i + 1)
        )

        // 获取本页藏品列表
        const paginatedExhibits = computed(() =>
            filteredExhibits.value.slice(
                (currentPage.value - 1) * itemsPerPage,
                currentPage.value * itemsPerPage
            )
        )

        // 方法
        const resetPagination = () => currentPage.value = 1
        const goToDetail = (id) => router.push(`/exhibit/${id}/`)

        return {
            exhibits,
            loading,
            error,
            filterCategory,
            searchKeyword,
            currentPage,
            itemsPerPage,
            categories,
            filteredExhibits,
            totalItems,
            totalPages,
            pageRange,
            paginatedExhibits,
            resetPagination,
            goToDetail,
        }
    }
}
</script>

<style scoped>
/* 调整本页面与顶部（导航栏）之间的间距 */
.content-container {
    margin-top: 45px;
}

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