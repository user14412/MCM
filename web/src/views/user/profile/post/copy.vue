<template>
    <div v-for="post in posts.posts" :key="post.id" class="article-item">
        <UserPostCard :article="post" />
    </div>
</template>

<script>
import UserPostCard from '@/views/user/profile/post/UserPostCard.vue';
import { useStore } from 'vuex';
import { computed } from 'vue';
import { reactive } from 'vue';
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

        axios({
            url: "http://127.0.0.1:3000/article/get/user/",
            method: "GET",
            params: {
                userId: user.value.id,
            },
            headers: {
                Authorization: "Bearer " + store.state.user.token
            }
        })
            .then(response => {
                // console.log("getArticleByUser 请求成功:", response.data);
                posts.posts = response.data; // 响应数据需从response.data获取[1,5](@ref)
                posts.count = posts.posts.length;
            })
            .catch(error => {
                if (error.response) {
                    // 服务器返回了非2xx状态码的错误
                    console.error("getArticleByUser 请求失败:", error.response.data);
                } else {
                    // 网络错误或无响应（如跨域问题）
                    console.error("网络错误:", error.message);
                }
            });

        return {
            user,
            posts,
        }
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
    /* 修正拼写错误 */
    padding: 12px 0;
    /* 调整间距为12px */
}

.article-item:not(:last-child) {
    margin-bottom: 8px;
    /* 新增间隔控制 */
}
</style>