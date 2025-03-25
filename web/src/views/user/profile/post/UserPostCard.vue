<template>
     <div 
        class="card mb-3 hover-card"
        @mouseenter="isHovered = true"
        @mouseleave="isHovered = false"
        :style="cardHoverStyle"
    >

        <div class="card">
            <div class="row g-0">
                <!-- 头像列垂直顶端对齐 -->
                <div class="col-md-1 d-flex align-items-start">
                    <img 
                        :src="article.photo" 
                        class="img-fluid rounded-circle mt-2 mx-auto"
                        style="max-width: 50px; height: 50px; object-fit: cover;"
                        alt="用户头像">
                </div>
                
                <div class="col-md-11">
                    <div class="card-body p-3">
                        <!-- 用户名与时间区域 -->
                        <div class="d-flex align-items-baseline gap-2 text-muted mb-2">
                            <h6 class="fw-bold mb-0">{{ article.username }}</h6>
                            <span class="fs-7">
                                {{ formatDate(article.createtime) }} · 发布了文章
                            </span>
                        </div>

                        <!-- 文章标题 -->
                        <h5 class="card-title fw-bold mb-2">{{ article.title }}</h5>

                        <!-- 正文内容 -->
                        <p class="card-text text-secondary fs-6 mb-0">
                            {{ truncatedContent }}
                            <router-link 
                                :to="`/article/${article.id}/`" 
                                class="link-primary text-decoration-none">
                                全文
                            </router-link>
                        </p>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
export default {
    props: {
        article: {
            type: Object,
            required: true,
        }
    },
    data() {
        return {
            isHovered: false
        }
    },
    computed: {
        truncatedContent() {
            const maxLength = 186;
            return this.article.content.length > maxLength 
                ? this.article.content.substring(0, maxLength) + '...'
                : this.article.content;
        },
        cardHoverStyle() {
            return {
                transform: this.isHovered ? 'translateY(-2px)' : 'none',
                transition: 'all 0.3s ease'
            }
        },
    },
    methods: {
        formatDate(date) {
            if (!date) return '';
            return new Date(date).toLocaleString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            }).replace(/\//g, '年')
              .replace('年', '年')
              .replace('月', '月')
              .replace('日', '日');
        }
    }
};
</script>

<style scoped>
.hover-card {
    border: none; /* 去除默认边框 */
    cursor: pointer;
    transition: all 0.3s ease;
}

.hover-card:hover {
    box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08); /* 原0.15→0.08 */
}
.card {
    border: none !important; /* 强制去除默认边框 */
    background-clip: unset; /* 防止背景溢出 */
}
.fs-7 {
    font-size: 0.85rem !important;
}
.align-items-baseline {
    align-items: baseline !important;
}
</style>