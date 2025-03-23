<template>
    <ContentField v-if="!$store.state.user.pulling_info">
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">提交</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'
import { useStore } from 'vuex'
import { ref } from 'vue'
import router from '../../../router/index'

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let error_message = ref('');

        const jwt_token = localStorage.getItem("jwt_token");
        if(jwt_token){
            // 从内存中读取用户的token
            store.commit("updateToken", jwt_token);
            // 从数据库中拉取user信息，更新store里的user信息
            store.commit("updatePullingInfo", true);
            store.dispatch("getinfo",{
                success(){
                    router.push({name: "home"});
                    // 拉取信息完毕，恢复pulling_info的值
                    store.commit("updatePullingInfo", false);
                },
                error(){
                    store.commit("updatePullingInfo", false);
                }
            })
        }

        const login = () => {
            error_message.value = "";
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success() {
                    store.commit("updatePullingInfo", true);
                    store.dispatch("getinfo", {
                        success() {
                            // 登录成功
                            // 获取用户信息：用户名、头像成功
                            // 跳转到主页
                            router.push({ name: 'home' });
                            // 拉取信息完毕，恢复pulling_info的值
                            store.commit("updatePullingInfo", false);
                        },
                        error(){
                            store.commit("updatePullingInfo", false);
                        }
                    })
                },
                error() {
                    error_message.value = "用户名或密码错误";
                }
            })
        }

        return {
            username,
            password,
            error_message,
            login,
        }
    }
}
</script>

<style scoped>
button {
    width: 100%;
}
div.error-message {
    color: red;
}
</style>
