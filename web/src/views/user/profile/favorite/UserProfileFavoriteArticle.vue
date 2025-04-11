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
         
        // 获取用户点赞的所有文章的id列表
        axios({
            url: "http://127.0.0.1:3000/article/getlist/favorite/",
            method: "GET",
            headers: {
                Authorization: "Bearer " + store.state.user.token
            },
        })
        .then((resp) =>{
            console.log("resp", resp.data)
            posts.posts = resp.data;
            posts.count = posts.posts.length;
        })
        .catch((error) => {
            console.log("error", error);
        })

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