<template>
    <div>
        <!-- 搜索区域 -->
        <div class="mb-4">
            <div class="input-group">
                <input v-model="searchKeyword" type="text" class="form-control" placeholder="输入文章标题">
                <button @click="resetPagination" class="btn btn-primary">搜索</button>
            </div>
        </div>

        <!-- 文章列表 -->
        <div v-for="post in paginatedPosts" :key="post.id" class="article-item">
            <UserPostCard :article="post" />
        </div>

        <!-- 分页控件 -->
        <nav class="mt-4">
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

<script>
import UserPostCard from '@/views/user/profile/post/UserPostCard.vue';
import { useStore } from 'vuex';
import { computed, reactive, ref } from 'vue';
import axios from 'axios';

export default {
    components: {
        UserPostCard,
    },
    setup() {
        const store = useStore();
        let user = computed(() => store.state.user);
        const posts = reactive({
            count: 0,
            posts: [],
        });

        // 响应式数据
        const searchKeyword = ref('');
        const currentPage = ref(1);
        const itemsPerPage = 5;

        // 获取用户点赞的所有文章的id列表
        axios({
            url: "http://127.0.0.1:3000/article/getlist/like/",
            method: "GET",
            params: {
                userId: user.value.id,
            },
            headers: {
                Authorization: "Bearer " + store.state.user.token
            },
        })
            .then((resp) => {
                posts.posts = resp.data;
                posts.count = posts.posts.length;
            })
            .catch((error) => {
                console.log("error", error);
            });

        // 计算属性
        const filteredPosts = computed(() =>
            posts.posts.filter(post =>
                post.title.includes(searchKeyword.value)
            )
        );

        const totalItems = computed(() => filteredPosts.value.length);
        const totalPages = computed(() => Math.ceil(totalItems.value / itemsPerPage));
        const pageRange = computed(() =>
            Array.from({ length: totalPages.value }, (_, i) => i + 1)
        );

        const paginatedPosts = computed(() =>
            filteredPosts.value.slice(
                (currentPage.value - 1) * itemsPerPage,
                currentPage.value * itemsPerPage
            )
        );

        // 方法
        const resetPagination = () => currentPage.value = 1;

        return {
            user,
            posts,
            searchKeyword,
            currentPage,
            totalPages,
            pageRange,
            paginatedPosts,
            resetPagination,
        };
    }
}
</script>

<style scoped>
.my-post-title {
    font-size: 20px;
    margin-bottom: 20px;
}

.article-item {
    border-bottom: 1px solid #eee;
    padding: 12px 0;
}

.article-item:not(:last-child) {
    margin-bottom: 8px;
}
</style>