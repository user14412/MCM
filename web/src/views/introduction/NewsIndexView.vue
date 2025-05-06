<template>
    <ContentField>
        <div class="news-container">
            <!-- 搜索框 -->
            <div class="search-box mb-4">
                <div class="input-group">
                    <input type="text" class="form-control" v-model="keyword" placeholder="搜索新闻标题..."
                        @keyup.enter="handleSearch">
                    <button class="btn btn-primary" @click="handleSearch">
                        <i class="bi bi-search"></i> 搜索
                    </button>
                </div>
            </div>

            <!-- 新闻列表 -->
            <div class="news-list">
                <div v-if="loading" class="text-center py-5">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">加载中...</span>
                    </div>
                </div>
                <div v-else-if="articles.length === 0" class="text-center py-5">
                    <p class="text-muted">暂无新闻</p>
                </div>
                <div v-else>
                    <div v-for="article in articles" :key="article.id" class="news-item card mb-3"
                        @click="goToArticle(article.id)">
                        <div class="card-body">
                            <h5 class="card-title">{{ article.title }}</h5>
                            <div class="card-text d-flex align-items-center gap-3 text-muted">
                                <div class="d-flex align-items-center gap-2">
                                    <img :src="article.photo" class="rounded-circle"
                                        style="width: 24px; height: 24px; object-fit: cover;">
                                    <span>{{ article.username }}</span>
                                </div>
                                <span>{{ formatDate(article.createtime) }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 分页 -->
            <div class="pagination-container d-flex justify-content-center mt-4">
                <nav aria-label="Page navigation">
                    <ul class="pagination">
                        <li class="page-item" :class="{ disabled: currentPage === 1 }">
                            <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">上一页</a>
                        </li>
                        <li v-for="page in totalPages" :key="page" class="page-item"
                            :class="{ active: currentPage === page }">
                            <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
                        </li>
                        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                            <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">下一页</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import axios from 'axios';
import { useStore } from 'vuex';
import router from '@/router';

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        return {
            store,
        };
    },
    data() {
        return {
            articles: [],
            loading: false,
            currentPage: 1,
            pageSize: 10,
            total: 0,
            keyword: '',
        };
    },
    computed: {
        totalPages() {
            return Math.ceil(this.total / this.pageSize);
        }
    },
    created() {
        this.fetchArticles();
    },
    methods: {
        async fetchArticles() {
            this.loading = true;
            try {
                const response = await axios.get('http://127.0.0.1:3000/article/get/news/', {
                    params: {
                        page: this.currentPage,
                        size: this.pageSize,
                        keyword: this.keyword
                    },
                    headers: {
                        Authorization: "Bearer " + this.store.state.user.token,
                    }
                });
                this.articles = response.data.articles;
                this.total = response.data.total;
            } catch (error) {
                console.error('获取新闻列表失败:', error);
            } finally {
                this.loading = false;
            }
        },
        handleSearch() {
            this.currentPage = 1;
            this.fetchArticles();
        },
        changePage(page) {
            if (page < 1 || page > this.totalPages) return;
            this.currentPage = page;
            this.fetchArticles();
        },
        goToArticle(articleId) {
            router.push(`/article/${articleId}`);
        },
        formatDate(date) {
            if (!date) return '';
            // 创建日期对象并添加8小时
            const d = new Date(date);
            d.setHours(d.getHours() + 8);
            return d.toLocaleString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit'
            });
        }
    }
}
</script>

<style scoped>
.news-container {
    max-width: 900px;
    margin: 0 auto;
    padding: 20px;
}

.search-box {
    max-width: 600px;
    margin: 0 auto;
}

.news-item {
    cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s;
}

.news-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-title {
    margin-bottom: 0.5rem;
    font-size: 1.1rem;
    color: #333;
}

.card-text {
    font-size: 0.9rem;
}

.pagination {
    margin-bottom: 0;
}

.page-link {
    color: #0d6efd;
}

.page-item.active .page-link {
    background-color: #0d6efd;
    border-color: #0d6efd;
}
</style>