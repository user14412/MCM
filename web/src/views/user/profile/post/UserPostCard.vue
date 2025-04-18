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
                            {{ truncatedContent() }}
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
import { ref } from 'vue';

export default {
    props: {
        article: {
            type: Object,
            required: true,
        }
    },
    setup(props){
        let isHovered = ref(false);

        const truncatedContent = () => { // 超出预览字数上限截断内容
            // 注意代码的鲁棒性
            // content === null 时直接访问其content字段会报错
            if (props.article.content === null || props.article.content === undefined){
                return '';
            }
            const str_show = props.article.content.replace(/<[^>]+>/g, '');
            if(str_show === null || str_show === undefined){
                return '';
            }
            // console.log("str_show: ", str_show);
            const maxLength = 186;
            return str_show.length > maxLength 
                ? str_show.substring(0, maxLength) + '...'
                : str_show;
        }

        const cardHoverStyle = () => { // 卡片悬浮样式
            return {
                transform: isHovered.value ? 'translateY(-2px)' : 'none',
                transition: 'all 0.3s ease'
            }
        }

        const formatDate = (date) => { // 格式化日期
            if (!date) return '';
            date = date.substring(0, 10);
            return date.replace('-', '年').replace('-', '月') + '日';
        }

        return {
            isHovered,
            truncatedContent,
            cardHoverStyle,
            formatDate,
        }
    },
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