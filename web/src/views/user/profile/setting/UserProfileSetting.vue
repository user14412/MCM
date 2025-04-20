<template>
    <div style="margin-top: 20px;">
        <div>
            <h3>修改用户名</h3>
            <input type="text" v-model="username" placeholder="输入新用户名">
            <button @click="updateUsername()" type="button" class="btn btn-primary">
                提交
            </button>
        </div>
        <div>
            <h3>更换头像</h3>
            <input type="file" @change="handleFileChange">
            <button @click="uploadFile()" type="button" class="btn btn-primary">
                提交
            </button>
        </div>
        <div>
            <h3>修改密码</h3>
            <input type="password" v-model="newPassword" placeholder="输入新密码">
            <input type="password" v-model="confirmPassword" placeholder="确认新密码">
            <button @click="updatePassword()" type="button" class="btn btn-primary">
                提交
            </button>
        </div>
    </div>
</template>

<script>
import { useStore } from "vuex";
import { ref } from 'vue'
import axios from 'axios';

export default {
    setup() {
        const store = useStore();

        const file = ref(null);
        const username = ref('');
        const newPassword = ref('');
        const confirmPassword = ref('');

        const handleFileChange = (event) => {
            file.value = event.target.files[0];
            console.log(file.value);
        };

        const uploadFile = () => {
            if (!file.value) {
                alert("请上传文件");
                return;
            }
            const formData = new FormData();
            formData.append("file", file.value);
            try {
                console.log(file.value);
                console.log(formData);
                // 在这里实现文件上传逻辑
                axios({
                    url: "http://127.0.0.1:3000/upload/image/",
                    method: "POST",
                    data: formData,
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                        'Content-Type': 'multipart/form-data' // 设置正确的Content-Type
                    },
                })
                    .then((response) => {
                        const resp = response.data;
                        console.log("resp", resp);
                        
                        // 修改头像
                        updateUserPhoto(resp);
                    })
                    .catch((error) => {
                        console.log("error", error);
                    });
                // alert("上传成功");
            } catch (error) {
                console.error(error);
            }
        };

        const updateUserPhoto = (imageUrl) => {
            axios({
                url: "http://127.0.0.1:3000/user/photo/update/",
                method: "POST",
                params:{
                    imageUrl: imageUrl,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                }
            })
                .then((response) => {
                    const resp = response.data;
                    if (resp === "success") {
                        alert("头像修改成功，请刷新查看");
                    } else {
                        alert("头像修改失败，请重试");
                    }

                })
                .catch((error) => {
                    console.log("error", error);
                })
                
        }

        const updateUsername = () => {
            if (!username.value) {
                alert("请输入用户名");
                return;
            }
            axios({
                url: "http://127.0.0.1:3000/user/username/update/",
                method: "POST",
                params: {
                    username: username.value,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                }
            })
                .then((response) => {
                    const resp = response.data;
                    if (resp === "success") {
                        alert("用户名修改成功，请刷新查看");
                    } else {
                        alert("用户名修改失败，请重试");
                    }
                })
                .catch((error) => {
                    console.log("error", error);
                })
        }

        const updatePassword = () => {
            if (!newPassword.value || !confirmPassword.value) {
                alert("请输入新密码和确认密码");
                return;
            }
            if (newPassword.value !== confirmPassword.value) {
                alert("两次输入的密码不一致");
                return;
            }
            axios({
                url: "http://127.0.0.1:3000/user/password/update/",
                method: "POST",
                params: {
                    newPassword: newPassword.value,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                }
            })
                .then((response) => {
                    const resp = response.data;
                    if (resp === "success") {
                        alert("密码修改成功");
                    } else {
                        alert("密码修改失败，请重试");
                    }
                })
                .catch((error) => {
                    console.log("error", error);
                });
        };

        return {
            file,
            handleFileChange,
            uploadFile,
            updateUserPhoto,
            username,
            updateUsername,
            newPassword,
            confirmPassword,
            updatePassword,
        };
    }
}
</script>

<style scoped>
    div {
        margin-bottom: 20px;
    }

    h3 {
        font-size: 18px;
        font-weight: bold;
    }

    input[type="text"], input[type="password"] {
        width: 100%;
        padding: 10px;
        margin-top: 5px;
        margin-bottom: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
    }

    button {
        background-color: #007BFF;
        color: white;
        padding: 10px 20px;
        margin-top: 10px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    button:hover {
        background-color: #0056b3;
    }
</style>
